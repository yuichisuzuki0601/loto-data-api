package jp.co.sbro.loto_data_api.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.sbro.loto_data.data.WinResult;
import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.service.logic.ForecastLogic;
import jp.co.sbro.loto_data_api.service.logic.ForecastLogic2;
import jp.co.sbro.util.LotoDataDateFormat;
import jp.co.sbro.util.SortableList;

/**
 * 予想サービス
 * 
 * @author yuichi
 *
 */
public final class Forecast {

	private final static SimpleDateFormat SDF = new LotoDataDateFormat();

	private final static ForecastLogic logic = new ForecastLogic2();

	private Forecast() {
	}

	private static Date getNextLotteryDate(Date latestDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(latestDate);
		while (true) {
			c.add(Calendar.DATE, 1);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.THURSDAY) {
				return c.getTime();
			}
		}
	}

	public static List<String> forecast() throws Exception {
		List<String> messages = new ArrayList<>();
		WinResultRepository repos = WinResultTable.getInstance().select();
		WinResult latest = repos.getLatestWinResult();
		String time = String.valueOf(latest.getTime() + 1);

		String nextLotteryDate = SDF.format(getNextLotteryDate(latest.getDate()));
		String mes1 = "次回 第" + time + "回(" + nextLotteryDate + "抽選)の予想ロンね！";
		messages.add(mes1);

		StringBuilder mes2 = new StringBuilder();
		Map<Integer, SortableList<Integer>> candidates = forecast(repos);
		boolean isFirst = true;
		for (Entry<Integer, SortableList<Integer>> e : candidates.entrySet()) {
			mes2.append(!isFirst ? "\n" : "");
			mes2.append(
					e.getValue().stream().map(num -> String.format("%02d", num)).reduce((n, m) -> n + ", " + m).get());
			isFirst = false;
		}
		messages.add(mes2.toString());

		String mes3 = "こんな感じで如何ロン？";
		messages.add(mes3);

		return messages;
	}

	public static Map<Integer, SortableList<Integer>> forecast(WinResultRepository repos) {
		return logic.forecast(repos);
	}

}
