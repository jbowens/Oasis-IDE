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
	protected File directory;
	
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
			if( f != null )	{
				// Save the default directory
				directory = f.getParentFile();

				// Get the absolute path so we can open
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

	/**
	 * Save the given tab/file to its current location on the disk.
	 */
	public void saveFile(Tab tabToSave) {

		// Can't save the tab if there is no tab!
		if( tabToSave == null ) {
			return;
		}

		// Make sure this file already has a path
		if( tabToSave.getPath() == null ) {
			// This file hasn't been saved yet, so do a save-as
			saveAs(tabToSave);
			return;
		}

		try {
			//Save the current directory
			directory = new File(tabToSave.getPath()).getParentFile();

			// Save the file
			bw = new BufferedWriter(new FileWriter(tabToSave.getPath()));
			bw.write(tabToSave.getText());
			bw.flush();
			bw.close();
			tabToSave.justSaved();
		} catch (IOException e) {
			System.err.println("Error writing to file");
		}
	}
	
	/**
	 * Save the current file/tab to its current location on the disk.
	 */
	public void saveFile()
	{
		Tab curTab = ca.getCurTab();
		saveFile(curTab);
	}

	/**
	 * Prompts the user for a new file location and then saves the given file/tab
	 * that file location.
	 *
	 * @param tabToSave the tab that should be saved.
	 */
	public void saveAs(Tab tabToSave) {

		// Can't save a nonexistent tab
		if( tabToSave == null )
			return;

		// Set the default save location
		if( directory != null )
			_fc.setSelectedFile(new File(directory.getAbsolutePath() + File.separator + "untitled.ml"));
		else
			_fc.setSelectedFile(new File("untitled.ml"));

		if( _fc.showSaveDialog(ca) == JFileChooser.APPROVE_OPTION ) {

			// Set this new file location to be the tab's file loc
			tabToSave.setFileLocation( _fc.getSelectedFile() );
			// Do a regular save
			saveFile(tabToSave);

		}
	}

	/**
	 * Prompt the user for a new file location and saves the currently open
	 * file/tab there.
	 */
	public void saveAs() {
		Tab currTab = ca.getCurTab();
		saveAs(currTab);
	}
}


