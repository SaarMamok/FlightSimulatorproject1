package test;

import test.Commands.Command;
import test.Commands.DefaultIO;

import java.util.ArrayList;

public class CLI {

	ArrayList<Command> commands;
	DefaultIO dio;
	Commands c;
	
	public CLI(DefaultIO dio) {
					/*dio.CreateFile();
			FileIO fio=new FileIO("input.txt", "output.txt");
			this.dio = fio;*/
		this.dio=dio;
		c=new Commands(dio);
		commands=new ArrayList<>();
		commands.add(c.new UploadCsv());
		commands.add(c.new AlgoSet());
		commands.add(c.new DetectAnomalies());
		commands.add(c.new DisplayResult());
		commands.add(c.new UploadAndAnalyzeResult());
		commands.add(c.new Exit());


	}
	/*public void CreateFile(String  outputFileNam)  {
			try {
				File myObj = new File(outputFileNam);
				if (myObj.createNewFile()) {
					//System.out.println("File created: " + myObj.getName());
				} else {
					System.out.println("File already exists.");
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}*/
	public void menu(){
		dio.write("Welcome to the Anomaly Detection Server.\nPlease choose an option:\n");
		for(int i=0; i< commands.size(); i++)
			dio.write(commands.get(i).description);
	}
	public void start() {
		int flag = 1;
		String selectedCommand_String;
		int selectedCommand = 0;
		while(flag == 1)
		{
			// Print menu
			menu();
			selectedCommand_String = dio.readText();
			selectedCommand = Integer.parseInt(selectedCommand_String) - 1;
			if (selectedCommand == 5)
				flag = 0;
			commands.get((int)selectedCommand).execute();
		}

	}
}
