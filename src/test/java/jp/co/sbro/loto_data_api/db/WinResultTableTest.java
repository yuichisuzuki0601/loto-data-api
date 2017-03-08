package jp.co.sbro.loto_data_api.db;

import org.junit.Test;

import jp.co.sbro.loto_data.data.WinResultRepository;

public class WinResultTableTest {

	@Test
	public void test() throws Exception {
		WinResultRepository r = WinResultTable.getInstance().select();
		r.filterByCnt(10).entrySet().stream().forEach(System.out::println);
	}

}
