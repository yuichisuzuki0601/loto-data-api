package jp.co.sbro.loto_data_api.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.sbro.loto_data.data.WinResult;
import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.util.LotoDataDateFormat;
import jp.co.sbro.util.SortableList;

/**
 * 反省サービス
 * 
 * @author yuichi
 *
 */
public final class Reflection {

	private final static SimpleDateFormat SDF = new LotoDataDateFormat();

	private Reflection() {
	}

	public static List<String> reflect(WinResultRepository repos) {
		List<String> messages = new ArrayList<>();
		WinResult latest = repos.getLatestWinResult();

		String time = String.valueOf(latest.getTime());
		String ymd = SDF.format(latest.getDate());

		String mes1 = "第" + time + "回(" + ymd + "抽選)の結果ロンね！";
		messages.add(mes1);

		String mes2 = "結果は\n";
		mes2 += latest.getHits().stream().map(num -> String.format("%02d", num)).reduce((n, m) -> n + ", " + m).get();
		mes2 += " (" + String.format("%02d", latest.getBonus()) + ")\n";
		mes2 += "だったロンよ！";
		messages.add(mes2);

		StringBuilder mes3 = new StringBuilder("その時ボクが予想したのは");
		repos = repos.filterByEnd(repos.getLatestWinResult().getTime() - 1);
		Map<Integer, SortableList<Integer>> candidates = Forecast.forecast(repos);

		if (candidates.isEmpty()) {
			messages.add(mes3.toString());
			messages.add("ごめん。前過ぎて覚えてないロン。");
			return messages;
		}
		mes3.append("\n\n");

		boolean hasPrize = false;
		boolean isFirst = true;
		for (Entry<Integer, SortableList<Integer>> e : candidates.entrySet()) {
			mes3.append(!isFirst ? "\n\n" : "");
			SortableList<Integer> list = e.getValue();
			String line = list.stream().map(num -> String.format("%02d", num)).reduce((n, m) -> n + ", " + m).get();
			long hits = list.stream().filter(lObj -> latest.getHits().contains(lObj)).count();
			if (hits >= 3) {
				hasPrize = true;
			}
			mes3.append(line).append("\n→").append(hits).append("個ヒット！");
			isFirst = false;
		}
		messages.add(mes3.toString());

		String mes4 = hasPrize ? "入賞があったロン！嬉しいロン！" : "はずれたロン。。ごめんロン。。";
		messages.add(mes4);

		return messages;
	}

}
