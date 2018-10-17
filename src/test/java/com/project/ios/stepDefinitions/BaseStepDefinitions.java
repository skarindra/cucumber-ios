package com.project.ios.stepDefinitions;

import com.project.ios.steps.*;
import com.project.ios.utils.ChoiceSelector;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class BaseStepDefinitions {

    final static Logger LOGGER = LogManager.getLogger(BaseStepDefinitions.class);

    @Given("^the user open apps$")
    public void theUserOpenApps() throws Throwable {

    }

    @Given("^user is in \"([^\"]*)\" page$")
    public void theUserIsInPage(String arg0) throws Throwable {
        ChoiceSelector.of(arg0)
                .when("home",() -> HomeSteps.userOnHome())
                .when("akun_onboarding",() -> AkunSteps.userOnAkunOnboarding())
                .when("login",() -> LoginSteps.userOnLoginPage())
                .when("daftar",() -> DaftarSteps.userOnDaftarPage())
                .orElse(() -> LOGGER.info("Your screen is not implemented yet: "+arg0));
    }


    @And("^user click \"([^\"]*)\" element$")
    public void theUserClickElement(String arg0) throws Throwable {
        BaseSteps.clickElementBy(arg0);
    }

    @When("^user navigate to \"([^\"]*)\" page$")
    public void theUserNavigateToPage(String arg0) throws Throwable {
        ChoiceSelector.of(arg0)
                .when("akun",() -> AkunSteps.clickAkunMenu())
                .orElse(() -> LOGGER.info("Your page is not implemented yet: "+arg0));
    }

    @And("^user input \"([^\"]*)\" field with \"([^\"]*)\" text$")
    public void theUserInputFieldWithText(String arg0, String arg1) throws Throwable {
        BaseSteps.inputValue(arg0,arg1);
    }

    @And("^user verify \"([^\"]*)\" exist$")
    public void theUserVerifyExist(String arg0) throws Throwable {
        BaseSteps.verifyElementExist(arg0);
    }

    @And("^user verify \"([^\"]*)\" does not exist$")
    public void theUserVerifyDoesNotExist(String arg0) throws Throwable {
        BaseSteps.verifyElementNotExist(arg0);
    }

}
