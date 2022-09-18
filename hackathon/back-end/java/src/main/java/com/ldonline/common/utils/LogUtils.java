package com.ldonline.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class LogUtils {
    protected static final Logger LOG = Logger.getLogger(LogUtils.class.getName());

    public static String buildTabLog(String message, Object... params){
        return tabLogBuilder().setMessage(message).addParam(params).build();
    }

    public static TabLogBuilder tabLogBuilder(){
        return new TabLogBuilder();
    }

    public static class TabLogBuilder {
        private String message;
        private List<Object> listParams = new ArrayList<>();

        public TabLogBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public TabLogBuilder addParam(Object... params){
            listParams.addAll(Arrays.asList(params));
            return this;
        }

        public String build(){
            StringBuilder sb = new StringBuilder();
            sb.append(Thread.currentThread().getStackTrace()[3].getMethodName());
            if (!CommonUtils.isEmpty(message)){
                sb.append("\t");
                sb.append(message);
            }
            for (int i = 0; i < listParams.size(); i++) {
                sb.append("\t");
                sb.append(listParams.get(i));
            }
            return sb.toString();
        }
    }
}
