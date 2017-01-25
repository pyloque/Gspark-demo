package acdemo;

import gspark.core.AppConfig;
import gspark.core.GuiceModule;

public class DemoModule extends GuiceModule {

	public DemoModule(AppConfig config) {
		super(config);
	}

	@Override
	public void onBindOk() {
		
	}

}
