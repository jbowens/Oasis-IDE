package camel.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;

import camel.gui.controller.FileHandler;
import camel.gui.code_area.CodeArea;
import camel.syntaxhighlighter.StyleLoader;
import camel.syntaxhighlighter.StyleSet;
import camel.Config;

/**
 * The view menu.
 */
public class ViewMenu extends JMenu implements ActionListener {

	/* The menu bar this menu is a part of */
	protected MenuBar parentBar;

	protected JMenu _styles;

	protected JMenu _font;

	protected JCheckBoxMenuItem _lineNumbers;

	protected CodeArea _codeArea;

	protected StyleLoader _styleLoader;

	protected Config _config;

	/**
	 * Create a new menu bar
	 *
	 * @param parentBar the parent menubar of this menu
	 */
	public ViewMenu(MenuBar parentBar, Config config, StyleLoader styleLoader, CodeArea codeArea) {
		
		super("View");

		this.parentBar = parentBar;
		_codeArea = codeArea;
		_config = config;
		_styleLoader = styleLoader;

		setMnemonic('V');

		_styles = new JMenu("Code Styles");

		List<StyleSet> styles = styleLoader.getAvailableStyles();

		for( StyleSet s : styles ) {
			JMenuItem menuItem = new JMenuItem(s.getName());
			_styles.add(menuItem);
			menuItem.addActionListener(new StyleSwitchListener(codeArea, s));
		}

		_font = new JMenu("Font");

		boolean lineNums = ! config.settingExists("linenumbers") || config.getSetting("linenumbers").equals("true");

		_lineNumbers = new JCheckBoxMenuItem("Line Numbers", lineNums);

		add(_styles);
		add(_font);
		add(_lineNumbers);

		_lineNumbers.addActionListener(this);

	}

	protected class StyleSwitchListener implements ActionListener {
		
		protected CodeArea codeArea;
		protected StyleSet style;

		public StyleSwitchListener(CodeArea ca, StyleSet s) {
			codeArea = ca;
			style = s;
		}

		/* Switch styles */
		public void actionPerformed(ActionEvent e) {
			codeArea.switchStyle(style);
		}

	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == _lineNumbers ) {
			if( _lineNumbers.getState() )
				_config.setSetting("linenumbers", "true");
			else
				_config.setSetting("linenumbers", "false");
			_codeArea.updateDisplayPreferences();
		}
	}

}
