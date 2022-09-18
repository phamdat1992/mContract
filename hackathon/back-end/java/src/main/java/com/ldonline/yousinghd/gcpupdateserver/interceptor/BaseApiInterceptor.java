package com.ldonline.yousinghd.gcpupdateserver.interceptor;

import com.ldonline.common.utils.JsonUtils;
import com.ldonline.yousinghd.gcpupdateserver.exception.BaseApiException;
import com.ldonline.yousinghd.gcpupdateserver.response.BaseApiResponse;
import com.ldonline.yousinghd.gcpupdateserver.response.EmptyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseApiInterceptor implements HandlerInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(BaseApiInterceptor.class);

    private boolean responseData(HttpServletRequest request, HttpServletResponse response, BaseApiResponse data) {
        response.addHeader("Content-Type", "application/json;charset=utf-8");
        try {
            response.getWriter().write(JsonUtils.toString(data));
            response.getWriter().flush();
            response.getWriter().close();
            return true;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            return doPreHandle(request, response, handler);
        } catch (BaseApiException ex){
            LOG.error(ex.getMessage(), ex);
            response.setStatus(HttpStatus.OK.value());
            responseData(request, response, ex.getResponse());
        } catch (Exception ex){
            response.setStatus(HttpStatus.OK.value());
            LOG.error(ex.getMessage(), ex);
            responseData(request, response, EmptyApiResponse.DEFAULT);
        }
        return false;
    }

    protected abstract boolean doPreHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
