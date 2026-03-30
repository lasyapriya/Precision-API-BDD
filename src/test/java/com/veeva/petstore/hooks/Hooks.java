package com.veeva.petstore.hooks;

import com.veeva.petstore.context.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);
    private final ScenarioContext context;

    public Hooks(ScenarioContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        log.info("========== START: {} ==========", scenario.getName());
        context.clear();
    }

    @After
    public void afterScenario(Scenario scenario) {
        log.info("========== END: {} | Status: {} ==========",
                scenario.getName(), scenario.getStatus());
    }
}
