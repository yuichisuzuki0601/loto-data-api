package jp.co.sbro.loto_data_api.process;

import java.util.List;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import jp.co.sbro.loto_data_api.util.Sleeper;

public class HateProcess implements ResponseProcess {

	@Override
	public List<Message> getMessages(MessageEvent<TextMessageContent> event) {
		Sleeper.sleep(5);
		Messages messages = new Messages();
		messages.add("うう。。悲しいロン。。");
		return messages;
	}

}
