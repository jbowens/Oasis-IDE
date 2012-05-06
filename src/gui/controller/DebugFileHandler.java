package camel.gui.controller;

import camel.gui.code_area.*;
import java.io.*;

public class DebugFileHandler extends FileHandler {

	public DebugFileHandler(CodeArea ca) {
		super(ca);
	}

	/**
	 * Initialize bufferedreader with appropriate file
	 * 
	 * @param path - path to file
	 */ 
	public void setBuffRead(String path) {
		try {
			super.br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(path))));
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file to debug. :(");
		}
	}
}
