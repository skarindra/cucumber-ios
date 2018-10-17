package com.project.ios.steps;

import com.project.ios.driver.AppiumHelper;
import com.project.ios.utils.PropertyUtil;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class BaseSteps extends PropertyUtil {

    final static Logger LOGGER = LogManager.getLogger(BaseSteps.class);
    private static IOSDriver<IOSElement> driver = AppiumHelper.getDriver();
    private static int MAX_SCROLL=5;

    public static IOSElement waitUntilObjectIsThere(By elementBy, int attempt)
    {
        IOSElement element = null;
        if(driver!=null)
        {
            for(int i=1;i<=attempt;i++)
            {
                try
                {
                    element=driver.findElement(elementBy);
                    if(element!=null)
                    {
                        LOGGER.info("Element present on screen, No need to scroll:"+elementBy);
                        return element;
                    }
                    else {
                        if ((i+1)<=attempt){
                            Thread.sleep(5000);
                        }
                    }
                }
                catch(Exception e)
                {
                    LOGGER.warn("Element not found in attempt:"+(i+1)+", Retrying for:"+elementBy.toString());
                    try {
                        if ((i+1)<=attempt){
                            Thread.sleep(5000);
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        else
        {
            LOGGER.error("Driver is not initialized or its closed now.");
            throw new WebDriverException("Driver is not initialized or its closed now.");
        }

        return element;
    }

    public static boolean isElementPresent(By by) {
        boolean elementIsPresent = false;
        IOSElement element = waitUntilObjectIsThere(by,2);
        if(element!=null){
            elementIsPresent = true;
        }
        return elementIsPresent;
    }

    public static boolean isElementPresent(String locator) {
        boolean elementIsPresent = false;
        By by = getLocator(locator);
        IOSElement element = waitUntilObjectIsThere(by,2);
        if(element!=null){
            elementIsPresent = true;
        }
        return elementIsPresent;
    }

    public static void waitForVisibilityOf(By locator){
        int timeout = 10;
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        Assert.assertTrue(isElementPresent(locator));
    }

    public static void waitForVisibilityOf(String locator){
        int timeout = 10;
        By by = getLocator(locator);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        Assert.assertTrue(isElementPresent(by));
    }

    public static void clickElementBy(String element){
        try
        {
            By by = getLocator(element);
            waitForVisibilityOf(by);
            driver.findElement(by).click();
            LOGGER.info("Click action performed on:"+by.toString());
        }
        catch(Exception e)
        {
            throw new RuntimeException("Unable to perform click on:"+element+"\n Exception Details:"+e.fillInStackTrace());
        }
    }

    public static void allowPopup(){
        if(isElementPresent("home_allow_button")){
            clickElementBy("home_allow_button");
        }
    }

    public static void inputValue(String locator, String value){
        By by = getLocator(locator);
        IOSElement element = waitUntilObjectIsThere(by, 2);
        try {
            element.clear();
            element.sendKeys(value);
            LOGGER.info("Value entered successfully for:" + (element.getText() == null ? element.toString() : element.getText() + ", as :" + value));
        }catch (Exception e){
            LOGGER.error("Error occured while performing Enter value operation on:"+ element.toString());
            throw new NoSuchElementException("Unable to perform Enter value action as element not present on screen:"+e.fillInStackTrace());
        }
    }

    public static void verifyElementExist(String locator){
        Assert.assertTrue("Element is not present!", isElementPresent(locator));
        //Assert.assertTrue("Element is not present!",isElementPresentWithScroll(locator));
    }

    public static void verifyElementNotExist(String locator){
        Assert.assertFalse("Element is present!", isElementPresent(locator));
    }

    /**
     * This function is used to scroll down
     */
    private static void scrollingDown(){
        Dimension size = driver.manage().window().getSize();

        int y0 = (int) (size.height * 0.8);
        int y1 = (int) (size.height * 0.3);
        int x = (size.width / 2);

        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(x, y0)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(x, y1)).release().perform();
    }

    /**
     * This function is used to scroll up
     */
    private static void scrollingUp(){
        Dimension size = driver.manage().window().getSize();

        int y0 = (int) (size.height * 0.4);
        int y1 = (int) (size.height * 0.8);
        int x = (size.width / 2);

        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(x, y0)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(x, y1)).release().perform();
    }

    /**
     * To check if element present or not, if not it will scroll down
     * @param elementName
     */
    public static boolean scrollDownToElement(String elementName){
        for(int i=0; i< MAX_SCROLL; i++){
            if(isElementPresent(elementName)){
                return true;
            }else{
                scrollingDown();
            }
        }
        return false;
    }

    /**
     * To check if element present or not, if not it will scroll up
     * @param elementName
     */
    public static boolean scrollUpToElement(String elementName){
        for(int i=0; i< MAX_SCROLL; i++){
            if(isElementPresent(elementName)){
                return true;
            }else{
                scrollingUp();
            }
        }
        return false;
    }

    public static boolean isElementPresentWithScroll(String element){
        if(!isElementPresent(element)){
            if(!scrollUpToElement(element)){
                if(!scrollDownToElement(element)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This function is used to scroll to the right with custom percentage
     * @param startPercentage percentage to determine the start point to be tapped (must be higher)
     * @param endPercentage percentage to determine the end point to be tapped (must be lower)
     * @param percentageY percentage to determine the y point
     */
    public void swipeRight(double startPercentage, double endPercentage, double percentageY) {
        Dimension size = driver.manage().window().getSize();

        int x0 = (int) (size.width * startPercentage);
        int x1 = (int) (size.width * endPercentage);
        int y = (int) (size.height * percentageY);

        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(x0, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(x1, y)).release().perform();
    }

    /**
     * This function is used to scroll to the left with custom percentage
     * @param startPercentage percentage to determine the start point to be tapped (must be lower)
     * @param endPercentage percentage to determine the end point to be tapped (must be higher)
     * @param percentageY percentage to determine the y point
     */
    protected void swipeLeft(double startPercentage, double endPercentage, double percentageY) {
        Dimension size = driver.manage().window().getSize();

        int x0 = (int) (size.width * startPercentage);
        int x1 = (int) (size.width * endPercentage);
        int y = (int) (size.height * percentageY);

        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(x0, y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(x1, y)).release().perform();
    }

    public void goToHomePage() throws Exception{

    }
}
