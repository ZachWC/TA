/**
 * Parse tree node representing a unary minus operator (-expr).
 */
public class NodeFactUnMinus extends NodeFact {

	private NodeFact fact;

	public NodeFactUnMinus(NodeFact fact) {
		this.fact = fact;
	}

	public double eval(Environment env) throws EvalException {
		return -fact.eval(env);
	}

	public String code() {
		return "-" + fact.code();
	}

}
