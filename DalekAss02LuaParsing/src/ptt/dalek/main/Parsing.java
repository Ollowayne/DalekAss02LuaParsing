package ptt.dalek.main;

import ptt.dalek.antlr.LuaParser;
import ptt.dalek.antlr.LuaLexer;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;

import java.io.FileInputStream;
import java.io.IOException;

public class Parsing {

    public static LuaParser parse(String s)
            throws IOException, RecognitionException  {

		FileInputStream stream = new FileInputStream(s);
		ANTLRInputStream antlr = new ANTLRInputStream(stream);
		LuaLexer lexer = new LuaLexer(antlr);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		LuaParser parser = new LuaParser(tokens);
		
		return parser;
    }
}