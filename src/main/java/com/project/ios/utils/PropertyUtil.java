package com.project.ios.utils;

import com.project.ios.exceptions.ConfigurationException;
import io.appium.java_client.MobileBy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class PropertyUtil {

    final static Logger LOGGER = LogManager.getLogger(PropertyUtil.class);
    public static Properties PROPERTIES=null;
    private static final String LOCATOR_NOT_IN_PROP_FILE = "Locator type '%s' not defined in the prop file!!";
    private static final String LOCATOR_BAD_DEFINED = "Locator %s is BAD DEFINED !!";

    protected static Properties readCapabilitiesProperties() {
        String propertiesFileName="capabilities.properties";
        Properties properties = new Properties();
        try
        {
            FileInputStream Master = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/"+propertiesFileName);
            properties.load(Master);
            Master.close();
        }
        catch (IOException e)
        {
            throw new ConfigurationException(String.format("Cannot load CAPABILITIES properties file: %s",propertiesFileName), e, PropertyUtil.class.getName());
        }
        return properties;
    }

    public static void loadPropertyFile() {
        String propertiesFileName = "element.properties";
        String countryFile = System.getProperty("user.dir") + "/src/main/resources/" + propertiesFileName;
        if(PROPERTIES==null)
        {
            PROPERTIES = new Properties();
            try
            {
                FileInputStream Master = new FileInputStream(countryFile);
                PROPERTIES.load(Master);
                Master.close();
            }
            catch (IOException e)
            {
                throw new ConfigurationException(String.format("Cannot load ELEMENT properties file: %s", countryFile), e, PropertyUtil.class.getName());
            }
        }
    }

    public static String getPropertyValue(String key) {
        if(PROPERTIES.getProperty(key)==null)
        {
            throw new NoSuchFieldError("Property key not found in properties file:"+key);
        }
        return PROPERTIES.getProperty(key);
    }

    public static By getLocatorFromValue(String value) {
        if (!value.contains("_")){
            LOGGER.error(String.format(LOCATOR_BAD_DEFINED, value));
            throw new NoSuchElementException(String.format(LOCATOR_BAD_DEFINED, value));
        }
        String[] values = value.split("_");
        String locatorType = values[0];
        String locatorValue = null;
        locatorValue = value.substring(value.indexOf("_") + 1);

        if(locatorType.toLowerCase().equals("id"))
            return MobileBy.ByAccessibilityId.AccessibilityId(locatorValue);
        else if(locatorType.toLowerCase().equals("name"))
            return MobileBy.ByIosNsPredicate.iOSNsPredicateString("name == '"+locatorValue+"'");
        else if(locatorType.toLowerCase().equals("label"))
            return MobileBy.ByIosNsPredicate.iOSNsPredicateString("label == '"+locatorValue+"'");
        else if((locatorType.toLowerCase().equals("classname")) || (locatorType.toLowerCase().equals("class")))
            return By.className(locatorValue);
        else if((locatorType.toLowerCase().equals("labelcontain")))
            return MobileBy.ByIosNsPredicate.iOSNsPredicateString("label CONTAINS '"+locatorValue+"'");
        else if(locatorType.toLowerCase().equals("xpath"))
            return By.ByXPath.xpath(locatorValue);
        else
            throw new NoSuchElementException(String.format(LOCATOR_NOT_IN_PROP_FILE, locatorType));
    }

    public static By getLocator(String elementName) {
        String elementNameValue;
        try
        {
            elementNameValue = getPropertyValue(elementName);
            return getLocatorFromValue(elementNameValue);
        }
        catch(Exception e)
        {
            throw new NoSuchElementException(e.getMessage());
        }

    }

}
