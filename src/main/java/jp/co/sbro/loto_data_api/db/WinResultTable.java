package jp.co.sbro.loto_data_api.db;

import java.util.Date;
import java.util.Map;

import jp.co.sbro.lite_db.Table;
import jp.co.sbro.loto_data.data.Scraper;
import jp.co.sbro.loto_data.data.WinResult;
import jp.co.sbro.loto_data.data.WinResult.Prize;
import jp.co.sbro.loto_data.data.WinResultRepository;

public class WinResultTable {

	private final static String DEFINE = "db/define/loto_six_data.json";

	private static WinResultTable instance;

	public static WinResultTable getInstance() throws Exception {
		if (instance == null) {
			instance = new WinResultTable();
		}
		return instance;
	}

	private Table table;

	private WinResultTable() throws Exception {
		this.table = Table.get(DEFINE);
		if (!table.exists()) {
			table.create();
			insert(new Scraper().getWinResultRepository());
		}
	}

	public void insert(WinResultRepository repos) {
		repos.entrySet().forEach(r -> insert(r.getValue()));
	}

	public void insert(WinResult winResult) {
		Object[] objs = new Object[20];
		objs[0] = winResult.getTime();
		objs[1] = winResult.getDate();
		int i = 2;
		for (int number : winResult.getHits()) {
			objs[i++] = number;
		}
		objs[8] = winResult.getBonus();
		int j = 9;
		for (Prize prize : winResult.getPrizes()) {
			objs[j++] = prize.getUnit();
			objs[j++] = prize.getPrice();
		}
		objs[19] = winResult.getCarryOver();
		table.insertIfNotExists(objs);
	}

	public WinResultRepository select() {
		WinResultRepository repos = new WinResultRepository();
		for (Map<String, Object> map : table.select()) {
			int time = (Integer) map.get("TIME");
			WinResult r = new WinResult(time);
			r.setDate(new Date(((java.sql.Date) map.get("DATE")).getTime()));
			r.addHits((Integer) map.get("ONE"));
			r.addHits((Integer) map.get("TWO"));
			r.addHits((Integer) map.get("THREE"));
			r.addHits((Integer) map.get("FOUR"));
			r.addHits((Integer) map.get("FIVE"));
			r.addHits((Integer) map.get("SIX"));
			r.setBonus((Integer) map.get("BONUS"));
			Object firstUnit = map.get("FIRST_UNIT");
			if (firstUnit != null) {
				Prize first = r.addPrize((Integer) firstUnit);
				first.setPrice((Integer) map.get("FIRST_PRICE"));
			}
			Object secondUnit = map.get("SECOND_UNIT");
			if (secondUnit != null) {
				Prize second = r.addPrize((Integer) secondUnit);
				second.setPrice((Integer) map.get("SECOND_PRICE"));
			}
			Object thirdUnit = map.get("THIRD_UNIT");
			if (thirdUnit != null) {
				Prize third = r.addPrize((Integer) thirdUnit);
				third.setPrice((Integer) map.get("THIRD_PRICE"));
			}
			Object fourthUnit = map.get("FOURTH_UNIT");
			if (fourthUnit != null) {
				Prize fourth = r.addPrize((Integer) fourthUnit);
				fourth.setPrice((Integer) map.get("FOURTH_PRICE"));
			}
			Object fifthUnit = map.get("FIFTH_UNIT");
			if (fifthUnit != null) {
				Prize fifth = r.addPrize((Integer) fifthUnit);
				fifth.setPrice((Integer) map.get("FIFTH_PRICE"));
			}
			Object carryOver = map.get("CARRY_OVER");
			if (carryOver != null) {
				r.setCarryOver((Integer) carryOver);
			}
			repos.put(time, r);
		}
		return repos;
	}

}
