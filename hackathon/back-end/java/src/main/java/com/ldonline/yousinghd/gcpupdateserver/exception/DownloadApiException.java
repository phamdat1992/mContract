package com.ldonline.yousinghd.gcpupdateserver.exception;

import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;

public class DownloadApiException extends BaseApiException {


    public DownloadApiException() {
        this("Fail");
    }

    public DownloadApiException(String message) {
        super(message);
        EmptyApiResponse response = new EmptyApiResponse(BaseApiResponse.ERROR.DOWNLOAD_FAIL,
                new BaseApiResponse.Message()
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.VI, "Tải về thất bại!")
                        .addLangMsg(BaseApiResponse.Message.LanguageCode.EN, "Download failed"));
        this.response = response;
    }


}
