package com.ldonline.yousinghd.gcpupdateserver.exception;

import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;

public class BaseApiException extends RuntimeException {
    protected BaseApiResponse response;

    public BaseApiException() {
        this("Unknown Message");
    }

    public BaseApiException(String errorMsg) {
        this(errorMsg, new EmptyApiResponse(BaseApiResponse.ERROR.FAIL, new BaseApiResponse.Message()
                .addLangMsg(BaseApiResponse.Message.LanguageCode.VI, "Đã có lỗi xảy ra, vui lòng thử lại sau!")
                .addLangMsg(BaseApiResponse.Message.LanguageCode.EN, "An error has occurred, please try again later!")));
    }

    public BaseApiException(String errorMsg, BaseApiResponse response) {
        super(errorMsg);
        this.response = response;
    }

    public BaseApiResponse getResponse() {
        return response;
    }
}
