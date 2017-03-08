package jp.co.sbro.loto_data_api.util;

public final class Sleeper {

	private Sleeper() {
	}

	public static void sleep(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
		}
	}

}
