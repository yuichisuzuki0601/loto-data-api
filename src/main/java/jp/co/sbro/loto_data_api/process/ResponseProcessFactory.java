package jp.co.sbro.loto_data_api.process;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

public final class ResponseProcessFactory {

	private final static Map<String[], Class<? extends ResponseProcess>> MAP = new LinkedHashMap<>();
	static {
		MAP.put(new String[] { "次回", "予想" }, ForecastProcess.class);
		MAP.put(new String[] { "前回" }, LatestProcess.class);
		MAP.put(new String[] { "第", "回", "指定" }, SpecificProcess.class);
		MAP.put(new String[] { "こんにちは" }, HelloProcess.class);
		MAP.put(new String[] { "かわいい", "可愛い", "いいね", "良いね", "ありがとう", "サンクス" }, GladProcess.class);
		MAP.put(new String[] { "嫌", "汚", "臭", "死", "殺", "ばか", "あほ", "まぬけ", "うんこ" }, HateProcess.class);
	}

	private ResponseProcessFactory() {
	}

	public static ResponseProcess getResponseProcess(MessageEvent<TextMessageContent> event) {
		if (SpecificProcess.que.contains(event.getSource().getUserId())) {
			return new SpecificProcess();
		}
		for (Entry<String[], Class<? extends ResponseProcess>> e : MAP.entrySet()) {
			String message = event.getMessage().getText();
			for (String keyword : e.getKey()) {
				if (message.contains(keyword)) {
					try {
						return e.getValue().newInstance();
					} catch (InstantiationException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return new DefaultProcess();
	}

}
