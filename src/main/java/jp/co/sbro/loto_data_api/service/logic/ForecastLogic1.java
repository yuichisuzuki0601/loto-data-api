package jp.co.sbro.loto_data_api.service.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.util.SortableList;

public class ForecastLogic1 implements ForecastLogic {

	@Override
	public Map<Integer, SortableList<Integer>> forecast(WinResultRepository repos) {
		// 前回採用された数字一覧
		Set<Integer> latestSet = repos.getLatestWinResult().getHitsAndBonus();

		// キャリーオーバーが出た回の各数字の出現頻度(出現の多い順)
		Map<Integer, Integer> frequency = repos.entrySet().stream().filter(e -> e.getValue().getCarryOver() != 0)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k, v) -> k, WinResultRepository::new))
				.frequency().sortByKey().reverse().sortByValue().reverse();

		// 過去データ過ぎて取れなかった場合
		if (frequency.isEmpty()) {
			return new HashMap<>();
		}

		// 前回採用された数字の中からキャリーオーバー回で最も多く採用されている数を決定
		int num1 = 0;
		for (Entry<Integer, Integer> e : frequency.entrySet()) {
			int number = e.getKey();
			if (latestSet.contains(number)) {
				num1 = number;
				break;
			}
		}
		frequency.remove(num1);

		// 出現の多い順に並んだ数字を7チームに分割していく(1つは必ず上記で決まった数字)
		Map<Integer, SortableList<Integer>> candidates = new HashMap<>();
		for (int i = 0; i < 7; ++i) {
			SortableList<Integer> list = new SortableList<>();
			list.add(num1);
			candidates.put(i, list);
		}
		int cnt = 0;
		for (Entry<Integer, Integer> e : frequency.entrySet()) {
			int key = cnt++ % 7;
			SortableList<Integer> list = candidates.get(key);
			if (list.size() < 6) {
				list.add(e.getKey());
			}
			candidates.put(key, list.sort());
		}

		// 上位2セット以外を間引く
		for (int i = 2; i < 7; ++i) {
			candidates.remove(i);
		}

		return candidates;
	}

}
