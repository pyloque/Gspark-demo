package acdemo.panders.redis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ackern.core.Holder;
import ackern.core.background.Pander;
import ackern.core.redis.RedisStore;

@Singleton
public class RedisPingPander extends Pander<String> {

	@Inject
	@Named("hello")
	private RedisStore redis;

	public RedisPingPander() {
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
