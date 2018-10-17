package com.project.ios.steps;

import com.project.ios.driver.AppiumHelper;
import org.junit.Assert;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class LoginSteps extends BaseSteps {

    public static void userOnLoginPage(){
        Assert.assertTrue(isElementPresent("login_back_button"));
        Assert.assertTrue(isElementPresent("login_email_text_field"));
        Assert.assertTrue(isElementPresent("login_password_pwd_field"));
        Assert.assertTrue(isElementPresent("login_login_button"));
        AppiumHelper.setLastActionPage(new LoginSteps());
    }

    public void goToHomePage(){
        clickElementBy("login_back_button");
        clickElementBy("akun_onboarding_close_button");
        AppiumHelper.setLastActionPage(new HomeSteps());
    }
}
