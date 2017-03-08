package jp.co.sbro.loto_data_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jp.co.sbro.loto_data_api.task.SyncTask;

@SpringBootApplication
public class LotoDataApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LotoDataApiApplication.class, args);
		SyncTask.getInstance().start();
	}

}
