package com.veeva.petstore.runners;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = "cucumber.plugin", value =
        "pretty, html:target/cucumber-reports/cucumber.html, " +
        "json:target/cucumber-reports/cucumber.json")
@ConfigurationParameter(key = "cucumber.glue", value = "com.veeva.petstore")
@ConfigurationParameter(key = "cucumber.publish.quiet", value = "true")
public class TestRunner {
    // This class is intentionally empty.
    // JUnit Platform Suite runs all feature files via annotations above.
}
