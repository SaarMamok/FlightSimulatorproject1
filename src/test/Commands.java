package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Commands {
	
	// Default IO interface
	public interface DefaultIO{
		public String readText();
		public void write(String text);
		public float readVal();
		public void write(float val);
		public void close();

	}
	
	// the default IO to be used in all commands
	DefaultIO dio;
	public Commands(DefaultIO dio) {
		this.dio=dio;
	}

	// the shared state of all commands
	private class SharedState{
		SimpleAnomalyDetector simpleAD=new SimpleAnomalyDetector(0.9f);
		String train_CSV_file = "anomalyTrain.csv";
		String test_CSV_file  = "anomalyTest.csv";
		TimeSeries trainTimeSeries;
		TimeSeries testTimeSeries;
		List<AnomalyReport> AnomalyReportList = new ArrayList<>();
	}

	private  SharedState sharedState=new SharedState();


	// Command abstract class
	public abstract class Command{
		protected String description;

		public Command(String description) {
			this.description=description;
		}

		public abstract void execute();
	}

	// My functions
	public void uploadFile(String fileName)
	{
		try {
			PrintWriter writer = new PrintWriter(fileName);
			String line = dio.readText();
			while (!line.equals("done"))
			{
				writer.print(line);
				writer.print("\n");
				line = dio.readText();
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dio.write("Upload complete.\n");
	}


	public ArrayList<Long[]> readNumbers() {
		ArrayList<Long[]>arr = new ArrayList<Long[]>(); // Notice that the size is very big
		//Arrays.fill(arr,"\0");
		int col =0,row =0;
		String[] line = dio.readText().split(",");
		while (!line[0].equals("done"))
		{
			Long[] temp = new Long[2];
			temp[0] = Long.parseLong(line[0]);
			temp[1] = Long.parseLong(line[1]);
			arr.add(temp);
			line = dio.readText().split(",");
			row++;
		}
		dio.write("Upload complete.\n");
		return arr;
	}

	public ArrayList<Long[]> reportsListToTimeStepsArr(List<AnomalyReport> AnomalyReportList)
	{
		int start = 0, i;
		int end = 0;
		int index =0;
		ArrayList<Long[]> reports = new ArrayList<Long[]>();
		for (i=0; i < AnomalyReportList.size(); i++)
		{
			start = i;
			end = i;
			while((i < AnomalyReportList.size()-1)&&
					(AnomalyReportList.get(end).description.equals(AnomalyReportList.get(end + 1).description)  )&&
					(AnomalyReportList.get(end).timeStep + 1 == (AnomalyReportList.get(end + 1).timeStep ))) {
				end++;
				i++;
			}
			Long[] temp = new Long[2];
			temp[0]=AnomalyReportList.get(start).timeStep;
			temp[1]=AnomalyReportList.get(end).timeStep;
			reports.add(index, temp);
			index++;
		}
		return reports;
	}
	// Execution of commands
	// Command number 1
	public class UploadCsv extends Command{

		public UploadCsv() {
			super("1. upload a time series csv file\n");
		}

		@Override
		public void execute() {
			// Train file
			dio.write("Please upload your local train CSV file.\n");
			uploadFile(sharedState.train_CSV_file);
			//dio.write("Upload complete.\n");
			sharedState.trainTimeSeries = new TimeSeries(sharedState.train_CSV_file); // Need to check if its ok

			// Test file
			dio.write("Please upload your local test CSV file.\n");
			uploadFile(sharedState.test_CSV_file);
			//dio.write("Upload complete.\n");
			sharedState.testTimeSeries = new TimeSeries(sharedState.test_CSV_file); // Need to check if its ok
		}		
	}

	// Command number 2
	public class AlgoSet extends Command {

		public AlgoSet() { super("2. algorithm settings\n"); }

		@Override
		public void execute() {

			float newVal;
			String temp;
			dio.write("The current correlation threshold is " + sharedState.simpleAD.GetThreshold() + "\nType a new threshold\n");
			temp = dio.readText();
			newVal = Float.parseFloat(temp);
			while((newVal < 0)|| (newVal > 1))
			{
				dio.write("please choose a value between 0 and 1." + "\nType a new threshold\n");
				temp = dio.readText();
				newVal = Float.parseFloat(temp);
			}
			sharedState.simpleAD.SetThreshold(newVal);
		}
	}

	// Command number 3
	public class DetectAnomalies extends Command{

		public DetectAnomalies() {
			super("3. detect anomalies\n");
		}

		@Override
		public void execute() {
			sharedState.simpleAD.learnNormal(sharedState.trainTimeSeries);
			sharedState.AnomalyReportList = sharedState.simpleAD.detect(sharedState.testTimeSeries);
			dio.write("anomaly detection complete.\n");
		}
	}

	// Command number 4
	public class DisplayResult extends Command{

		public DisplayResult() {
			super("4. display results\n");
		}

		@Override
		public void execute() {
			for (int i=0; i < sharedState.AnomalyReportList.size(); i++)
				dio.write(sharedState.AnomalyReportList.get(i).timeStep + "\t " + sharedState.AnomalyReportList.get(i).description+"\n");
			dio.write("Done.\n");
		}
	}

	// Command number 5
	public class UploadAndAnalyzeResult extends Command{

		public UploadAndAnalyzeResult() {
			super("5. upload anomalies and analyze results\n");
		}

		@Override
		public void execute() {
			//String localAnomalies_CSV_file = "anomalyTrain.csv";
			dio.write("Please upload your local anomalies file.\n");
			//dio.uploadFile(localAnomalies_CSV_file);
			ArrayList<Long[]> exceptions = readNumbers();
			//dio.write("Upload complete.\n");
			ArrayList<Long[]> reports = reportsListToTimeStepsArr(sharedState.AnomalyReportList);
			double True_Positive_rate,False_Positive_rate;
			double TruePositive = 0, FalsePositive= 0, p= 0 , totalTime= 0, FalseNegative = 0, N = 0;
			int flag;
			p = exceptions.size();
			totalTime = sharedState.testTimeSeries.dataTable.get(0).valuesList.size();
			N = totalTime;
			for(int i=0; i < exceptions.size(); i++)
			{
				flag = 0;
				for(int j=0; j< reports.size(); j++) {
					if (((reports.get(j)[0] <= exceptions.get(i)[0]) && ( exceptions.get(i)[0] <= reports.get(j)[1])) ||
							((reports.get(j)[0] <= exceptions.get(i)[1]) && (exceptions.get(i)[1] <= reports.get(j)[1])) ||
							((exceptions.get(i)[0] <= reports.get(j)[0]) && ( reports.get(j)[0]  <=exceptions.get(i)[1])) ||
							((exceptions.get(i)[0] <= reports.get(j)[1]) && (reports.get(j)[1] <= exceptions.get(i)[1]))) {
						TruePositive++;
						flag = 1;
					}
				}
				// An unreported exception was found
				if (flag == 0)
					FalseNegative++;
				N = N - (exceptions.get(i)[1] - exceptions.get(i)[0] + 1);
			}
			// False alerts
			FalsePositive = reports.size() - TruePositive;

			True_Positive_rate = (TruePositive/ p);
			False_Positive_rate =(FalsePositive / N);
			dio.write("True Positive Rate: " +(float)(Math.floor(True_Positive_rate*1000)/1000) +"\n");
			dio.write("False Positive Rate: "+(float)(Math.floor(False_Positive_rate*1000)/1000)  +"\n");
		}
	}
	// Command number 6
	public class Exit extends Command{

		public Exit() {
			super("6. exit\n");
		}

		@Override
		public void execute() {
			dio.close();
		}
	}
	
}
