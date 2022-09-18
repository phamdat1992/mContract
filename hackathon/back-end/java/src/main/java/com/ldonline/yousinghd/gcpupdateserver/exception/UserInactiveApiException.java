package com.ldonline.yousinghd.gcpupdateserver.exception;

import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;

public class UserInactiveApiException extends BaseApiException {


    public UserInactiveApiException() {
        this("InActive");
    }

    public UserInactiveApiException(String message) {
        super(message);
        EmptyApiResponse response = new EmptyApiResponse(BaseApiResponse.ERROR.USER_INACTIVE,
                new BaseApiResponse.Message()
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.VI, "Tai khoan het han")
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.EN, "Account in active"));
        this.response = response;
    }


}
