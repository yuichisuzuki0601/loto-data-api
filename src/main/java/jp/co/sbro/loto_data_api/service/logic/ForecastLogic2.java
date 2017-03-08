package jp.co.sbro.loto_data_api.service.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.util.Counter;
import jp.co.sbro.util.SortableList;
import jp.co.sbro.util.SortableMap;

public class ForecastLogic2 implements ForecastLogic {

	public static class GroupCount implements Comparable<GroupCount> {
		private int cntA = 0;
		private int cntB = 0;
		private int cntC = 0;
		private int cntD = 0;

		public GroupCount(Set<Integer> hits) {
			for (int n : hits) {
				if (1 <= n && n <= 11) {
					cntA++;
				} else if (12 <= n && n <= 22) {
					cntB++;
				} else if (23 <= n && n <= 33) {
					cntC++;
				} else if (34 <= n && n <= 43) {
					cntD++;
				}
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + cntA;
			result = prime * result + cntB;
			result = prime * result + cntC;
			result = prime * result + cntD;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GroupCount other = (GroupCount) obj;
			if (cntA != other.cntA)
				return false;
			if (cntB != other.cntB)
				return false;
			if (cntC != other.cntC)
				return false;
			if (cntD != other.cntD)
				return false;
			return true;
		}

		@Override
		public String toString() {
			List<Integer> list = new LinkedList<>();
			list.add(cntA);
			list.add(cntB);
			list.add(cntC);
			list.add(cntD);
			return list.toString();
		}

		@Override
		public int compareTo(GroupCount o) {
			return this.toString().compareTo(o.toString());
		}
	}

	public static int rollCnt = 81;

	@Override
	public Map<Integer, SortableList<Integer>> forecast(WinResultRepository repos) {
		// キャリーオーバーが出た回の各数字の出現頻度(出現の多い順)
		WinResultRepository coRepos = repos.entrySet().stream().filter(e -> e.getValue().getCarryOver() != 0)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k, v) -> k, WinResultRepository::new));
		Map<Integer, Integer> frequency = coRepos.frequency().sortByKey().reverse().sortByValue().reverse();

		// 過去データ過ぎて取れなかった場合
		if (frequency.isEmpty()) {
			return new HashMap<>();
		}

		// 条件別に出現頻度をグループ分け
		Map<Integer, Integer> frequencyA = filteredMap(frequency, e -> 1 <= e.getKey() && e.getKey() <= 11);
		Map<Integer, Integer> frequencyB = filteredMap(frequency, e -> 12 <= e.getKey() && e.getKey() <= 22);
		Map<Integer, Integer> frequencyC = filteredMap(frequency, e -> 23 <= e.getKey() && e.getKey() <= 33);
		Map<Integer, Integer> frequencyD = filteredMap(frequency, e -> 34 <= e.getKey() && e.getKey() <= 43);

		// 回転
		int rollingNum = repos.getLatestWinResult().getTime() * rollCnt;
		frequencyA = rolling(frequencyA, rollingNum);
		frequencyB = rolling(frequencyB, rollingNum);
		frequencyC = rolling(frequencyC, rollingNum);
		frequencyD = rolling(frequencyD, rollingNum);

		// キャリーオーバー回が全てどんな出現グループの回だったのかを調べる
		SortableMap<GroupCount, Integer> gcc = Counter
				.count(coRepos.values().stream().map(w -> new GroupCount(w.getHits())).collect(Collectors.toList()));
		// 最も多い出現グループを取得
		GroupCount gc = gcc.sortByValue().reverse().entrySet().iterator().next().getKey();

		// 2セット作る
		Map<Integer, SortableList<Integer>> candidates = new HashMap<>();
		for (int i = 0; i < 2; ++i) {
			SortableList<Integer> list = new SortableList<>();
			candidates.put(i, list);
			fillList(list, frequencyA, gc.cntA);
			fillList(list, frequencyB, gc.cntB);
			fillList(list, frequencyC, gc.cntC);
			fillList(list, frequencyD, gc.cntD);
			list.sort((a, b) -> a - b);
		}

		return candidates;
	}

	private static Map<Integer, Integer> filteredMap(Map<Integer, Integer> frequency,
			Function<Entry<Integer, Integer>, Boolean> filter) {
		return frequency.entrySet().stream().filter(e -> filter.apply(e))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k, v) -> k, SortableMap::new)).sortByKey()
				.reverse().sortByValue().reverse();
	}

	private static Map<Integer, Integer> rolling(Map<Integer, Integer> org, int time) {
		for (int i = 0; i < time; ++i) {
			org = rollingOne(org);
		}
		return org;
	}

	private static Map<Integer, Integer> rollingOne(Map<Integer, Integer> org) {
		Map<Integer, Integer> result = new LinkedHashMap<>();
		Entry<Integer, Integer> first = null;
		for (Entry<Integer, Integer> e : org.entrySet()) {
			if (first == null) {
				first = e;
				continue;
			}
			result.put(e.getKey(), e.getValue());
		}
		result.put(first.getKey(), first.getValue());
		return result;
	}

	private static void fillList(SortableList<Integer> list, Map<Integer, Integer> frequencyX, int gcCnt) {
		for (int i = 0; i < gcCnt; ++i) {
			Iterator<Integer> it = frequencyX.keySet().iterator();
			list.add(it.next());
			it.remove();
		}
	}

}
