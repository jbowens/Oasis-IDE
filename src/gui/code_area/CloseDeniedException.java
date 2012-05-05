package camel.gui.code_area;

/**
 * Thrown when a close command is sent to a tab or window, but, for whatever 
 * reason (user choice, whatever) it's unable to close.
 */
public class CloseDeniedException extends Exception { }