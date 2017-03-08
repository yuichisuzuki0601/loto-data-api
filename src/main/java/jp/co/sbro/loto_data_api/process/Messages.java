package jp.co.sbro.loto_data_api.process;

import java.util.ArrayList;

import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class Messages extends ArrayList<Message> {

	private static final long serialVersionUID = 1L;

	public Messages add(String e) {
		super.add(new TextMessage(e));
		return this;
	}

	public Messages toDefault() {
		clear();
		add("ロト6の予想をするロン！");
		return this;
	}

	public Messages toBug() {
		clear();
		add("ごめーんちょっと調子が悪いロン！");
		add("間を空けてもう一回メッセージをくれるロンか？");
		return this;
	}

}
