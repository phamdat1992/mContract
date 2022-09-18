package com.ldonline.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

//    private static final Gson GSON = new Gson();
//
//    public static <T> String toString(T object){
//        if (object == null){
//            return "{}";
//        }
//        return GSON.toJson(object);
//    }

    public static <T> String toString(T object) {
        if (object == null) {
            return "{}";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return "{}";
        }
    }

}
