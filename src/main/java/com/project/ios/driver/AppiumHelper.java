package com.project.ios.driver;

import com.project.ios.steps.BaseSteps;
import com.project.ios.utils.PropertyUtil;
import cucumber.api.Scenario;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.net.UrlChecker;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class AppiumHelper extends PropertyUtil {

    final static Logger LOGGER = LogManager.getLogger(AppiumHelper.class);
    public static Scenario CURRENT_SCENARIO;
    public static boolean EXECUTE_TEST = true;
    private static IOSDriver<IOSElement> driver;
    public static IOSDriver<IOSElement> getDriver() { return driver; }
    private static File app;
    private static DesiredCapabilities CAPABILITIES;
    private static String platformVersion;
    private static boolean INITIALIZED = false;
    private static final String APPIUM_LOG_FILE_PATH = System.getProperty("user.dir") + "/appium.log";
    private static URL APPIUM_SERVER_URL;
    private static AppiumDriverLocalService appiumService;
    public static BaseSteps lastActionPage;

    private static void setCapabilities()
    {
        LOGGER.info("appPath :"+System.getProperty("appPath"));
        String apkPathFilename = System.getProperty("user.dir") + "/" + System.getProperty("appPath");
        app = new File(apkPathFilename);

        try
        {
            CAPABILITIES = new DesiredCapabilities();
            CAPABILITIES.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
            CAPABILITIES.setCapability("useJSONSource",true);
            CAPABILITIES.setCapability("sendKeyStrategy","setValue");

            Properties capProp = readCapabilitiesProperties();
            LOGGER.info("Loading capabilities from property file....");

            String value=null;
            for(Map.Entry<Object, Object> e : capProp.entrySet())
            {
                value= (String) e.getValue();
                if(value.contains("{"))
                {
                    value=value.replace('{',' ');
                    value=value.replace('}',' ');
                    value=System.getProperty(value.trim());
                }
                CAPABILITIES.setCapability((String) e.getKey(), value);
            }

            LOGGER.info("Desired capabilities info : "+CAPABILITIES.toString());
        }
        catch(Exception e)
        {
            LOGGER.error("Error occured while setting capabilities:"+e.fillInStackTrace());
        }
    }

    public static void loadDriver()
    {
        try
        {
            setCapabilities();
            driver = new IOSDriver<IOSElement>(APPIUM_SERVER_URL, CAPABILITIES);
        }
        catch(Exception e)
        {
            LOGGER.error("Unable to create instance of ios driver:"+e.fillInStackTrace());
            throw new RuntimeException("Unable to create instance of ios driver:"+e.fillInStackTrace());
        }
    }

    public static void saveScreenShot(String fileNameSufix, Scenario scenario)
    {
        if (getDriver() instanceof TakesScreenshot)
        {
            TakesScreenshot camera = (TakesScreenshot) getDriver();
            byte[] screenshot = camera.getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        }
    }
    public static void closeDriver()
    {
        try
        {
            if(driver==null)
            {
                LOGGER.info("Driver is already closed");
            }
            else
            {
                try
                {
                    saveScreenShot("", CURRENT_SCENARIO);
                }
                finally
                {
                    driver.quit();
                    LOGGER.info("Driver closed successfully.");
                    driver=null;
                }
            }
        }
        catch(Exception e)
        {
            LOGGER.error("Error occured while closing driver :"+e.getStackTrace());
        }
    }

    public static boolean startAppiumServer()
    {
        boolean serverOk = false;
        serverOk = startLocalServer();
        try {
            APPIUM_SERVER_URL = new URL(getAppiumServerUrl());
            serverOk = true;
        }
        catch (MalformedURLException e) {
            LOGGER.error("The APPIUM_SERVER_URL is malformed " + APPIUM_SERVER_URL.toString());
            AppiumHelper.EXECUTE_TEST = false;
        }
        catch (Exception e)
        {
            LOGGER.error("Appium server url problem: " + e.getMessage());
        }
        return serverOk;
    }

    /***
     * Starts the local Appium server
     */
    private static boolean startLocalServer() {
        boolean started = false;
        File appiumLogFile = new File(APPIUM_LOG_FILE_PATH);
        appiumService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().
                usingAnyFreePort().withLogFile(appiumLogFile));
        appiumService.start();


        try
        {
            APPIUM_SERVER_URL = new URL(getAppiumServerUrl());
            URL status = new URL(APPIUM_SERVER_URL + "/sessions");
            new UrlChecker().waitUntilAvailable(50, TimeUnit.SECONDS, status);
            LOGGER.info("Appium server started successfully at :"+getAppiumServerUrl());
            started = true;
        }
        catch (MalformedURLException e) {
            LOGGER.error("The APPIUM_SERVER_URL is malformed " + APPIUM_SERVER_URL.toString());
            AppiumHelper.EXECUTE_TEST = false;
        }
        catch (Exception e)
        {
            LOGGER.error("Appium server did not start within expected time!");
        }
        LOGGER.info("starting appium successfully");
        return started;
    }

    public static String  getAppiumServerUrl() throws Exception{
        LOGGER.info("Appium Server URL:"+String.format("%s/wd/hub",appiumService.getUrl().toString()));
        return appiumService.getUrl().toString();
    }

    public static void stopAppiumServer(){
        appiumService.stop();
    }

    public static void init() {
        if(!INITIALIZED) {
            try {
                INITIALIZED = true;
                loadPropertyFile();
                if(AppiumHelper.startAppiumServer()){
                    AppiumHelper.loadDriver();
                    LOGGER.info("Driver initialized successfully.");
                }else {
                    //stop execution for remaining test cases
                    AppiumHelper.EXECUTE_TEST=false;
                }
            } catch (Exception e) {
                LOGGER.error("Error while initializing driver:"+e.fillInStackTrace());
                LOGGER.error(String.valueOf(e.fillInStackTrace()));
                AppiumHelper.EXECUTE_TEST=false;
            }
        }
    }

    public static void setLastActionPage(Object lastActionPage) {
        if (BaseSteps.class.isInstance(lastActionPage))
        {
            LOGGER.info("Setting last action page: " + lastActionPage.getClass().getSimpleName());
            AppiumHelper.lastActionPage = (BaseSteps) lastActionPage;
        }
    }

    public static void goToHomePage() throws Exception {
        if (lastActionPage != null) {
            AppiumHelper.lastActionPage.goToHomePage();
        }
    }

}
