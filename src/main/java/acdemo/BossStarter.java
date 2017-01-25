package acdemo;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import acdemo.panders.redis.RedisPingPander;
import acdemo.panders.redis.RedisPongPander;
import ackern.core.AppConfig;
import ackern.core.background.Boss;

@Singleton
public class BossStarter {
	@Inject
	private Boss boss;
	@Inject
	private RedisPingPander pingPander;
	@Inject
	private RedisPongPander pongPander;

	public static void main(String[] args) {
		AppConfig config = AppConfig.load();
		Injector injector = Guice.createInjector(new DemoModule(config));
		BossStarter boss = injector.getInstance(BossStarter.class);
		boss.start();
	}

	public void start() {
		boss.pander(pingPander).pander(pongPander).start();
		Runtime.getRuntime().addShutdownHook(new Thread() {

			public void run() {
				boss.stop();
			}

		});
	}

}
