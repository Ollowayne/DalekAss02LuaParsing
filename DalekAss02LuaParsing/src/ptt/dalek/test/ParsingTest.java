package ptt.dalek.test;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.antlr.v4.runtime.RecognitionException;

import java.io.File;
import java.io.IOException;

import ptt.dalek.antlr.LuaParser;
import ptt.dalek.main.Parsing;

public class ParsingTest {

	/* Parsing test cases
	 * 
	 * testPositive1() tests with an empty file
	 * testPositive2() tests with a short, easy to read lua example
	 * testPositive3() tests a hole bunch of lua code
	 * 
	 * testNegative() error case, missing 'end' for function noLuaFunction()
	 */
	
	
    private static String emptyFile =
        "inputs"
        + File.separator
        + "example.lua";
    private static String simpleExample =
            "inputs"
            + File.separator
            + "example2.lua";
    private static String complexExample =
            "inputs"
            + File.separator
            + "example3.lua";
    
    private static String notLuaCode =
        "inputs"
        + File.separator
        + "noexample.lua";
    
    @Test
    public void testPositive1() 
    		throws RecognitionException, IOException  {
		LuaParser parser = Parsing.parse(emptyFile);
		parser.chunk();
		
		assertTrue(parser.getNumberOfSyntaxErrors() == 0);
    }
    
    @Test
    public void testPositive2() 
    		throws RecognitionException, IOException  {
		LuaParser parser = Parsing.parse(simpleExample);
		parser.chunk();
		
		assertTrue(parser.getNumberOfSyntaxErrors() == 0);
    }

    @Test
    public void testPositive3() 
    		throws RecognitionException, IOException {
		LuaParser parser = Parsing.parse(complexExample);
		parser.chunk();
		
		assertTrue(parser.getNumberOfSyntaxErrors() == 0);
    }
    
    @Test
    public void testNegative() 
    		throws RecognitionException, IOException {  
    	
    		System.out.println("-- Negative Example: ");
		LuaParser parser = Parsing.parse(notLuaCode);
		parser.chunk();
		
		assertTrue(parser.getNumberOfSyntaxErrors() > 0);
    }
}