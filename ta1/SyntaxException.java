/**
 * Exception thrown when a syntax error occurs during parsing.
 * Contains position information, expected token, and found token.
 */
public class SyntaxException extends Exception {

	private int pos;
	private Token expected;
	private Token found;

	public SyntaxException(int pos, Token expected, Token found) {
		this.pos=pos;
		this.expected=expected;
		this.found=found;
	}

	public String toString() {
		return "syntax error"
			+", pos="+pos
			+", expected="+expected
			+", found="+found;
	}

}
