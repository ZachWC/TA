/**
 * This class provides an environment for storing and retrieving variable values.
 * Accessing an undefined variable throws an exception.
 * 
 * This environment uses a HashMap to map variable names to their double values.
 */
import java.util.HashMap;
import java.util.Map;

public class Environment {

	private Map<String, Double> variables = new HashMap<String, Double>();

	/**
	 * Stores a variable value in the environment.
	 * @param var Variable name
	 * @param val Variable value
	 * @return The value that was stored
	 */
	public double put(String var, double val) {
		variables.put(var, val);
		return val;
	}

	/**
	 * Retrieves a variable value from the environment.
	 * @param pos Position in source for error reporting
	 * @param var Variable name
	 * @return The variable's value
	 * @throws EvalException if the variable is not defined
	 */
	public double get(int pos, String var) throws EvalException {
		if (!variables.containsKey(var)) {
			throw new EvalException(pos, "undefined variable: " + var);
		}
		return variables.get(var);
	}

	/**
	 * Generates C code to declare all variables in the environment.
	 * @return C code declaring all variables as doubles
	 */
	public String toC() {
		String s = "";
		String sep = " ";
		for (String v : variables.keySet()) {
			s += sep + v;
			sep = ",";
		}
		return s == "" ? "" : "double" + s + ";\n";
	}

}
