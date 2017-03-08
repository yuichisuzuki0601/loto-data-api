package jp.co.sbro.loto_data_api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import jp.co.sbro.loto_data_api.process.ResponseProcess;
import jp.co.sbro.loto_data_api.process.ResponseProcessFactory;

@LineMessageHandler
public class LineHandler {

	@Autowired
	private LineMessagingService lms;

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
		System.out.println("event: " + event);
		BotApiResponse response = reply(event);
		System.out.println("Sent messages: " + response);
	}

	@EventMapping
	public void defaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

	private BotApiResponse reply(MessageEvent<TextMessageContent> event) throws IOException {
		ResponseProcess proc = ResponseProcessFactory.getResponseProcess(event);
		return lms.replyMessage(new ReplyMessage(event.getReplyToken(), proc.getMessages(event))).execute().body();
	}

}
