package jp.co.sbro.loto_data_api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.service.Forecast;
import jp.co.sbro.loto_data_api.service.Reflection;

@RestController
@RequestMapping("test")
public class TestController {

	private String join(List<String> list) {
		return list.stream().map(s -> s.replaceAll("\n", "<br>")).reduce((s1, s2) -> s1 + "<br>" + s2).get();
	}

	@RequestMapping("forecast")
	public String forecast() throws Exception {
		return join(Forecast.forecast());
	}

	@RequestMapping("reflection")
	public String reflection() throws Exception {
		return join(Reflection.reflect(WinResultTable.getInstance().select()));
	}

	@RequestMapping("reflection-specify")
	public String reflection(@RequestParam int time) throws Exception {
		return join(Reflection.reflect(WinResultTable.getInstance().select().filterByEnd(time)));
	}

	@RequestMapping("hello")
	public String hello() throws Exception {
		return "こんにちはロン！";
	}

	@RequestMapping("thanks")
	public String thanks() throws Exception {
		return "うう、嬉しいロンロン！";
	}

	@RequestMapping("hate")
	public String hate() throws Exception {
		return "うぅ。。悲しいロン。。";
	}

}
