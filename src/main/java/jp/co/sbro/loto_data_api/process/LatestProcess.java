package jp.co.sbro.loto_data_api.process;

import java.util.List;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.service.Reflection;
import jp.co.sbro.loto_data_api.task.SyncTask;

public class LatestProcess implements ResponseProcess {

	@Override
	public List<Message> getMessages(MessageEvent<TextMessageContent> event) {
		Messages messages = new Messages();
		try {
			SyncTask.getInstance().sync();
			List<String> ms = Reflection.reflect(WinResultTable.getInstance().select());
			for (String m : ms) {
				messages.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
			messages.toBug();
		}
		return messages;
	}

}
