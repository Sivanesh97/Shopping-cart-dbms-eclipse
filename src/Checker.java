//$Id$

// Singleton class
public class Checker {
	static String s = null;
	static private Checker c;

	static protected Checker getInstance() {
		if (c == null) {
			c = new Checker();
		}

		return c;
	}

	protected boolean isEmpty(String s) {
		if (s.equals("")) {
			System.out.println("This field must not be empty");
			return true;
		}
		return false;
	}
}
