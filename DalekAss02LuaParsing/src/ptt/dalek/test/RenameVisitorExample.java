package ptt.dalek.test;

import static ptt.dalek.main.Parsing.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import ptt.dalek.antlr.LuaParser;
import ptt.dalek.main.PrintVisitor;
import ptt.dalek.main.RenameVisitor;

public class RenameVisitorExample {
	
	/* Pretty printing example
	 * 
	 * parses example lua file and
	 * pretty prints to "outputs/prettyPrinted.lua", as well as system.out,
	 * using PrintVisitor in both cases.
	 */
	
   private static String example =
            "inputs"
            + File.separator
            + "example2.lua";
   
   private static String output =
            "outputs"
            + File.separator
            + "prettyPrinted.lua";
	
	public static void main(String args[]) {

		try {
			LuaParser parser;
			
			parser = parse(example);
			parser.setBuildParseTree(true);
			ParseTree tree = parser.chunk();
			
				// DEBUG prints tree.getText() to console
				//TODO remove 
			System.out.println("-- Lua parsing finished!\n" + tree.getText());
			
			try {
				PrintStream out;
				
				// print to file outputs/prettyPrinted.lua
				out = new PrintStream(output);
				RenameVisitor p = new RenameVisitor(out);
				p.setRenamer("exampleFunction", "anotherFunctionName");
				p.visit(tree);
				out.close();
				
				// print to system.out
				RenameVisitor p2 = new RenameVisitor();
				p2.setRenamer("exampleFunction", "anotherFunctionName");
				p2.visit(tree);
				
				System.out.println("\n-- Lua Pretty printing finished!");
				
				
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