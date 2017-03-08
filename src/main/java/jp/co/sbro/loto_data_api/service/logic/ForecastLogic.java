package jp.co.sbro.loto_data_api.service.logic;

import java.util.Map;

import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.util.SortableList;

public interface ForecastLogic {

	public Map<Integer, SortableList<Integer>> forecast(WinResultRepository repos);

}
