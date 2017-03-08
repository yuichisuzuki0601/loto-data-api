package jp.co.sbro.loto_data_api.util;

public final class NumberUtil {

	private NumberUtil() {
	}

	public static boolean hasNumber(String str) {
		boolean result = false;
		if (str != null) {
			result = str.matches(".*[0-9].*");
		}
		return result;
	}

}
