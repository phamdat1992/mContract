package com.ldonline.yousinghd.gcpupdateserver.controller;

import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ldonline.yousinghd.gcpupdateserver.GcpUpdateServerApplication;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.logging.Logger;

@Controller
public class YSHDDownloadController {

    private static final Logger LOG = Logger.getLogger(GcpUpdateServerApplication.class.getName());
    public static final boolean SERVE_USING_BLOBSTORE_API = false;
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static final Storage STORAGE = StorageOptions.getDefaultInstance().getService();

    @Value("${gcp.storage.bucketName}")
    private String gcsBucketName;

    @RequestMapping(value = "/gcs/download", method = RequestMethod.GET)
    @ResponseBody
    public void downloadV3(@RequestParam final String filePath, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        LOG.info(gcsBucketName);
        LOG.info(filePath);
        ReadChannel reader = STORAGE.get(gcsBucketName).get(filePath).reader();
        InputStream inputStream = Channels.newInputStream(reader);
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
