package jp.co.sbro.loto_data_api;

import java.util.Arrays;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sbro.loto_data.data.WinResultRepository;
import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.task.SyncTask;

@RestController
@RequestMapping("loto")
public class LotoDataApiController {

	private WinResultRepository repos() throws Exception {
		return WinResultTable.getInstance().select();
	}

	@RequestMapping("sync")
	public String sync() throws Exception {
		SyncTask.getInstance().sync();
		return latest();
	}

	@RequestMapping("all")
	public String all() throws Exception {
		return repos().toString();
	}

	@RequestMapping("get")
	public String get(@RequestParam int time) throws Exception {
		return repos().getWinResult(time).toString();
	}

	@RequestMapping("latest")
	public String latest() throws Exception {
		return repos().getLatestWinResult().toString();
	}

	@RequestMapping("filter-by-times")
	public String filterByTimes(@RequestParam String times) throws Exception {
		return repos().filterByTimes(Arrays.stream(times.split(",")).map(Integer::parseInt).toArray(Integer[]::new))
				.toString();
	}

	@RequestMapping("filter-by-start-and-end")
	public String filterByStartAndEnd(@RequestParam int start, int end) throws Exception {
		return repos().filterByStartAndEnd(start, end).toString();
	}

	@RequestMapping("filter-by-start")
	public String filterByStart(@RequestParam int start) throws Exception {
		return repos().filterByStart(start).toString();
	}

	@RequestMapping("filter-by-end")
	public String filterByEnd(@RequestParam int end) throws Exception {
		return repos().filterByEnd(end).toString();
	}

	@RequestMapping("filter-by-cnt")
	public String filterByCnt(@RequestParam int time) throws Exception {
		return repos().filterByCnt(time).toString();
	}

	@RequestMapping("frequency")
	public String frequency() throws Exception {
		return repos().frequency().toString();
	}

}
