package acdemo.ui;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import gspark.core.view.ViewBase;
import spark.Request;
import spark.Response;
import spark.Service;

@Singleton
public class HelloUI extends ViewBase {

	public String show(Request req, Response res) {
		String word = req.queryParams("word");
		Map<String, Object> context = new HashMap<>();
		context.put("word", word);
		return this.renderTemplate("index.html", context);
	}

	@Override
	public void setup(Service spark) {
		spark.get("/show", this::show);
	}

}
