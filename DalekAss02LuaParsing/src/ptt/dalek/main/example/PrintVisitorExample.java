package ptt.dalek.main.example;

import static ptt.dalek.main.Parsing.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;

import ptt.dalek.antlr.LuaParser;
import ptt.dalek.main.PrintVisitor;

public class PrintVisitorExample {
	
	   /*
	    * Example for pretty printing.
	    * Prints to prettyPrinted.lua as well as system.out.
	    * 	note: This is just an example. 
	    * 	Correctness of resulting lua file is tested in ptt.dalek.test/RenameVisitorTest.
	    *  
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

			System.out.println("-- Lua parsing finished!\n" + tree.getText());
			
			try {
				PrintStream out;
				
				// print to file outputs/prettyPrinted.lua
				out = new PrintStream(output);
				PrintVisitor p = new PrintVisitor(out);
				p.visit(tree);
				out.close();
				
				// print to system.out
				PrintVisitor p2 = new PrintVisitor();
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
