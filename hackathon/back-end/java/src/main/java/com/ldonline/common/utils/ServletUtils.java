package com.ldonline.common.utils;

import javax.servlet.http.HttpServletRequest;

public class ServletUtils {

    public static String getStringHeader(HttpServletRequest req, String key) {
        return getStringHeader(req, key, null);
    }

    public static String getStringHeader(HttpServletRequest req, String key, String defaultValue) {
        String rs = req.getHeader(key);
        if (rs == null) {
            return defaultValue;
        }
        return rs;
    }

    public static String getStringParam(HttpServletRequest req, String key) {
        return getStringParam(req, key, null);
    }

    public static String getStringParam(HttpServletRequest req, String key, String defaultValue) {
        String rs = req.getParameter(key);
        if (rs == null) {
            return defaultValue;
        }
        return rs;
    }

    public static Long getLongHeader(HttpServletRequest req, String key) {
        return getLongHeader(req, key, null);
    }

    public static Long getLongHeader(HttpServletRequest req, String key, Long defaultValue) {
        String rsString = req.getHeader(key);
        Long rs = null;
        if (!CommonUtils.isEmpty(rsString)) {
            rs = ConvertUtils.toLong(rsString);
            ;
        }
        if (rs == null) {
            return defaultValue;
        }
        return rs;
    }

    public static Long getLongParam(HttpServletRequest req, String key) {
        return getLongParam(req, key, null);
    }

    public static Long getLongParam(HttpServletRequest req, String key, Long defaultValue) {
        String rsString = req.getParameter(key);
        Long rs = null;
        if (!CommonUtils.isEmpty(rsString)) {
            rs = ConvertUtils.toLong(rsString);
            ;
        }
        if (rs == null) {
            return defaultValue;
        }
        return rs;
    }

    public static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURI().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
