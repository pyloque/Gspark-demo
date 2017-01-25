package acdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import acdemo.api.ErrorApi;
import acdemo.api.HelloApi;
import acdemo.ui.HelloUI;
import ackern.core.AppConfig;
import ackern.core.error.KernError;
import ackern.core.spark.SparkServer;
import ackern.core.view.ApiError;

@Singleton
public class WebStarter {
	private final static Logger LOG = LoggerFactory.getLogger(WebStarter.class);

	@Inject
	private SparkServer webServer;
	@Inject
	private HelloApi helloApi;
	@Inject
	private HelloUI helloUi;
	@Inject
	private ErrorApi errorApi;

	public static void main(String[] args) {
		AppConfig config = AppConfig.load();
		Injector injector = Guice.createInjector(new DemoModule(config));
		WebStarter app = injector.getInstance(WebStarter.class);
		app.start();
	}

	public void start() {
		webServer.start(spark -> {
			spark.path("/api/hello", () -> {
				helloApi.setup(spark);
			});
			spark.path("/ui/hello", () -> {
				helloUi.setup(spark);
			});
			spark.exception(ApiError.class, (exc, req, res) -> {
				ApiError error = (ApiError) exc;
				res.body(errorApi.renderError(error));
			});
			spark.exception(KernError.class, (exc, req, res) -> {
				LOG.error("api error in server", exc);
				ApiError error = ApiError.newServerError(exc.getMessage());
				res.body(errorApi.renderError(error));
			});
		});
	}

}
