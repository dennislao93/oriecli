package main;

public class Debug {

	public static boolean debug = false;
	private static char debugFlag;
	public static int cycleNum = 0;

	public static void setDebug(boolean b) {
		debug = b;
	}

	/**
	 * a - all
	 * n - network
	 * r - random
	 * @param string
	 */
	public static void setFlag(String string) {
		debugFlag = string.charAt(0);
	}

	public static void print(char flag, String s) {
		if (debug && (debugFlag == flag || debugFlag == 'a')) System.out.print(s);
	}

	public static void println(char flag, String s) {
		if (debug && (debugFlag == flag || debugFlag == 'a')) System.out.println(s);
	}

}
