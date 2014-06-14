package ptt.dalek.main.example;

import static ptt.dalek.main.Parsing.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import ptt.dalek.antlr.LuaParser;
import ptt.dalek.main.RenameVisitor;

public class RenameVisitorExample {

	   /*
	    * This is just an example, not the actual test case.
	    * Test cases are hiding in ptt.dalek.test
	    * 
	    */
	
   private static String example =
            "inputs"
            + File.separator
            + "example2.lua";
   
   private static String output =
            "outputs"
            + File.separator
            + "renamed.lua";
	
	public static void main(String args[]) {

		try {
			LuaParser parser;
			
			parser = parse(example);
			parser.setBuildParseTree(true);
			ParseTree tree = parser.chunk();
			
			try {
				PrintStream out;
				out = new PrintStream(output);
				RenameVisitor p = new RenameVisitor(out);
				
				p.addName("exampleFunction", "changedFunctionName");
				p.addName("param1", "changedParam1");
					// note: "1.21" is not valid (no valid Lua name) and should not show up
				p.addName("param2", "1.21");
					// note: "then" is not valid (blacklisted lua syntax token) and should not show up
				p.addName("param3", "then");
				
				p.visit(tree);
				out.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("-- EXCEPTION: FILE NOT FOUND!");
				e.printStackTrace();
				
			}
		} catch (ParseCancellationException | IOException e1) {
			System.out.println("-- ERROR PARSING!");
			e1.printStackTrace();
			
		}
	}
}
