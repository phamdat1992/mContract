package com.ldonline.yousinghd.gcpupdateserver.exception;

import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;

public class InputInvalidApiException extends BaseApiException {

    public InputInvalidApiException() {
        this("InputInvalid");
    }

    public InputInvalidApiException(String message) {
        super(message);
        EmptyApiResponse response = new EmptyApiResponse(BaseApiResponse.ERROR.INPUT_INVALID,
                new BaseApiResponse.Message()
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.VI, "Tham số không hợp lệ.")
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.EN, "InputInvalid"));
        this.response = response;
    }


}
