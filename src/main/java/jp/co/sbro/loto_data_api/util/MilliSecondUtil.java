package jp.co.sbro.loto_data_api.util;

public final class MilliSecondUtil {

	private MilliSecondUtil() {
	}

	public final static long DAY(int day) {
		return HOUR(day * 24);
	}

	public final static long HOUR(int hour) {
		return MINUTE(hour * 60);
	}

	public final static long MINUTE(int minute) {
		return SECOND(minute * 60);
	}

	public final static long SECOND(int second) {
		return second * 1000;
	}

}
