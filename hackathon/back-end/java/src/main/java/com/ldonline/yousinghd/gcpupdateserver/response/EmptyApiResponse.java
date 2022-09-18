package com.ldonline.yousinghd.gcpupdateserver.response;

public class EmptyApiResponse extends BaseApiResponse {

    public static final EmptyApiResponse DEFAULT = new EmptyApiResponse(ERROR.FAIL);

    public EmptyApiResponse(ERROR error) {
        super(error);
    }

    public EmptyApiResponse(ERROR error, Message message) {
        super(error, message);
    }
}
