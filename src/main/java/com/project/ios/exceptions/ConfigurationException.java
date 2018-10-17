package com.project.ios.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sekarayukarindra on 17/10/18.
 */
public class ConfigurationException extends RuntimeException {

    final static Logger LOGGER = LogManager.getLogger(ConfigurationException.class);

    public ConfigurationException(String customMessage, Exception e, String className) {

        if (e != null) {
            LOGGER.error(String.format("Class name: %s.\n\tCustom message: %s.\n\tExcepetion message: %s", className, customMessage, e.getMessage()));
        } else {
            LOGGER.error(String.format("Class name: %s.\n\tCustom message: %s.", className, customMessage));

        }
    }
}
