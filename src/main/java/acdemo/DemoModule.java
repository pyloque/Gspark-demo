package acdemo;

import ackern.core.AppConfig;
import ackern.core.GuiceModule;

public class DemoModule extends GuiceModule {

	public DemoModule(AppConfig config) {
		super(config);
	}

	@Override
	public void onBindOk() {
		
	}

}
