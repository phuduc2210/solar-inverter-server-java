package solar.server.util;

public class ParseUtil {
	public static int parseInt(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return i;
	}

	public static double parseDouble(String s) {
		double i = 0D;
		try {
			i = Double.parseDouble(s);
		} catch (Exception e) {
			e.printStackTrace();
			return 0D;
		}
		return i;
	}
}
