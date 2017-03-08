package jp.co.sbro.loto_data_api.util;

import java.text.SimpleDateFormat;

public class LotoDataDateTimeFormat extends SimpleDateFormat {

	private static final long serialVersionUID = 1L;

	public LotoDataDateTimeFormat() {
		super("yyyy-MM-dd HH:mm:ss.SSS");
	}

}
