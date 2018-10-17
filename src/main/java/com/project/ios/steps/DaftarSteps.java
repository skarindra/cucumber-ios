package com.project.ios.steps;

import com.project.ios.driver.AppiumHelper;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class DaftarSteps extends BaseSteps {

    public static void userOnDaftarPage(){
        verifyElementExist("daftar_page_title");
        verifyElementExist("daftar_name_text_field");
        verifyElementExist("daftar_email_text_field");
        AppiumHelper.setLastActionPage(new DaftarSteps());
    }

    public void goToHomePage(){
        clickElementBy("daftar_back_button");
        clickElementBy("akun_onboarding_close_button");
        AppiumHelper.setLastActionPage(new HomeSteps());
    }
}
