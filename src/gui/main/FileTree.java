package camel.gui.main;

import camel.gui.controller.FileHandler;
import camel.util.DirectoryFileFilter;
import camel.util.HiddenFileFilter;
import camel.util.MlFileFilter;
import camel.util.TextFileFilter;
import camel.util.UnionFileFilter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileFilter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class FileTree extends JPanel {

	protected File root;
	protected JScrollPane sp;
	
    /**
	 * FileTree Constructor
     *
     * @param fh  a FileHandler
	 */
	public FileTree(FileHandler fh)
	{
        // Set the root to be the user home directory
		root = PrintableFile.createFromFile( new File(System.getProperty( "user.home" ) ));

		final FileHandler fHandler = fh;
		FileTreeModel model = new FileTreeModel(root);
		final JTree tree = new JTree();
		tree.setModel(model);
		
		int startRow = 1;
		String prefix = "";
		File node;
		TreePath path;
		TreeSelectionListener tsl = new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e)
			{
                File selectedFile = (File) tree.getLastSelectedPathComponent();
                if( selectedFile != null && ! selectedFile.isDirectory() )
                    fHandler.openFile(selectedFile, false);
		    }
		};
		tree.addTreeSelectionListener(tsl);
		sp = new JScrollPane(tree);
	}
}


class FileTreeModel implements TreeModel
{
	protected FileFilter fileFilter;
	protected File root;
	
    /**
     * Constructs a new FileTreeModel with the given file as the root.
     */
    public FileTreeModel(File r)
	{
		root = r;

        // Setup the filter
        UnionFileFilter filter = new UnionFileFilter();
        filter.unionFilter( new TextFileFilter() );
        filter.unionFilter( new MlFileFilter() );
        filter.unionFilter( new DirectoryFileFilter() );
        fileFilter = new HiddenFileFilter( filter );
	}

	/**
	 * Gets a specific file from the tree
	 */
	public Object getChild(Object parent, int index) {
		
        File[] children = ((File) parent).listFiles( fileFilter );

		if ((children == null) || (index >= children.length))
        { 
			return null;
		}
		else
        {
			return PrintableFile.createFromFile(children[index]);
        }
	}

	/**
	 * Tells the FTModel how many children it has
	 */
	public int getChildCount(Object parent) {
		File fileSysEntity = (File)parent;
        if ( fileSysEntity.isDirectory() ) {
            File[] children = fileSysEntity.listFiles(fileFilter);
            return children.length;
        }
        else {
            return 0;
        }
	}

	/*
	 * Gets the index of a given child
	 */
	public int getIndexOfChild(Object parent, Object child) {
		 
        File directory = (File) parent;
        File fileSysEntity = (File) child;
        File[] children = directory.listFiles( fileFilter );

        int result = -1;

        for ( int i = 0; i < children.length; i++ ) {
            if ( fileSysEntity.equals( children[i] ) )  {
                result = i;
                break;
            }
        }

        return result;
	}

	/**
	 * Returns the root of the tree
	 */
	public Object getRoot() {
		return root;
	}

	/**
	 * 	Returns true if node is a file
	 */
	public boolean isLeaf(Object node) {
		return ((File)node).isFile();
	}

	public void removeTreeModelListener(TreeModelListener l) {
		// do nothing
	}
	public void addTreeModelListener(TreeModelListener l) {
		// do nothing
	}
	public void valueForPathChanged(TreePath path, Object newValue) {
		// do nothing
	}

}
