package org.huberg.SpiderNet.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by admin on 2015/6/8.
 */
public class Tools {
    private static Pattern patternForProtocol = Pattern.compile("[\\w]+://");
    public static String removeProtocol(String url) {
        return patternForProtocol.matcher(url).replaceAll("");
    }
    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        int i = StringUtils.indexOf(domain, "/", 1);
        if(i > 0) {
            domain = StringUtils.substring(domain, 0, i);
        }
        return domain;
    }
    public static boolean validUrl(String url) {
        return true;
    }
}
