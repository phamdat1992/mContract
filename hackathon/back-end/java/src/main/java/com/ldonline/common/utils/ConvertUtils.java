package com.ldonline.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ConvertUtils.class);

    public static Long toLong(String input){
        return toLong(input, null);
    }

    public static Long toLong(String input, Long defaultValue){
        try {
            return Long.parseLong(input);
        } catch (Exception e){
            LOG.error(LogUtils.buildTabLog(e.getMessage(), input), e);
        }
        return defaultValue;
    }

    public static Integer toInt(String input){
        return toInt(input, null);
    }

    public static Integer toInt(String input, Integer defaultValue){
        try {
            return Integer.parseInt(input);
        } catch (Exception e){
            LOG.error(LogUtils.buildTabLog(e.getMessage(), input), e);
        }
        return defaultValue;
    }
}
