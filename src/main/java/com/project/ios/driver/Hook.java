package com.project.ios.driver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assume;
import org.junit.AssumptionViolatedException;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class Hook {

    final static Logger LOGGER = LogManager.getLogger(Hook.class);

    @Before(order = 1)
    public static void beforeScenario(Scenario scenario) throws AssumptionViolatedException
    {
        LOGGER.info("********* Automation execution started for scenario:"+scenario.getName());
        AppiumHelper.CURRENT_SCENARIO=scenario;
        try
        {
            Assume.assumeTrue(AppiumHelper.EXECUTE_TEST);
        }
        catch(Exception e)
        {
            AppiumHelper.closeDriver();
            throw new AssumptionViolatedException("Rest of the test cases will not be executed due to assumption violated:"+e.fillInStackTrace());
        }
    }
    @After
    public void afterScenario(Scenario scenario) throws Exception
    {
        if(scenario.isFailed())
        {
            LOGGER.error("********* Automation execution error for scenario:"+scenario.getName());
            AppiumHelper.saveScreenShot(scenario.getName().replace(' ', '_'), scenario);
        } else {
            LOGGER.info("********* Automation execution completed successfully for scenario:"+scenario.getName());
        }
    }

    @Before(order = 0)
    public static void init() {
        AppiumHelper.init();
    }
}
