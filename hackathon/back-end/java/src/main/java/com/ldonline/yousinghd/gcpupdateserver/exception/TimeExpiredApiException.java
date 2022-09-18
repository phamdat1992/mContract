package com.ldonline.yousinghd.gcpupdateserver.exception;

import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;

public class TimeExpiredApiException extends BaseApiException {

    public TimeExpiredApiException() {
        this("Timeout");
    }

    public TimeExpiredApiException(String message) {
        super(message);
        EmptyApiResponse response = new EmptyApiResponse(BaseApiResponse.ERROR.TIME_EXPIRED,
                new BaseApiResponse.Message()
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.VI, "Vuot qua thoi gian cho phep")
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.EN, "Timeout"));
        this.response = response;
    }


}
