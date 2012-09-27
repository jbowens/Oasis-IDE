package camel.gui.controller;

import camel.gui.code_area.*;
import java.io.*;

public class DebugFileHandler {

	protected BufferedReader br;
	protected File f;
	protected CodeArea dca;

	public DebugFileHandler(CodeArea dca) {
		this.dca = dca;	
	}

	/**
	 * Initialize bufferedreader with appropriate file
	 * 
	 * @param path - path to file
	 */ 
	public void setBuffRead(String path) {
		try {
			this.br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(path))));
		} catch (FileNotFoundException e) {
			System.err.println("Could not open file to debug. :(");
		}
	}
	
	public String nextLine() 
	{
		try {
			if(br.ready())
			{
				return br.readLine();
			}
			else
			{
				return null;
			}
		} catch (IOException e) {
			System.err.println("Error, bufferedreader was not initialized");
		}
		return null;
	}

	public void closeReader() {
		if (br != null) {
			try{
				br.close();
			} catch(Exception e) {
				//eat it
			}
		}
	}

}

