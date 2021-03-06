package jp.co.sbro.loto_data_api.process;

import java.util.List;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import jp.co.sbro.loto_data_api.util.Sleeper;

public class DefaultProcess implements ResponseProcess {

	@Override
	public List<Message> getMessages(MessageEvent<TextMessageContent> event) {
		Sleeper.sleep(2);
		return new Messages().toDefault();
	}

}
