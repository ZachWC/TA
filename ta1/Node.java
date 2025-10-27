/**
 * This class, and its subclasses,
 * collectively model parse-tree nodes.
 * Each kind of node can be eval()-uated,
 * and/or code()-generated.
 */
public abstract class Node {

	protected int pos=0;

	/**
	 * Evaluates this node in the given environment.
	 * @param env The environment containing variable values
	 * @return The result of the evaluation
	 * @throws EvalException if an evaluation error occurs
	 */
	public double eval(Environment env) throws EvalException {
		throw new EvalException(pos,"cannot eval() node!");
	}

	/**
	 * Generates target code for this node.
	 * @return Generated code as a string
	 */
	public String code() { return ""; }

}
