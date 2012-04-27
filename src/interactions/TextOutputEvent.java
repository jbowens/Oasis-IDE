package camel.interactions;

/*
 * An event that is sent to TextOutputListeners when text is outputted.
 */
public class TextOutputEvent {

    protected final int handle;
    protected final String text;

    public TextOutputEvent(String txt, int handle) {
	this.text = txt;
	this.handle = handle;
    }

    /* Get the outputted text */
    public String getText() {
	return text;
    }

    /* Get the handle */
    public int getHandle() {
	return handle;
    }

}
