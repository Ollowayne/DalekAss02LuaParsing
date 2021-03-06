package ptt.dalek.main;

import java.io.PrintStream;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import ptt.dalek.antlr.LuaBaseVisitor;
import ptt.dalek.antlr.LuaParser.*;

public class PrintVisitor extends LuaBaseVisitor<Void> {	
	private int indentFactor = 4;
	private String space = " ";
	
	// HELPER METHODS
	
	private int indent = -4;
	
	// prints #indent SPACE to output stream out
	private void indent() {
		for(int i = 0; i < indent; i++) {
			out.print(space);
		}
	}
	
	// increments the indent by INDENT_FACTOR
	private void incIndent() {
		indent += indentFactor;
	}
	
	// ...et vice versa
	private void decIndent() {
		indent -= indentFactor;
	}
	
	// prints newline to output stream out
	private void newline() {
		out.print("\n");
	}
	
	public void setSpace(String s) {
		space = s;
	}
	
	public void setIndentFactor(int i) {
		indentFactor = i;
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
	// note: default cases are not commented any further
	
	// default case for terminal print(c) or visit(c)
	private void visitDefault(ParseTree c) {
		//if terminal, print
		if (c instanceof TerminalNodeImpl) {
			print(c);
		}
		// else visit children
		else {
			visit(c);
		}
	}
	
	// default case for terminal print(c + SPACE) or visit(c)
	private void visitDefaultSpaced(ParseTree c) {
		//if terminal, print
		if (c instanceof TerminalNodeImpl) {
			print(c, space);
		}
		// else visit children
		else {
			visit(c);
		}
	}
	
	// PRINT METHODS 
	
	// prints strings s1 and s2, as well as ParseTree c
	private void print(String s1, ParseTree c, String s2) {
		out.print(s1 + c.getText() + s2);
	}
	
	private void print(String s1, ParseTree c) {
		print(s1, c, "");
	}
	
	private void print(ParseTree c, String s2) {
		print("", c, s2);
	}
	
	private void print(ParseTree c) {
		print("", c, "");
	}
	
	
	// +++
	
	// VISITOR METHODS
	
	// +++
	
	
	@Override
	public Void visitChunk(ChunkContext ctx) {

		// for all children except <EOF>
		for (int i = 0; i < ctx.getChildCount() - 1; i++) {
			visitDefaultSpaced(ctx.getChild(i));
		}
		return null;
	}
	
	@Override
	public Void visitBlock(BlockContext ctx) {
		// newline and increment indent when entering block 
		newline();
		incIndent();
		
		// catches case for empty input
		if(ctx.children == null) 
			return null;
		
		for (ParseTree c : ctx.children) {
			visitDefaultSpaced(c);
		}
		
		// decrement indent after leaving block
		decIndent();
		indent();
		return null;
	}
	
	@Override
	public Void visitStat(StatContext ctx) {
		// indent stats (in block)
		indent();
		
		for (ParseTree c : ctx.children) {
			if (c instanceof TerminalNodeImpl) {
				if(c.getText().equals("=") | c.getText().equals("then") | c.getText().equals("do") | c.getText().equals("in")) {
					// "=", "then", "do", "in" are surrounded by spaces
					print(space, c, space);
				}
				else if(c.getText().equals("function")) {
					// function start in a new line
					newline();
					print(c, space);
				}
				else {
					// other stats are spaced
					print(c, space);
				}
			}
			
			else {
				visit(c);
			}
		}
		
		// newline after each stat
		newline();
		return null;
	}
	
	@Override
	public Void visitRetstat(RetstatContext ctx) {
		// indent retstat (in block)
		indent();
		
		for (ParseTree c : ctx.children) {
			visitDefaultSpaced(c);
		}
		
		// newline at end of retstat
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
				if(c.getText().equals(",")) {
					// "," is the only terminal in namelist followed by space
					print(c, space);
				}
				else {
					print(c);
				}
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
					// "," is the only terminal in explist followed by space
					print(c, space);
				}
				else {
					print(c);
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
			if (c instanceof TerminalNodeImpl) {
				if(c.getText().equals("end")) {
					// end is followed by newline
					print(c, space);
					newline();
				}
				else {
					print(c);
				}
			}
			else {
				visit(c);
			}
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
				if(c.getText().equals("{") | c.getText().equals("}")) {
					// "{" and "}" are the only terminals in tableconstructor not followed by space
					print(c);
				}
				else {
					print(c, space);				
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
					// "=" is the only terminal in field surrounded by spaces
					print(space, c, space);
				}
				else {
					print(c);
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
			// fieldseps are followed by space
			print(c, space);
		}
		return null;
	}
	
	@Override
	public Void visitBinop(BinopContext ctx) {
		for (ParseTree c : ctx.children) {
			// binops are surrounded by spaces
			print(space, c, space);
		}
		return null;
	}
	
	@Override
	public Void visitUnop(UnopContext ctx) {
		for (ParseTree c : ctx.children) {
			if(c.getText().equals("not")) {
				// not is the only unop surrounded with spaces
				print(space, c, space);
			}
			else {
				print(space, c);
			}
		}
		return null;
	}
	
}

