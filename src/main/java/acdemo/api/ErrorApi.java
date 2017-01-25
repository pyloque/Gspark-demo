package acdemo.api;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.inject.Singleton;

import ackern.core.view.ApiError;

@Singleton
public class ErrorApi {

	public String renderError(ApiError error) {
		Map<String, Object> result = new HashMap<>();
		result.put("code", error.code());
		result.put("msg", error.getMessage());
		result.put("body", "");
		return JSON.toJSONString(result);
	}

}
