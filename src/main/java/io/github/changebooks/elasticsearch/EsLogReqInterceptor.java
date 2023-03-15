package io.github.changebooks.elasticsearch;

import org.apache.http.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 拦截ES请求，打日志
 *
 * @author changebooks@qq.com
 */
public class EsLogReqInterceptor implements HttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsLogReqInterceptor.class);

    @Override
    public void process(HttpRequest request, HttpContext context) {
        if (disabled()) {
            return;
        }

        if (request == null) {
            LOGGER.error("request can't be null");
            return;
        }

        RequestLine requestLine = request.getRequestLine();
        if (requestLine == null) {
            LOGGER.error("requestLine can't be null");
            return;
        }

        String method = requestLine.getMethod();
        String uri = requestLine.getUri();
        if (request instanceof HttpEntityEnclosingRequest) {
            log(method, uri, (HttpEntityEnclosingRequest) request, context);
        } else {
            info(method, uri, null, context);
        }
    }

    /**
     * 打日志
     *
     * @param method  请求方法
     * @param uri     请求链接
     * @param request 请求实例
     * @param context http上下文
     */
    public void log(String method, String uri, HttpEntityEnclosingRequest request, HttpContext context) {
        try {
            String entity = readRequest(request);
            info(method, uri, entity, context);
        } catch (IOException ex) {
            LOGGER.error("request, readRequest failed, method: {}, uri: {}, throwable: ", method, uri, ex);
        }
    }

    /**
     * 打日志
     *
     * @param method  请求方法
     * @param uri     请求链接
     * @param entity  请求实例
     * @param context http上下文
     */
    public void info(String method, String uri, String entity, HttpContext context) {
        LOGGER.info("request, method: {}, uri: {}, entity: {}", method, uri, entity);
    }

    /**
     * 读请求内容
     *
     * @param request 请求实例
     * @return 请求内容
     * @throws IOException 读失败
     */
    public String readRequest(HttpEntityEnclosingRequest request) throws IOException {
        if (request == null) {
            return null;
        }

        HttpEntity entity = request.getEntity();
        if (entity == null) {
            return null;
        } else {
            return EntityUtils.toString(entity, charset());
        }
    }

    /**
     * 字符编码
     *
     * @return 默认，UTF-8
     */
    public Charset charset() {
        return Consts.UTF_8;
    }

    /**
     * 关闭日志？
     *
     * @return 默认，开启日志
     */
    public boolean disabled() {
        return false;
    }

}
