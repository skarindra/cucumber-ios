package com.project.ios;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by sekarayukarindra on 17/10/18.
 */

@RunWith(Cucumber.class)
@CucumberOptions(
        strict = false,
        features={"classpath:features"},
        glue={"classpath:com/project/ios/stepDefinitions/","classpath:com/project/ios/driver/"},
        plugin = {"json:target/cucumber.json" }
)
public class TestRunner {
}
