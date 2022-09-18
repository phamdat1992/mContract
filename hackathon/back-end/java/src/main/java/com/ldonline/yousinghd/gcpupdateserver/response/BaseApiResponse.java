package com.ldonline.yousinghd.gcpupdateserver.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseApiResponse<T> {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private int error;
    private Message message = null;
    private T data = null;

    public BaseApiResponse(ERROR error) {
        this.error = error.getValue();
    }

    public BaseApiResponse(ERROR error, Message message) {
        this.error = error.getValue();
        this.message = message;
    }

    public BaseApiResponse(ERROR error, Message message, T data) {
        this.error = error.getValue();
        this.message = message;
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public Message getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static class Message {
        private Map<String, String> languageMsg = new HashMap<>();

        public Message addLangMsg(LanguageCode language, String message){
            this.languageMsg.put(language.name().toLowerCase(),message);
            return this;
        }

        public static enum LanguageCode {
            VI,EN
        }

        @JsonAnyGetter
        public Map<String, String> getLanguageMsg() {
            return languageMsg;
        }
    }

    public static enum ERROR {
        SUCCESS(0), FAIL(-1), FORBIDDEN(-2), USER_INACTIVE(-3),
        TIME_EXPIRED(-4), USER_UNREGISTER(-5), INPUT_INVALID(-6),
        DOWNLOAD_FAIL(-7),
        ;
        private int value;
        ERROR(int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }
    }
}
