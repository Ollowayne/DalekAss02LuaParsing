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
import ptt.dalek.main.PrintVisitor;

public class PrintVisitorTest {

		/* print visitor test case
		 * 
		 * test() parses example2.lua
		 * 	      prints to testPrettyPrinted.lua
		 *        parses testPrettyPrinted.lua and examplePrettyPrinted.lua 
		 *        compares resulting parse trees
		 *        
		 */
	
    private static String example =
        "inputs"
        + File.separator
        + "example2.lua";
    
    private static String renamed =
            "inputs"
            + File.separator
            + "examplePrettyPrinted.lua";
    
    private static String testRenamed =
            "outputs"
            + File.separator
            + "testPrettyPrinted.lua";
    
    @Test
    public void test() 
    		throws RecognitionException, IOException  {
    	
    		// generate parse tree from example2.lua
		LuaParser parser;
		parser = parse(example);
		parser.setBuildParseTree(true);
		ParseTree tree = parser.chunk();
		
		PrintStream out;
		out = new PrintStream(testRenamed);
		PrintVisitor p = new PrintVisitor(out);
		p.visit(tree);
		out.close();

			// parse (example) result file
		parser = parse(renamed);
		ParseTree tree2 = parser.chunk();
			// parse test case file
		parser = parse(testRenamed);
		ParseTree tree3 = parser.chunk();
		
			// test equality for both resulting trees (as String)
		assertEquals(tree2.getText(), tree3.getText());
    }
}