package ptt.dalek.test;

import static org.junit.Assert.assertEquals;
import static ptt.dalek.main.Parsing.parse;

import org.junit.Test;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import ptt.dalek.antlr.LuaParser;
import ptt.dalek.main.RenameVisitor;

public class RenameVisitorTest {

		/* Rename visitor test case
		 * 
		 * test() parses example2.lua
		 * 	      renames and prints to testRenamed.lua
		 *        parses testRenamed.lua and exampleRenamed.lua 
		 *        compares resulting parse trees (text based)
		 *        
		 */
	
    private static String example =
        "inputs"
        + File.separator
        + "example2.lua";
    
    private static String renamed =
            "inputs"
            + File.separator
            + "exampleRenamed.lua";
    
    private static String testRenamed =
            "outputs"
            + File.separator
            + "testRenamed.lua";
    
    @Test
    public void test() 
    		throws RecognitionException, IOException  {
    	
    		// generate parse tree from example2.lua
		LuaParser parser;
		parser = parse(example);
		parser.setBuildParseTree(true);
		ParseTree tree = parser.chunk();
		
			// rename and print to testRenamed.lua
		PrintStream out;
		out = new PrintStream(testRenamed);
		RenameVisitor p = new RenameVisitor(out);
			p.addName("exampleFunction", "changedFunctionName");
			p.addName("param1", "changedParam1");
				// note: "1.21" is not valid (no valid Lua name) and should not show up
			p.addName("param2", "1_21");
				// note: "then" is not valid (blacklisted lua syntax token) and should not show up
			p.addName("param3", "then");
		p.visit(tree);
		out.close();

			// parse (renamed) result file
		parser = parse(renamed);
		ParseTree tree2 = parser.chunk();
			// parse test case file
		parser = parse(testRenamed);
		ParseTree tree3 = parser.chunk();
		
			// assert equality for both resulting trees (as text)
		assertEquals(tree2.getText(), tree3.getText());
    }
}