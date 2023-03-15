package io.github.changebooks.elasticsearch;

import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;

/**
 * 拦截ES请求和ES响应，打日志
 *
 * @author changebooks@qq.com
 */
public class EsLogCustomizer implements RestClientBuilderCustomizer {

    @Override
    public void customize(RestClientBuilder builder) {
    }

    @Override
    public void customize(HttpAsyncClientBuilder builder) {
        EsLogReqInterceptor reqInterceptor = reqInterceptor();
        if (reqInterceptor != null) {
            builder.addInterceptorLast(reqInterceptor);
        }

        EsLogRespInterceptor respInterceptor = respInterceptor();
        if (respInterceptor != null) {
            builder.addInterceptorLast(respInterceptor);
        }
    }

    /**
     * 拦截ES请求
     *
     * @return {@link EsLogReqInterceptor} 实例
     */
    public EsLogReqInterceptor reqInterceptor() {
        return new EsLogReqInterceptor();
    }

    /**
     * 拦截ES响应
     *
     * @return {@link EsLogRespInterceptor} 实例
     */
    public EsLogRespInterceptor respInterceptor() {
        return new EsLogRespInterceptor();
    }

}
