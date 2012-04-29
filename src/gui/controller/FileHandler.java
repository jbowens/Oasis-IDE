package camel.gui.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;

import camel.gui.code_area.*;
import camel.gui.main.FileTree;
import camel.gui.menus.FileMenu;

public class FileHandler {
	
	protected JFileChooser _fc;
	protected FileTree ft;
	protected BufferedReader br;
	protected BufferedWriter bw;
	protected File f;
	protected CodeArea ca;
	
	public FileHandler(CodeArea ca)
	{
		_fc = new JFileChooser();
		this.ca = ca;
	}

	public void openFile() 
	{
		int val = _fc.showOpenDialog(null);
		if(val == JFileChooser.APPROVE_OPTION)
		{
			f = _fc.getSelectedFile();
			String path = f.getAbsolutePath();
			try {
				br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(path))));
				ca.makeTab(this, f);
			} catch (FileNotFoundException e) {
				System.err.println("Couldn't open file");
			} catch (IOException e) {
				System.err.println("Error reading from file");
			}
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

	public String getName()
	{
		return f.getName();
	}

	public File getFile()
	{
		return f;
	}
	
	public void saveFile()
	{
		System.out.println("here");
		Tab curTab = ca.getCurTab();
		try {
			bw = new BufferedWriter(new FileWriter(curTab.getPath()));
			bw.write(curTab.getText());
			bw.flush();
		} catch (IOException e) {
			System.err.println("Error writing to file");
		}
	}
}


