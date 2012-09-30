package camel.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * A file filter that is composed of several other file filters. If any of its file
 * filters accept the given file, this filter will accept that file.
 *
 * @author jbowens
 * @since September 2012
 */
public class UnionFileFilter implements FileFilter
{

    // The filters this filter unions to determine accepted files
    protected List<FileFilter> filtersToUnion;

    public UnionFileFilter()
    {
        filtersToUnion = new ArrayList<FileFilter>();
    }

    /**
     * Unions a filter with the current set of accepted files.
     *
     * @param filter  the filter to union
     */
    public void unionFilter( FileFilter filter )
    {
        filtersToUnion.add( filter );
    }

    /**
     * Removes a filter from the set of filter used to determine
     * acceptance of files.
     *
     * @param fitler  the filter to remove
     */
    public void removeFilter( FileFilter filter )
    {
        filtersToUnion.remove( filter );
    }

    /**
     * Determines if the given file is in the union of this filter's composed
     * file filters.
     *
     * @see FileFilter.accept
     */
    public boolean accept(File pathname)
    {
        boolean inUnion = false;
        for( FileFilter filter : filtersToUnion )
        {
            inUnion = inUnion || filter.accept(pathname);
        }
        return inUnion;
    }

}
