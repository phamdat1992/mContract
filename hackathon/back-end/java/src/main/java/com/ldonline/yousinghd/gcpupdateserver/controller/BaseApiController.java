package com.ldonline.yousinghd.gcpupdateserver.controller;

import com.ldonline.yousinghd.gcpupdateserver.exception.BaseApiException;
import com.ldonline.yousinghd.gcpupdateserver.exception.DownloadApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseApiController {
    private static final Logger LOG = LoggerFactory.getLogger(BaseApiController.class);

    @ExceptionHandler(DownloadApiException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleDownloadException(DownloadApiException ex) {
        return ex.getResponse();
    }

    @ExceptionHandler(BaseApiException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(BaseApiException ex) {
        LOG.error(ex.getMessage(),ex);
        return ex.getResponse();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handleException(Exception ex) {
        LOG.error(ex.getMessage(),ex);
        return new BaseApiException("Unknown Exception!").getResponse();
    }

}
