package com.ldonline.yousinghd.gcpupdateserver.exception;

import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;

public class ForbiddenApiException extends BaseApiException {


    public ForbiddenApiException() {
        this("Forbidden");
    }

    public ForbiddenApiException(String message) {
        super(message);
        EmptyApiResponse response = new EmptyApiResponse(BaseApiResponse.ERROR.FORBIDDEN,
                new BaseApiResponse.Message()
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.VI, "Khong co quyen truy cap")
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.EN, "Access denied"));
        this.response = response;
    }


}
