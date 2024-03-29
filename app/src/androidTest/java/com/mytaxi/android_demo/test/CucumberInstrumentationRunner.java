package com.mytaxi.android_demo.test;

import android.os.Bundle;
import android.support.test.runner.MonitoringInstrumentation;
import cucumber.api.CucumberOptions;
import cucumber.api.android.CucumberInstrumentationCore;

@CucumberOptions(
        features = "features",
        glue = "com.mytaxi.android_demo.test")

public class CucumberInstrumentationRunner extends MonitoringInstrumentation {

    private final CucumberInstrumentationCore instrumentationCore = new CucumberInstrumentationCore(this);

    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        instrumentationCore.create(bundle);
        start();
    }

    @Override
    public void onStart() {
        waitForIdleSync();
        instrumentationCore.start();
    }
}
