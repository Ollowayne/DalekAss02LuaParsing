package ptt.dalek.main;

import java.io.PrintStream;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import ptt.dalek.antlr.LuaBaseVisitor;
import ptt.dalek.antlr.LuaParser.*;

public class PrintVisitor extends LuaBaseVisitor<Void>{
	public static final int INDENT_FACTOR = 4;
	public static final String SPACE = " ";
	
	// HELPER METHODS
	
	private int indent = -4;
	
	// prints #indent SPACE to output stream out
	private void indent() {
		for(int i = 0; i < indent; i++) {
			out.print(SPACE);
		}
	}
	
	// increments the indent by INDENT_FACTOR
	private void incIndent() {
		indent += INDENT_FACTOR;
	}
	
	// ...et vice versa
	private void decIndent() {

		indent -= INDENT_FACTOR;
	}
	
	// prints newline to output stream out
	private void newline() {
		out.print("\n");
	}

	// CONSTUCTOR
	
	PrintStream out;
	
	// constructed with PrintStream out
	public PrintVisitor(PrintStream out) {
		this.out=out;
	}
	
	// default constructed with system.out
	public PrintVisitor() {
		this.out=System.out;
	}
	
	// VISITOR HELPER (DEFAULT) 
	
	// default case for terminal print(c) or visit(c) to enhance readability
	private void visitDefault(ParseTree c) {
		if (c instanceof TerminalNodeImpl) {
			out.print(c);
		}
		else {
			visit(c);
		}
	}
	
	// default case for terminal print(c + SPACE) or visit(c) to enhance readability
	private void visitDefaultSpaced(ParseTree c) {
		if (c instanceof TerminalNodeImpl) {
			out.print(c + SPACE);
		}
		else {
			visit(c);
		}
	}
	
	
	// VISITOR METHODS
	
	@Override
	public Void visitChunk(ChunkContext ctx) {
		out.print("-- pretty printed Lua code\n");
		
		ParseTree c;
		
		for (int i = 0; i < ctx.getChildCount() - 1; i++) {
			if ((c = ctx.getChild(i)) instanceof TerminalNodeImpl)
				out.print(c.getText() + SPACE);
			else
				visit(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitBlock(BlockContext ctx) {
		newline();
		incIndent();
		
		// catches case for empty input
		if(ctx.children == null) 
			return null;
		
		for (ParseTree c : ctx.children) {
			visitDefaultSpaced(c);
		}
		
		decIndent();
		indent();
		return null;
	}
	
	@Override
	public Void visitStat(StatContext ctx) {
		indent();
		
		for (ParseTree c : ctx.children) {
			if (c instanceof TerminalNodeImpl) {
				if(c.getText().equals("=") | c.getText().equals("then") | c.getText().equals("do") | c.getText().equals("in")) {
					out.print(SPACE + c + SPACE);
				}
				else if(c.getText().equals("function")) {
					out.print("\n" + c + SPACE);
				}
				else {
					out.print(c + SPACE);
				}
			}
			else {
				visit(c);
			}
		}
		newline();
		return null;
	}
	
	@Override
	public Void visitRetstat(RetstatContext ctx) {
		indent();
		for (ParseTree c : ctx.children) {
			visitDefaultSpaced(c);
		}
		
		newline();
		return null;
	}
	
	@Override
	public Void visitLabel(LabelContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefaultSpaced(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitFuncname(FuncnameContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitVarlist(VarlistContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefaultSpaced(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitNamelist(NamelistContext ctx) {
		for (ParseTree c : ctx.children) {
			if (c instanceof TerminalNodeImpl) {
				if(c.getText().equals(",")) 
					out.print(c + SPACE);
				else
					out.print(c);
			}
			else {
				visit(c);
			}
		}
		
		return null;
	}
	
	@Override
	public Void visitExplist(ExplistContext ctx) {
		for (ParseTree c : ctx.children) {
			if (c instanceof TerminalNodeImpl) {
				if(c.getText().equals(",")) {
					out.print(c + SPACE);
				}
				else {
					out.print(c);
				}
			}
			else {
				visit(c);
			}
		}
		
		return null;
	}
	
	@Override
	public Void visitExp(ExpContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitVar(VarContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		return null;
	}
	
	@Override
	public Void visitPrefixexp(PrefixexpContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitFunctioncall(FunctioncallContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitArgs(ArgsContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitFunctiondef(FunctiondefContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefaultSpaced(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitFuncbody(FuncbodyContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitParlist(ParlistContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitTableconstructor(TableconstructorContext ctx) {
		for (ParseTree c : ctx.children) {
			if (c instanceof TerminalNodeImpl) {
				if(c.getText().equals("{")) {
					out.print(c);
				}
				else if(c.getText().equals("}")) {
					out.print(c);
				}
				else {
					out.print(c + SPACE);				
				}
			}
			else {
				visit(c);
			}
		}
		
		return null;
	}
	
	@Override
	public Void visitFieldlist(FieldlistContext ctx) {
		for (ParseTree c : ctx.children) {
			visitDefault(c);
		}
		
		return null;
	}
	
	@Override
	public Void visitField(FieldContext ctx) {
		for (ParseTree c : ctx.children) {
			if (c instanceof TerminalNodeImpl) {
				if(c.getText().equals("=")) {
					out.print(SPACE + c + SPACE);
				}
				else {
					out.print(c);
				}
			}
			else {
				visit(c);
			}
		}
		
		return null;
	}
	
	@Override
	public Void visitFieldsep(FieldsepContext ctx) {
		for (ParseTree c : ctx.children) {
			out.print(c + SPACE);
		}
		
		return null;
	}
	
	public Void visitBinop(BinopContext ctx) {
		for (ParseTree c : ctx.children) {
			out.print(SPACE + c + SPACE);
		}
		
		return null;
	}
	
	public Void visitUnop(UnopContext ctx) {
		for (ParseTree c : ctx.children) {
			if(c.getText().equals("not")) {
				out.print(SPACE + c + SPACE);
			}
			else {
				out.print(SPACE + c);
			}
		}
		
		return null;
	}
}
