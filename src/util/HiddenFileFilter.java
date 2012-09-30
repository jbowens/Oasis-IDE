package camel.util;

import java.io.File;
import java.io.FileFilter;

/**
 * A filter that accepts everything except for hidden files.
 *
 * @author jbowens
 * @since September 2012
 */
public class HiddenFileFilter implements FileFilter
{

    protected FileFilter filter;

    public HiddenFileFilter( FileFilter acceptingFilter )
    {
        filter = acceptingFilter;
    }

    /**
     * Sets a filter that defines which non-hidden files this filter should accept.
     *
     * @param acceptingFilter  the accepting file filter to use
     */
    public void setAcceptFilter( FileFilter acceptingFilter )
    {
        filter = acceptingFilter;
    }

    public boolean accept(File pathname)
    {
        if( pathname.isHidden() )
            return false;

        if( filter != null )
            return filter.accept(pathname);
        else
            return true;
    }

}
