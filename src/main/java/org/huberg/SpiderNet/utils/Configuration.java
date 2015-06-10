package org.huberg.SpiderNet.utils;

import java.util.ResourceBundle;

/**
 * Created by admin on 2015/6/8.
 */
public class Configuration {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("configuration");
    private static String[] args = {""};
    private static String getValue(String key) {
        if(resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            return null;
        }
    }
}
