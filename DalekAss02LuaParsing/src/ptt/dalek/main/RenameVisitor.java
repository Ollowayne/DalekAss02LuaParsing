package ptt.dalek.main;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import ptt.dalek.antlr.LuaBaseVisitor;
import ptt.dalek.antlr.LuaParser.*;

public class RenameVisitor extends LuaBaseVisitor<Void>{
	public static final int INDENT_FACTOR = 4;
	public static final String SPACE = " ";
	
	// blacklist contains all names, that can not be used
	public static final String[] blacklist = {	"local", 
												"function", 
												"break", 
												"goto", 
												"do", 
												"end", 
												"while", 
												"repeat", 
												"until", 
												"if", 
												"then", 
												"elseif", 
												"else", 
												"for", 
												"in"
												};
	
	
	// HELPER METHODS
	
	private int indent = -4;
	
	private HashMap<String, String> names = new HashMap<String, String>(); 
	
	private boolean renameEnabled = true;
	private boolean blacklistEnabled = true;
	private boolean namecheckEnabled = true;
	
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
	public RenameVisitor(PrintStream out) {
		this.out=out;
	}
	
	// default constructed with system.out
	public RenameVisitor() {
		this.out=System.out;
	}
	
	// RENAMER STUFF
	
	public void setRenamer() {
		this.renameEnabled = true;
	}
	
	public void setRenamer(HashMap<String, String> names) {
		addNames(names);
		this.renameEnabled = true;
	}
	
	public void disableBlacklist() {
		blacklistEnabled = false;
	}
	
	public void enableBlacklist() {
		blacklistEnabled = true;
	}
	
	public void disableNamecheck() {
		blacklistEnabled = false;
	}
	
	public void enableNamecheck() {
		blacklistEnabled = false;
	}
	
	// adds name + rename to names, checks whether rename is a) valid lua name b) not blacklisted (as syntax token) 
	// if namecheck and blacklist are enambled
	public void addName(String name, String rename) {
		if(namecheckEnabled && rename.matches("([a-zA-Z]|_)([a-zA-Z]|[0-9]|_)*")) {
			if(!blacklisted(rename)) {
				names.put(name, rename);
			}
		}
	}
	
	// adds HashMap to names
	public void addNames(HashMap<String, String> names) {
		for(Entry<String, String> entry : names.entrySet()) {
		    String name = entry.getKey();
		    String rename = entry.getValue();
		    
		    addName(name, rename);
		}
	}
	
	// checks whether rename is element of blacklist (if check is enabled)
	private boolean blacklisted(String rename) {
		if(blacklistEnabled) {
			for(String s : blacklist) {
				if(s.equals(rename)) {
					return true;
				}
			}
		}

		return false;
	}

	// VISITOR HELPER (DEFAULT) 
	// note: default cases are not commented any further
	
	// default case for terminal print(c) or visit(c) to enhance readability
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
	
	// default case for terminal print(c + SPACE) or visit(c) to enhance readability
	private void visitDefaultSpaced(ParseTree c) {
		//if terminal, print
		if (c instanceof TerminalNodeImpl) {
			print(c, SPACE);
		}
		// else visit children
		else {
			visit(c);
		}
	}
	
	// PRINT METHODS 
	
	// prints strings s1 and s2, as well as ParseTree c, while renaming content of c if it equals the specified name
	private void print(String s1, ParseTree c, String s2) {
		if(renameEnabled && checkName(c.getText())) {
			out.print(s1 + names.get(c.getText()) + s2);
		}
		else {
			out.print(s1 + c + s2);
		}
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
	
	private boolean checkName(String name) {
		String t = names.get(name);
		return t != null;
	}
	
	
	// +++
	
	// VISITOR METHODS
	
	// +++
	
	
	@Override
	public Void visitChunk(ChunkContext ctx) {
		ParseTree c;
		
		// for all children except <EOF>
		for (int i = 0; i < ctx.getChildCount() - 1; i++) {
			if ((c = ctx.getChild(i)) instanceof TerminalNodeImpl) {
				print(c, SPACE);
			}
			else {
				visit(c);
			}
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
					print(SPACE, c, SPACE);
				}
				else if(c.getText().equals("function")) {
					// function start in a new line
					newline();
					print(c, SPACE);
				}
				else {
					// other stats are spaced
					print(c, SPACE);
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
					print(c, SPACE);
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
					print(c, SPACE);
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
					print(c, SPACE);
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
					print(c, SPACE);				
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
					print(SPACE, c, SPACE);
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
			print(c, SPACE);
		}
		return null;
	}
	
	@Override
	public Void visitBinop(BinopContext ctx) {
		for (ParseTree c : ctx.children) {
			// binops are surrounded by spaces
			print(SPACE, c, SPACE);
		}
		return null;
	}
	
	@Override
	public Void visitUnop(UnopContext ctx) {
		for (ParseTree c : ctx.children) {
			if(c.getText().equals("not")) {
				// not is the only unop surrounded with spaces
				print(SPACE, c, SPACE);
			}
			else {
				print(SPACE, c);
			}
		}
		return null;
	}
	
}

