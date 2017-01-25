package acdemo;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import acdemo.workers.redis.RedisPingWorker;
import acdemo.workers.redis.RedisPongWorker;
import gspark.core.AppConfig;
import gspark.core.background.Boss;

@Singleton
public class BossStarter {
	@Inject
	private Boss boss;
	@Inject
	private RedisPingWorker pingWorker;
	@Inject
	private RedisPongWorker pongWorker;

	public static void main(String[] args) {
		AppConfig config = AppConfig.load();
		Injector injector = Guice.createInjector(new DemoModule(config));
		BossStarter boss = injector.getInstance(BossStarter.class);
		boss.start();
	}

	public void start() {
		boss.worker(pingWorker).worker(pongWorker).start();
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				boss.stop();
			}

		});
	}

}
