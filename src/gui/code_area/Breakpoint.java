package camel.gui.code_area;


public class Breakpoint {
	
	private int lineNum;
	private int breakpointHandle;

	public Breakpoint(int lineNum, int breakpointHandle) {
		this.lineNum = lineNum;
		this.breakpointHandle = breakpointHandle;
	}

	public int getLineNum() {
		return lineNum;
	}

	public int getBreakpointHandle() {
		return breakpointHandle;
	}

}
