import org.junit.*;
import java.util.*;
import org.junit.Assert.*;
import java.io.StringReader;

import camel.syntaxhighlighter.*;

public class CommentSeparatorTests {

    @Test
    public void testSimpleProgram() {
    	
        String simpleProg = "(* this is a comment! *)
let rec length(alist : 'a list) = match alist with
| [] -> 0
| _::tl -> 1 + length tl;;

length( [5; 4; 3; 2; 1] );;";

        StringReader reader = new StringReader(simpleProg);

        CommentSeparator separator = new CommentSeparator();

        List<TextBlock> results = separator.separateComments(separator);

        assert( results.size() == 3 );

    }

}
