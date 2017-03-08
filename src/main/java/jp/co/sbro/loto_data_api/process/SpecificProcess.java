package jp.co.sbro.loto_data_api.process;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.service.Reflection;
import jp.co.sbro.loto_data_api.task.SyncTask;
import jp.co.sbro.loto_data_api.util.NumberUtil;

public class SpecificProcess implements ResponseProcess {

	public final static Set<String> que = new HashSet<>();

	@Override
	public List<Message> getMessages(MessageEvent<TextMessageContent> event) {
		String received = event.getMessage().getText();
		String userId = event.getSource().getUserId();
		Messages messages = new Messages();
		if (NumberUtil.hasNumber(received)) {
			try {
				SyncTask.getInstance().sync();
				int time = Integer.parseInt(received.replaceAll("[^0-9]", ""));
				List<String> ms = Reflection.reflect(WinResultTable.getInstance().select().filterByEnd(time));
				for (String m : ms) {
					messages.add(m);
				}
				que.remove(userId);
			} catch (Exception e) {
				e.printStackTrace();
				messages.toBug();
			}
		} else if (que.contains(userId)) {
			messages.add("指定回の結果は不要みたいロンね。");
			messages.add("他に聞きたいことはあるロンか？");
			que.remove(userId);
		} else {
			messages.add("第何回の結果が知りたいロンか？");
			messages.add("回数を数字だけ指定して送ってくれロン！");
			que.add(userId);
		}
		return messages;
	}

}
