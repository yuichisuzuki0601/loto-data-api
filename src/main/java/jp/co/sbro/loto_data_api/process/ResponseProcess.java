package jp.co.sbro.loto_data_api.process;

import java.util.List;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

public interface ResponseProcess {

	public List<Message> getMessages(MessageEvent<TextMessageContent> event);

}
