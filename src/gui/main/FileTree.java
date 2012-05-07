package camel.gui.main;
import camel.gui.controller.FileHandler;
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
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class FileTree extends JPanel {
	protected File root;
	protected JScrollPane sp;
	/*
	* FileTree Constructor
	*/
	public FileTree(FileHandler fh)
	{
		root = new File(".");
		final FileHandler fHandler = fh;
		FileTreeModel model = new FileTreeModel(root);
		final JTree tree = new JTree();
		tree.setModel(model);
		
		int startRow = 1;
		String prefix = ".";
		File node;
		TreePath path;
		MouseListener ml = new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if(selPath != null)
				{
					String sPath = selPath.toString();
					//regex to parse path from selpath
					Pattern p = Pattern.compile("\\[(.*),(.*?)\\]");
					Matcher m = p.matcher(sPath);
					if(m.matches())
					{
						//regex to check for .ml files
						String path = m.group(2);
						Pattern p2 = Pattern.compile("(.*?)\\.ml");
						Matcher m2 = p2.matcher(path);
						if(selRow != -1 && m2.matches() && e.getClickCount() == 2)
						{
							//need regex to parse path from treepath
							File f = new File(path);
							String abs = f.getAbsolutePath().toString();
							Pattern p3 = Pattern.compile("(.*)\\s\\./(.*)");
							Matcher m3 = p3.matcher(abs);
							if(m3.matches())
								fHandler.openFTFile(m3.group(1)+m3.group(2));
						}
					}
				}
			}
		};
		tree.addMouseListener(ml);
		sp = new JScrollPane(tree);
	}
}


class FileTreeModel implements TreeModel
{
	private FileFilter fileFilter = new HiddenFileFilter();
	protected File root;
	public FileTreeModel(File r)
	{
		root = r;
	}
	/*
	 * Gets a specific file from the tree
	 */
	public Object getChild(Object parent, int index) {
		String[] children = ((File)parent).list();
		if ((children == null) || (index >= children.length)){ 
			return null;
		}
		else if(children[index].startsWith("."))
		{
			return new File("");
		}
		
		else
			return new File((File) parent, children[index]);
	}

	/*
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
		 File directory = (File)parent;
         File fileSysEntity = (File)child;
         File[] children = directory.listFiles(fileFilter);
         int result = -1;

         for ( int i = 0; i < children.length; ++i ) {
             if ( fileSysEntity.equals( children[i] ) ) {
                 result = i;
                 break;
             }
         }

         return result;

	}

	/*
	 * Returns the root of the tree
	 */
	public Object getRoot() {
		return root;
	}
	/*
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
	private class HiddenFileFilter implements FileFilter {

		public boolean accept(File pathname) {
			return !pathname.isHidden();
		}
	}

}