package com.ldonline.yousinghd.gcpupdateserver.exception;

import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;

public class UserUnregisteredApiException extends BaseApiException {


    public UserUnregisteredApiException() {
        this("Unregistered");
    }

    public UserUnregisteredApiException(String message) {
        super(message);
        EmptyApiResponse response = new EmptyApiResponse(BaseApiResponse.ERROR.USER_UNREGISTER,
                new BaseApiResponse.Message()
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.VI, "Chưa đăng ký")
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.EN, "Unregistered"));
        this.response = response;
    }


}
