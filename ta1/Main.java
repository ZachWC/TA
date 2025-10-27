/**
 * This is the main class for the interpreter/compiler.
 * Each command-line argument is a complete program,
 * which is scanned, parsed, and evaluated.
 * All evaluations share the same environment,
 * so they can share variables.
 */
public class Main {

	/**
	 * Main entry point for the translator.
	 * Processes each command-line argument as a separate program.
	 * All programs share the same environment for variable storage.
	 * 
	 * @param args Array of source programs, each a complete assignment statement
	 */
	public static void main(String[] args) {
		Parser parser=new Parser();
		Environment env=new Environment();
		String code="";
		for (String prog: args)
			try {
				Node node=parser.parse(prog);
				node.eval(env);
				code+=node.code();
			} catch (Exception e) {
				System.err.println(e);
			}
		new Code(code,env);
	}

}
