package camel.gui.code_area;

/**
 * Represents a breakpoint source.
 */
public interface BreakpointSource {
	
	/**
	 * Determines whether there is a breakpoint on the given line.
	 *
	 * @param line the line to put the breakpoint on.
	 */
	public boolean breakpointOnLine(int line);

}