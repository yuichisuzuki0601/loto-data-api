package jp.co.sbro.loto_data_api.task;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import jp.co.sbro.loto_data.data.Scraper;
import jp.co.sbro.loto_data_api.db.WinResultTable;
import jp.co.sbro.loto_data_api.util.LotoDataDateTimeFormat;
import jp.co.sbro.loto_data_api.util.MilliSecondUtil;

public final class SyncTask extends TimerTask {

	private final static DateFormat DF = new LotoDataDateTimeFormat();

	private static SyncTask instance;

	public static SyncTask getInstance() {
		if (instance == null) {
			instance = new SyncTask();
		}
		return instance;
	}

	public synchronized void sync() throws Exception {
		WinResultTable table = WinResultTable.getInstance() ;
		int fromTime = table.selectLatestTime() + 1;
		table.insert(new Scraper(fromTime).getWinResultRepository());
		System.out.println(DF.format(new Date()) + "  synced!!");
	}

	private Timer timer = new Timer();

	private SyncTask() {
	}

	public void start() {
		// 毎日20時30分に起動
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 20);
		c.set(Calendar.MINUTE, 30);
		c.set(Calendar.SECOND, 00);
		timer.schedule(this, c.getTime(), MilliSecondUtil.DAY(1));
		System.out.println(DF.format(this.scheduledExecutionTime()) + " <- sync task scheduled!!");
	}

	@Override
	public void run() {
		try {
			sync();
		} catch (Exception e) {
		}
	}

	// 手動で1度だけsyncする
	public static void main(String[] args) throws Exception {
		new SyncTask().sync();
		System.exit(0);
	}

}
