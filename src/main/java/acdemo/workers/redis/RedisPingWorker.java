package acdemo.workers.redis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import gspark.core.Holder;
import gspark.core.background.Worker;
import gspark.core.redis.RedisStore;

@Singleton
public class RedisPingWorker extends Worker<String> {

	@Inject
	@Named("hello")
	private RedisStore redis;

	public RedisPingWorker() {
		super(5);
	}

	@Override
	public void beforeStart() {
		redis.execute(jedis -> {
			jedis.rpush("pong", "ping ping ping");
		});
	}

	@Override
	public String take() {
		Holder<String> holder = new Holder<>();
		redis.execute(jedis -> {
			holder.set(jedis.lpop("ping"));
		});
		return holder.value();
	}

	@Override
	public void process(String t) {
		System.out.printf("ping:%s\n", t);
		redis.execute(jedis -> {
			jedis.rpush("pong", "ping ping ping");
		});
	}

	@Override
	public void idle() {
		System.out.println("ping:idle");
	}

}
