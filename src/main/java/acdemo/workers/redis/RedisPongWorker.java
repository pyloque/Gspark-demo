package acdemo.workers.redis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import gspark.core.Holder;
import gspark.core.background.Worker;
import gspark.core.redis.RedisStore;

@Singleton
public class RedisPongWorker extends Worker<String> {

	@Inject
	@Named("hello")
	private RedisStore redis;

	public RedisPongWorker() {
		super(5);
	}

	@Override
	public void beforeStart() {

	}

	@Override
	public String take() {
		Holder<String> holder = new Holder<>();
		redis.execute(jedis -> {
			holder.set(jedis.lpop("pong"));
		});
		return holder.value();
	}

	@Override
	public void process(String t) {
		System.out.printf("pong:%s\n", t);
		redis.execute(jedis -> {
			jedis.rpush("ping", "pong pong pong");
		});
	}

	@Override
	public void idle() {
		System.out.println("pong:idle");
	}

}
