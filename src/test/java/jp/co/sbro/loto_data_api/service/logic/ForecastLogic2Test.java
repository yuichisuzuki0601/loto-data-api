package jp.co.sbro.loto_data_api.service.logic;

import org.junit.Test;

import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.service.Reflection;

public class ForecastLogic2Test {

	@Test
	public void test() throws Exception {
		System.out.println(new ForecastLogic2().forecast(WinResultTable.getInstance().select()));
	}

	@Test
	public void rolling() throws Exception {
		WinResultRepository repos = WinResultTable.getInstance().select();
		for (int i = 0; i <= 100; ++i) {
			for (int time = 1045; time < 10000; ++time) {
				if (time > repos.getLatestWinResult().getTime()) {
					break;
				}
				ForecastLogic2.rollCnt = i;
				for (String aa : Reflection.reflect(repos.filterByEnd(time))) {
					String a = "6個ヒット";
					if (aa.contains(a)) {
						System.out.println(a + "を含んでいます。回転数=" + i + ", 回=" + time);
					}
					String b = "5個ヒット";
					if (aa.contains(b)) {
						System.out.println(b + "を含んでいます。回転数=" + i + ", 回=" + time);
					}
					String c = "4個ヒット";
					if (aa.contains(c)) {
						System.out.println(c + "を含んでいます。回転数=" + i + ", 回=" + time);
					}
				}
			}
		}
	}

}
