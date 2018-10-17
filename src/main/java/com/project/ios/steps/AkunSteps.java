package com.project.ios.steps;

import com.project.ios.driver.AppiumHelper;
import org.junit.Assert;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class AkunSteps extends BaseSteps {

    public static void userOnAkunOnboarding(){
        Assert.assertTrue(isElementPresent("akun_onboarding_close_button"));
        Assert.assertTrue(isElementPresent("akun_onboarding_login_button"));
        Assert.assertTrue(isElementPresent("akun_onboarding_daftar_akun_button"));
        Assert.assertTrue(isElementPresent("akun_onboarding_menu_lain_button"));
        AppiumHelper.setLastActionPage(new AkunSteps());
    }

    public static void clickAkunMenu(){
        clickElementBy("home_akun_tab");
        AppiumHelper.setLastActionPage(new AkunSteps());
    }

    public void goToHomePage(){
        clickElementBy("akun_onboarding_close_button");
        AppiumHelper.setLastActionPage(new HomeSteps());
    }
}
