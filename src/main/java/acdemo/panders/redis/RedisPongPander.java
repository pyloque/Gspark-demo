package acdemo.panders.redis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ackern.core.Holder;
import ackern.core.background.Pander;
import ackern.core.redis.RedisStore;

@Singleton
public class RedisPongPander extends Pander<String> {

	@Inject
	@Named("hello")
	private RedisStore redis;

	public RedisPongPander() {
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
