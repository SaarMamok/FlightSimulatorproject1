package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TimeSeries {
	public List<FeaturesData> getDataTable() {
		return dataTable;
	}

	public TimeSeries() {
	}

	public static class FeaturesData{
		public String getFeatureName() {
			return featureName;
		}

		public List<Float> getValuesList() {
			return valuesList;
		}

		public String featureName;
		public List<Float> valuesList;
		public FeaturesData(String name){
			this.featureName = name;
			this.valuesList = new ArrayList<Float>();
		}

	}
	List<FeaturesData> dataTable = new ArrayList<>();

	public void RowToCol ( String[] s)
	{
		for(int i=0; i<this.dataTable.size(); i++)
		{
			this.dataTable.get(i).valuesList.add(Float.parseFloat(s[i]));
		}
	}
	public TimeSeries(FeaturesData f1,FeaturesData f2){
		this.dataTable.add(f1);
		this.dataTable.add(f2);
	}
	public TimeSeries(FeaturesData f1){
		this.dataTable.add(f1);
	}
	public TimeSeries(String csvFileName)
	{
		try
		{
			//parsing a CSV file into BufferedReader class constructor
			String line =  null;
			BufferedReader br = new BufferedReader(new FileReader(csvFileName));
			if((line = br.readLine()) != null){
				String[] s = line.split(","); // First line - Names of cols
				for(int i = 0; i<s.length; i++)
				{
					FeaturesData tempfeature = new FeaturesData(s[i]);
					this.dataTable.add(tempfeature);
				}

			}
			while ((line = br.readLine()) != null)   //run until the end of the file
			{
				String[] currentLine = line.split(",");
				RowToCol(currentLine);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();

		}
	}

}


