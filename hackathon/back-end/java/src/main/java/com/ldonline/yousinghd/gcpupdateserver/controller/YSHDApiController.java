package com.ldonline.yousinghd.gcpupdateserver.controller;

import com.google.api.gax.paging.Page;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ldonline.common.utils.CommonUtils;
import com.ldonline.common.utils.ConvertUtils;
import com.ldonline.common.utils.EncryptUtils;
import com.ldonline.common.utils.LogUtils;
import com.ldonline.yousinghd.gcpupdateserver.common.AppConstant;
import com.ldonline.yousinghd.gcpupdateserver.exception.BaseApiException;
import com.ldonline.yousinghd.gcpupdateserver.exception.DownloadApiException;
import com.ldonline.yousinghd.gcpupdateserver.exception.ForbiddenApiException;
import com.ldonline.yousinghd.gcpupdateserver.model.entities.SongEntity;
import com.ldonline.yousinghd.gcpupdateserver.model.entities.UserEntity;
import com.ldonline.yousinghd.gcpupdateserver.response.AppInfoApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.SongListApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.service.SongService;
import com.ldonline.yousinghd.gcpupdateserver.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class YSHDApiController extends BaseApiController {
    private static final Logger LOG = LoggerFactory.getLogger(YSHDApiController.class);

    public static final boolean SERVE_USING_BLOBSTORE_API = false;
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static final Storage STORAGE = StorageOptions.getDefaultInstance().getService();

    @Value("${gcp.storage.bucketName}")
    private String gcsBucketName;

    @Autowired
    private SongService songService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/3.0/public/dl/app-release/{appName}", method = RequestMethod.GET)
    public void downloadV3(@PathVariable String appName, @RequestParam(name = "file_name") String fileName,
                           final HttpServletRequest request, final HttpServletResponse response) {
        try {
            String filePath = "data/apk/" + appName + "/" + fileName;
            ReadChannel reader = STORAGE.get(gcsBucketName).get(filePath).reader();
            InputStream inputStream = Channels.newInputStream(reader);
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new DownloadApiException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/3.0/public/check/app-release/{appName}")
    @ResponseBody
    public AppInfoApiResponse checkAppVersion(@PathVariable String appName,
                                              @RequestParam(name = "app_version") int appVersion,
                                              final HttpServletRequest request, final HttpServletResponse response) {
        Storage.BlobListOption listOptions = Storage.BlobListOption.prefix("data/apk/" + appName + "/apk");
        Page<Blob> listBlob = STORAGE.get(gcsBucketName).list(listOptions);
        if (listBlob == null) {
            return new AppInfoApiResponse();
        }
        List<String> listApkName = new ArrayList<>();
        for (Blob currentBlob : listBlob.iterateAll()) {
            listApkName.add(currentBlob.getName());
        }
        if (listApkName.isEmpty()) {
            return new AppInfoApiResponse();
        }

        listApkName.sort((o1, o2) -> o2.compareTo(o1));

        String latestApkFileName = FilenameUtils.getName(listApkName.get(0));
        String latestApkName = FilenameUtils.getBaseName(latestApkFileName);
        if (CommonUtils.isEmpty(latestApkName)) {
            return new AppInfoApiResponse();
        }

        String[] split = latestApkName.split("-");
        if (split.length < 4) {
            return new AppInfoApiResponse();
        }

        int latestVersion = ConvertUtils.toInt(split[1], 0);
        if (appVersion >= latestVersion) {
            return new AppInfoApiResponse();
        }
        String latestVersionName = split[2];

        int isForceUpdate = ConvertUtils.toInt(split[3], 0);

        AppInfoApiResponse.AppInfo appInfo = new AppInfoApiResponse.AppInfo();
        appInfo.latestVersion = latestVersion;
        appInfo.isForceUpdate = isForceUpdate;
        appInfo.latestVersionName = latestVersionName;
        appInfo.fileName = latestApkFileName;
        return new AppInfoApiResponse(appInfo);
    }

    @RequestMapping(value = "/3.0/auth/dl/song-release/{songId}")
    public void download(@RequestAttribute UserEntity userEntity,
                         @PathVariable int songId,
                         final HttpServletRequest request,
                         final HttpServletResponse response) throws Exception {
        try {
            songId = (songId % 1000000) + 1000000;
            String strSongId = String.valueOf(songId);
            strSongId = strSongId.substring(strSongId.length() - 6, strSongId.length());
            String folderName = strSongId.substring(0, 3);
            String fileName = strSongId.substring(3, 6);
            fileName = "c" + fileName + ".mkv";

            String filePath = "data/hdmk/" + folderName + "/" + fileName;
            LOG.info(filePath);
            ReadChannel reader = STORAGE.get(gcsBucketName).get(filePath).reader();
            InputStream inputStream = Channels.newInputStream(reader);
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new DownloadApiException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/3.0/auth/check/song-release")
    @ResponseBody
    public SongListApiResponse checkSongRelease(@RequestAttribute UserEntity userEntity, @RequestParam(name = "sversion") int sVersion, final HttpServletRequest request,
                                                final HttpServletResponse response) {
        LogUtils.TabLogBuilder logBuilder = LogUtils.tabLogBuilder();
        logBuilder.addParam(userEntity, sVersion);
        LOG.info(logBuilder.setMessage("Input").build());
        List<SongEntity> listSongUpdate = songService.getListSongUpdate(sVersion, userEntity.type);
        return new SongListApiResponse(BaseApiResponse.ERROR.SUCCESS, null, listSongUpdate);
    }

    @RequestMapping(value = "/3.0/test/build-sig")
    @ResponseBody
    public Map<String, Object> buildSig(
            @RequestParam String url, @RequestParam(name = "device_id") String deviceId,
            @RequestParam(required = false, defaultValue = "0") long timestamp,
            final HttpServletRequest request, final HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        result.put("deviceId", deviceId);
        UserEntity userByDeviceId = userService.getUserByDeviceId(deviceId);
        String sessionId = userByDeviceId.sessionId;
        result.put("sessionId", sessionId);
        timestamp = timestamp == 0 ? System.currentTimeMillis() : timestamp;
        result.put("timestamp", timestamp);
        String dataSig = url + deviceId + sessionId + timestamp;
        LOG.info(dataSig);
        LOG.info(AppConstant.API_PRIVATE_KEY);
        String serverSig = EncryptUtils.genSHA1(dataSig, AppConstant.API_PRIVATE_KEY);
        result.put("signature", serverSig);
        LOG.info(serverSig);
        return result;
    }

    //////////////////////////////////// test /////////////////////////////////////
    @RequestMapping(value = "/test/get/listsong")
    @ResponseBody
    public List<SongEntity> testGetListSong(final HttpServletRequest request,
                                            final HttpServletResponse response) {
        List<SongEntity> listSongUpdate = songService.getListSongUpdate(0, "-vn");
        return listSongUpdate;
    }

    @RequestMapping("/1.0/auth/test/interceptor")
    @ResponseBody
    public String testInterceptor() throws Exception {
        LOG.info("Hello Interceptor");
        return "Hello Interceptor";
    }

    @RequestMapping("/test/forbidden")
    public String forbidden() throws Exception {
        throw new ForbiddenApiException("Truy cập bị từ chối");
    }


    @RequestMapping("/test/apiexception")
    public String apiException() throws Exception {
        throw new BaseApiException("Hello!!!");
    }

    @RequestMapping("/test/exception")
    public String exception() throws Exception {
        throw new Exception();
    }
}
