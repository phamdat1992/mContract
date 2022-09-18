package com.ldonline.yousinghd.gcpupdateserver.response;

public class AppInfoApiResponse extends BaseApiResponse<AppInfoApiResponse.AppInfo> {

    public AppInfoApiResponse() {
        this(ERROR.SUCCESS);
    }

    public AppInfoApiResponse(ERROR error) {
        this(error, null);
    }

    public AppInfoApiResponse(ERROR error, Message message) {
        this(error, message, null);
    }

    public AppInfoApiResponse(AppInfoApiResponse.AppInfo data) {
        this(ERROR.SUCCESS, new Message()
                        .addLangMsg(Message.LanguageCode.VI, "Đã có phiên bản mới, hãy cập nhật để trải nghiệm tốt hơn.")
                        .addLangMsg(Message.LanguageCode.EN, "Đã có phiên bản mới, hãy cập nhật để trải nghiệm tốt hơn.")
                , data);
    }

    public AppInfoApiResponse(ERROR error, Message message, AppInfoApiResponse.AppInfo data) {
        super(error, message, data);
    }

    public static class AppInfo {
        public int latestVersion;
        public String latestVersionName;
        public int isForceUpdate;
        public String fileName;

    }

}
