package com.project.ios.steps;

import com.project.ios.driver.AppiumHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class HomeSteps extends BaseSteps {

    final static Logger LOGGER = LogManager.getLogger(HomeSteps.class);

    public static void userOnHome(){
        try {
            AppiumHelper.goToHomePage();
        } catch (Exception e) {
            LOGGER.info("Unable to perform goToHome.");
            LOGGER.error(e.fillInStackTrace());
            throw new RuntimeException("Unable to perform goToHome.");
        }
        allowPopup();
        verifyElementExist("home_kategori_tab");
        verifyElementExist("home_inspirasi_tab");
        AppiumHelper.setLastActionPage(new HomeSteps());
    }

    public void goToHomePage(){
        LOGGER.info("On Home page..");
    }
}
