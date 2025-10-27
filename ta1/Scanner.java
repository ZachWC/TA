/**
 * This class is a scanner (lexical analyzer) for the program
 * and programming language being interpreted.
 * It tokenizes the input source code.
 */
import java.util.*;

public class Scanner {

	private String program;		// source program being interpreted
	private int pos;			// index of next char in program
	private Token token;		// last/current scanned token

	// sets of various characters and lexemes
	private Set<String> whitespace=new HashSet<String>();
	private Set<String> digits=new HashSet<String>();
	private Set<String> letters=new HashSet<String>();
	private Set<String> legits=new HashSet<String>();
	private Set<String> keywords=new HashSet<String>();
	private Set<String> operators=new HashSet<String>();

	// initializers for previous sets

	private void fill(Set<String> s, char lo, char hi) {
		for (char c=lo; c<=hi; c++)
			s.add(c+"");
	}

	private void initWhitespace(Set<String> s) {
		s.add(" ");
		s.add("\n");
		s.add("\t");
	}

	private void initDigits(Set<String> s) {
		fill(s,'0','9');
	}

	private void initLetters(Set<String> s) {
		fill(s,'A','Z');
		fill(s,'a','z');
	}

	private void initLegits(Set<String> s) {
		s.addAll(letters);
		s.addAll(digits);
	}

	private void initOperators(Set<String> s) {
		s.add("=");
		s.add("+");
		s.add("-");
		s.add("*");
		s.add("/");
		s.add("(");
		s.add(")");
		s.add(";");
	}

	private void initKeywords(Set<String> s) {
	}

	// constructor:
	//     - squirrel-away source program
	//     - initialize sets
	public Scanner(String program) {
		this.program=program;
		pos=0;
		token=null;
		initWhitespace(whitespace);
		initDigits(digits);
		initLetters(letters);
		initLegits(legits);
		initKeywords(keywords);
		initOperators(operators);
	}

	// handy string-processing methods

	/**
	 * Checks if the entire source program has been scanned.
	 * @return true if at end of program
	 */
	public boolean done() {
		return pos>=program.length();
	}

	/**
	 * Advances the position past a sequence of characters in the given set.
	 * @param s Set of characters to consume
	 */
	private void many(Set<String> s) {
		while (!done()&&s.contains(program.charAt(pos)+""))
			pos++;
	}

	/**
	 * Advances the scanner until just after a specific character is found.
	 * Used primarily to skip comments until end of line.
	 * @param c The character to search for
	 */
	private void past(char c) {
		while (!done()&&c!=program.charAt(pos))
			pos++;
		if (!done()&&c==program.charAt(pos))
			pos++;
	}

	// scan various kinds of lexeme

	/**
	 * Scans a numeric literal (integer or floating point).
	 * Handles both whole numbers and decimal numbers.
	 */
	private void nextNumber() {
		int old=pos;
		many(digits);
		// Handle decimal point for floating point numbers
		if (!done() && program.charAt(pos) == '.') {
			pos++;
			many(digits);
		}
		token=new Token("num",program.substring(old,pos));
	}

	/**
	 * Scans a keyword or identifier.
	 * First scans letters, then any combination of letters and digits.
	 * Checks if the resulting lexeme is a keyword.
	 */
	private void nextKwId() {
		int old=pos;
		many(letters);
		many(legits);
		String lexeme=program.substring(old,pos);
		token=new Token((keywords.contains(lexeme) ? lexeme : "id"),lexeme);
	}

	/**
	 * Scans an operator.
	 * Attempts to scan two-character operators first,
	 * then falls back to one-character operators.
	 */
	private void nextOp() {
		int old=pos;
		pos=old+2;
		if (!done()) {
			String lexeme=program.substring(old,pos);
			if (operators.contains(lexeme)) {
				token=new Token(lexeme); // two-char operator
				return;
			}
		}
		pos=old+1;
		String lexeme=program.substring(old,pos);
		token=new Token(lexeme); // one-char operator
	}

	/**
	 * Determines the kind of the next token (e.g., "id"),
	 * and calls a method to scan that token's lexeme (e.g., "foo").
	 * Skips whitespace and comments before scanning.
	 * @return true if a token was successfully scanned
	 */
	public boolean next() {
		many(whitespace);
		// Skip comments
		if (!done() && program.charAt(pos) == '/' && pos + 1 < program.length() && program.charAt(pos + 1) == '/') {
			past('\n');
			return next();
		}
		if (done()) {
			token=new Token("EOF");
			return false;
		}
		String c=program.charAt(pos)+"";
		if (digits.contains(c))
			nextNumber();
		else if (letters.contains(c))
			nextKwId();
		else if (operators.contains(c))
			nextOp();
		else {
			System.err.println("illegal character at position "+pos);
			pos++;
			return next();
		}
		return true;
	}

	/**
	 * Matches the current token with the expected token.
	 * Advances to the next token if match is successful.
	 * @param t The expected token
	 * @throws SyntaxException if the current token doesn't match the expected token
	 */
	public void match(Token t) throws SyntaxException {
		if (!t.equals(curr()))
			throw new SyntaxException(pos,t,curr());
		next();
	}

	/**
	 * Returns the current token.
	 * @return The current token
	 * @throws SyntaxException if no token has been scanned yet
	 */
	public Token curr() throws SyntaxException {
		if (token==null)
			throw new SyntaxException(pos,new Token("ANY"),new Token("EMPTY"));
		return token;
	}

	/**
	 * Returns the current position in the source program.
	 * @return The current index in the source program
	 */
	public int pos() {
		return pos;
	}

	// for unit testing
	public static void main(String[] args) {
		try {
			Scanner scanner=new Scanner(args[0]);
			while (scanner.next())
				System.out.println(scanner.curr());
		} catch (SyntaxException e) {
			System.err.println(e);
		}
	}

}
