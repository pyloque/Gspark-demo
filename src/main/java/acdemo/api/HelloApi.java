package acdemo.api;

import java.util.concurrent.ThreadLocalRandom;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ackern.core.Holder;
import ackern.core.redis.RedisStore;
import ackern.core.view.ApiError;
import ackern.core.view.ViewBase;
import spark.Request;
import spark.Response;
import spark.Service;

@Singleton
public class HelloApi extends ViewBase {

	@Inject
	@Named("hello")
	private RedisStore redis;

	public String say(Request req, Response res) {
		Holder<String> holder = new Holder<>();
		redis.execute(jedis -> {
			holder.set(jedis.get("hello"));
		});
		return this.renderOk(holder.value());
	}

	public String read(Request req, Response res) {
		redis.execute(jedis -> {
			jedis.set("hello", "" + ThreadLocalRandom.current().nextInt());
		});
		return this.renderOk("ok");
	}

	public Void error(Request req, Response res) {
		throw new ApiError(123, "wtf!");
	}

	public Void unknown(Request req, Response res) {
		throw new RuntimeException("wtf!");
	}

	public void setup(Service spark) {
		spark.get("/say", this::say);
		spark.get("/read", this::read);
		spark.get("/error", this::error);
		spark.get("/unknown", this::unknown);
	}

}
