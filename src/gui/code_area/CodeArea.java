package camel.gui.code_area;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import camel.gui.controller.FileHandler;
import camel.gui.menus.MenuBar;
import camel.gui.main.MainWindow;
import camel.syntaxhighlighter.StyleSet;
import camel.syntaxhighlighter.SimpleStyleSet;
import camel.syntaxhighlighter.StyleWrapper;
import camel.Application;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.TabbedPaneUI;

import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class CodeArea extends JPanel {

	protected Color UNSAVED_ICON_COLOR = new Color(161, 80, 80);
	protected Color CLOSE_TAB_COLOR = UNSAVED_ICON_COLOR;

	/* The tabbed pane that holds all the existing tabs */
	protected JTabbedPane tabs;

	/* The tab lists */
	protected ArrayList<Tab> tabList;

	/* The current style set being used */
	protected StyleWrapper style;

	/* The window this code area is tied to */
	protected MainWindow mainWindow;
	
	/* The application this Code Area is tied to */
	protected Application app;

	/* Default file handler for this code area */
	protected FileHandler fh;

	/**
	 * Creates a new code area.
	 *
	 * @param app the application instance the code area belongs to
	 * @param mainWindow the main window the code area belongs to
	 */
	public CodeArea(Application app, MainWindow mainWindow) {
		super(new GridBagLayout());

		this.app = app;
		this.mainWindow = mainWindow;
		this.fh = new FileHandler(this);

		tabList = new ArrayList<Tab>();
		tabs = new JTabbedPane();
		tabs.addMouseListener(new MouseAdapter() {
			int prev_position;

			public void mousePressed(MouseEvent e)
			{
      			prev_position = tabs.getUI().tabForCoordinate(tabs, e.getX(), e.getY());
      		}

      		public void mouseReleased(MouseEvent e)
      		{
      			int new_position = tabs.getUI().tabForCoordinate(tabs, e.getX(), e.getY());
      			if(new_position >= 0 && (new_position != prev_position))
      			{
	      			Tab t = (Tab)tabs.getComponentAt(prev_position);
	           		String title = tabs.getTitleAt(prev_position);
	            	tabs.removeTabAt(prev_position);
	            	tabs.insertTab(title, null, t, null, new_position);
	            	tabs.setTabComponentAt(tabs.indexOfComponent(t), new TabTitle(t,title));
            	}
      		}
   		 });


		GridBagConstraints fullFill = new GridBagConstraints();
		fullFill.weighty = fullFill.weightx = 1.0; 
		fullFill.fill = GridBagConstraints.BOTH; 
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		add(tabs,fullFill);
		setSize(600,600);

		/* Load the user's preferred default style if it's available */
		StyleSet initialStyle = new SimpleStyleSet();

		String configStyle = app.getConfig().getSetting("styleset");

		for( StyleSet s : app.getStyleLoader().getAvailableStyles() ) {
			if( s.getName() != null && s.getName().equals(configStyle) ) {
				initialStyle = s;
				break;
			}
		}

		this.style = new StyleWrapper( initialStyle );

	}

	/**
	 * Returns this code area's corresponding application
	 */
	public Application getApplication() {
		return app;
	}

	/**
	 * Returns the window associated with this code area.
	 *
	 * @return the code area's parent window
	 */
	public MainWindow getWindow() {
		return mainWindow;
	}

	/**
	 * Switch the style.
	 */
	public void switchStyle(StyleSet newStyle) {
		style.setStyle(newStyle);
		this.repaint();
		if( newStyle != null && newStyle.getName() != null )
			app.getConfig().setSetting("styleset", newStyle.getName());
	}

	/**
	 * Make a new tab not yet associated with any file.
	 *
	 * @param fh - the associated FileHandler
	 */
	public void makeTab(FileHandler fh) {
		Tab t = new Tab(this, fh, style);
		tabs.addTab("Untitled", t);
		tabs.setSelectedComponent(t);
		tabs.setTabComponentAt(tabs.indexOfComponent(t), new TabTitle(t,"Untitled"));
		tabList.add(t);
	}

	/**
	 * Make a tab from an already given filename.
	 *
	 * @param fh - the associated FileHandler
	 * @param filename - the file path of the file to open
	 */
	public void makeTab(FileHandler fh, String filename) {
		Tab t = new Tab(this, new File(filename), fh, style);
		tabs.addTab(fh.getName(), t);
		tabs.setSelectedComponent(t);
		tabs.setTabComponentAt(tabs.indexOfComponent(t), new TabTitle(t,fh.getName()));
		tabList.add(t);
	}

	/**
	 * Makes a tab from an already given File object.
	 *
	 * @param fh - the associated FileHandler
	 * @param file - the file to create the tab from
	 */
	public void makeTab(FileHandler fh, File f) {
		Tab t = new Tab(this, f, fh, style);
		tabs.addTab(fh.getName(), t);
		tabs.setSelectedComponent(t);
		tabs.setTabComponentAt(tabs.indexOfComponent(t), new TabTitle(t,fh.getName()));
		tabList.add(t);
	}

	/**
	 * Makes a debug tab from an already given file object.
	 */
	public void makeDebugTab(FileHandler fh, File f) {
		DebugTab t = new DebugTab(this, f, fh, style);
		t.setDebug();
		tabs.addTab("DEBUG -"+ fh.getName(), t);
		tabs.setSelectedComponent(t);
		tabs.setTabComponentAt(tabs.indexOfComponent(t), new TabTitle(t,"DEBUG -"+ fh.getName()));
		tabList.add(t);
	}

	/**
	 * Makes a new tab from a user-inputted file.
	 * 
	 * Note: Prompts for user input of the file.
	 *
	 * @param fh - the associated FileHandler
	 */
	public void makeTabFromFile(FileHandler fh) {
		Tab t = new Tab(this, fh.getFile(), fh, style);
		tabs.addTab(fh.getName(), t);
		tabs.setSelectedComponent(t);
		tabs.setTabComponentAt(tabs.indexOfComponent(t), new TabTitle(t,fh.getName()));
		tabList.add(t);
	}

	/**
	 * Gets the tab that currently has focus.
	 *
	 * @return the current tab
	 */
	public Tab getCurTab() {
		return (Tab) tabs.getSelectedComponent();
	}

	public void updateDisplayPreferences() {
		for( Tab t : tabList )
			t.updateDisplayPreferences();
		repaint();
	}

	/**
	 * Gets the current font used by the code area.
	 */
	public Font getFont() {
		if( getWindow() == null )
			return super.getFont();
		else
			return getWindow().getFont();
	}

	/**
	 * Saves all the code area's tabs
	 */
	public void saveAll() {
		for( Tab t : tabList ) {
			if( tabs.indexOfComponent(t) != -1 ) {
				setCurrentTab(t);
				fh.saveFile(t);
			}
		}
	}

	/**
	 * Runs the current file.
	 */
	public void runCurrentFile() {
		Tab current = getCurTab();
		if( current == null ) {
			getWindow().displayErrorMessage("There is no open file to run.");
			return;
		}
		
		current.run();
	}

	/**
	 * Opens debug tab for current file.
	 */

	public void debugCurrentFile() {
		Tab current = getCurTab();
		if( current == null ) {
			getWindow().displayErrorMessage("There is no open file to run.");
			return;
		}
		/*Can't debug a debug tab!*/
		if (!current.checkDebug()) {
			fh.openFile(new File(current.getPath()));
		}
	}


	/**
	 * Resets the current tab's interactions window.
	 */
	public void resetInteractions() {
		Tab current = getCurTab();
		if( current == null ) {
			return;
		}
		current.resetInteractions();
	}

	/**
	 * Sets the given tab's title to the given title.
	 *
	 * @param t the tab whose title should be updated
	 * @param title the new title for the tab
	 */
	public void setTabTitle(Tab t, String title) {
		int index = tabs.indexOfComponent(t);
		if( index == -1 )
			return;
		tabs.setTitleAt(index, title);
	}

	/**
	 * Sets the given tab as the current tab (switches focus to the
	 * given tab)
	 *
	 * @param tabToFocus the tab that should now the current tab
	 */
	public void setCurrentTab(Tab newCurrent) {
		if( tabs.indexOfComponent(newCurrent) != -1 )
			tabs.setSelectedComponent(newCurrent);
	}

	/**
	 * Closes the currently open file/tab.
	 */
	public void closeCurrentTab() throws CloseDeniedException {
		if( getCurTab() == null )
			getWindow().displayErrorMessage("No tabs to close");
		closeTab(getCurTab());
	}

	/**
	 * Closes the given tab and prompts the user asking if he wishes to save
	 * if changes have been made since the last save.
	 *
	 * @param tabToClose the tab to close
	 *
	 * @throws CloseDeniedException when the user decides not to close the tab after all
	 */
	public void closeTab(Tab tabToClose) throws CloseDeniedException {

		// Make sure this tab actually exists.
		if( tabs.indexOfComponent(tabToClose) == -1 ) {
			// The tab doesn't actually exist. Remove it from the tab list if it's
			// still in there.
			if(tabList.contains(tabToClose))
				tabList.remove(tabToClose);
			return;
		}

		// Make sure the tab doesn't have any unsaved changes
		if( tabToClose.unsavedChanges() ) {
			if( tabToClose.getFile() == null ) {
				// This file has never been saved
				setCurrentTab(tabToClose);
				int userChoice = JOptionPane.showConfirmDialog(getWindow(),
															   "This tab has never been saved. Would you like to save it before closing it?",
															   "Unsaved Changes",
															   JOptionPane.YES_NO_CANCEL_OPTION,
															   JOptionPane.WARNING_MESSAGE);
				if( userChoice == JOptionPane.CANCEL_OPTION || userChoice == JOptionPane.CLOSED_OPTION )
					throw new CloseDeniedException();
				else if( userChoice == JOptionPane.YES_OPTION ) {
					fh.saveAs(tabToClose);
				}
			} else {
				// This file has been saved. Warn the user and save it before quitting if they want to
				int userChoice = JOptionPane.showConfirmDialog(getWindow(),
											  				   "Would you like to save changes to " + tabToClose.getFile().getName() + " before closing the tab?",
											  				   "Unsaved Changes",
											  				   JOptionPane.YES_NO_CANCEL_OPTION,
											  				   JOptionPane.WARNING_MESSAGE);
				if( userChoice == JOptionPane.CANCEL_OPTION || userChoice == JOptionPane.CLOSED_OPTION )
					throw new CloseDeniedException();
				else if( userChoice == JOptionPane.YES_OPTION ) {
					fh.saveFile(tabToClose);
				}
			} 

			removeTab(tabToClose);

		} else {
			// If no unsaved changes, just remove it
			removeTab(tabToClose);
		}
	}

	/**
	 * Removes the given tab from the code area. (Note: This does not
	 * prompt the user if there are unsaved changes. If you want to close
	 * a tab, you should use closeTab() which will call this method when
	 * it needs to.)
	 *
	 * @param tabToRemove the tab that should be removed
	 */
	protected void removeTab(Tab tabToRemove) {
		// Let the tab know that it should get its affairs in order
		tabToRemove.close();
		tabs.remove(tabToRemove);
	}

	/**
	 * Cleans up the code area before it's closed. This is usually called on
	 * a window closing event.
	 */
	public void close() throws CloseDeniedException {
		for(Tab t : tabList)
			closeTab(t);
		tabList.clear();
	}


	//This is code for a "close" button, it works but not sure how to add the button
	private class TabTitle extends JPanel
	{
		private JLabel title;
		private JButton closeButton;
		public TabTitle(Tab t,String name)
		{
			closeButton = new TabButton(t);
			title = new JLabel(name);
			add(title);
			add(closeButton);
			setBackground(new Color(0, 0, 0, 0));
			setBorder(BorderFactory.createEmptyBorder());
		}
	}
	private class TabButton extends JButton implements ActionListener
	{
		private int size = 15;
		protected Tab tabToClose;
		public TabButton(Tab t)
		{
			tabToClose = t;
			setPreferredSize(new Dimension(size,size));
			setToolTipText("Close Tab");
			setUI(new BasicButtonUI());
			setContentAreaFilled(false);
			setFocusable(false);
	        setBorder(BorderFactory.createEtchedBorder());
	        setBorderPainted(false);
	        addMouseListener(buttonMouseListener);
	        setRolloverEnabled(true);
	        addActionListener(this);
		}
		public void actionPerformed(ActionEvent e) 
		{
			try {
	        	closeTab(tabToClose);
	    	}
	    	catch(Exception e1){}
	    }
	    protected void paintComponent(Graphics g)
	    {
	    	g = setRenderingHints(g);

	    	if(tabToClose.changes == false || getModel().isRollover())
	    	{
		        super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g.create();
		        //shift the image for pressed buttons
		        if (getModel().isPressed()) {
		            g2.translate(1, 1);
		        }
		        
		        g2.setStroke(new BasicStroke(2));
		        g2.setColor(Color.BLACK);
		        if (getModel().isRollover()) {
                	g2.setColor(CLOSE_TAB_COLOR);
            	}
		        int delta = 3;
		        g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
		        g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
		        g2.dispose();
	    	}
	    	else
	    	{
	    		super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g.create();
		        //shift the image for pressed buttons
		        if (getModel().isPressed()) {
		            g2.translate(1, 1);
		        }
		        g2.setStroke(new BasicStroke(2));
		        g2.setColor(UNSAVED_ICON_COLOR);
		        g2.fillOval(3, 3, getWidth()-6, getHeight()-6);
		        g2.dispose();
	    	}
	    }

	    /**
	     * Sets rendering hints needed for drawing the tab icons.
	     *
	     * @param g the graphics context
	     */
	    protected Graphics2D setRenderingHints(Graphics g) {
	    	Graphics2D g2 = (Graphics2D) g;
	    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    						RenderingHints.VALUE_ANTIALIAS_ON);
	    	return g2;
	    }

	}
	 private final static MouseListener buttonMouseListener = new MouseAdapter() 
	 {
	    public void mouseEntered(MouseEvent e) {
	        Component component = e.getComponent();
	        if (component instanceof AbstractButton) {
	            AbstractButton button = (AbstractButton) component;
	            button.setBorderPainted(true);
	        }
	    }

	    public void mouseExited(MouseEvent e) {
	        Component component = e.getComponent();
	        if (component instanceof AbstractButton) {
	            AbstractButton button = (AbstractButton) component;
	            button.setBorderPainted(false);
	        }
	    }
	};

}
