package jp.co.sbro.loto_data_api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.sbro.loto_data.data.WinResult;
import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.service.logic.ForecastLogic;
import jp.co.sbro.loto_data_api.service.logic.ForecastLogic2;
import jp.co.sbro.util.SortableList;

/**
 * 予想サービス
 * 
 * @author yuichi
 *
 */
public final class Forecast {

	private final static ForecastLogic logic = new ForecastLogic2();
	
	private Forecast() {
	}

	public static List<String> forecast() throws Exception {
		List<String> messages = new ArrayList<>();
		WinResultRepository repos = WinResultTable.getInstance().select();
		WinResult latest = repos.getLatestWinResult();
		String time = String.valueOf(latest.getTime() + 1);

		String mes1 = "第" + time + "回(次回)の予想ロンね！";
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
