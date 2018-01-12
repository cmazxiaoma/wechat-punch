package com.cmazxiaoma.wechat.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HttpAPIService {

    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig requestConfig;

    //不带参数的GET请求
    public HttpResult doGet(String url) {
        return new HttpTemplate() {
            @Override
            public CloseableHttpClient getHttpClient() {
                return httpClient;
            }

            @Override
            public RequestConfig getRequestConfig() {
                return requestConfig;
            }

            @Override
            public HttpGet initHttpGetData(HttpGet oldHttpGet) throws Exception {
                return oldHttpGet;
            }

            @Override
            public HttpPost initHttpPostData(HttpPost oldHttpPost) throws Exception {
                return null;
            }

        }.execute(url, HttpTemplate.GET);
    }

    //带参数的GET请求
    public HttpResult doGet(final String url, final Map<String, Object> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
                }
            }

            //参数拼接好了，调用不带参数的get请求
            return this.doGet(uriBuilder.build().toString());
        } catch (Exception e) {
            log.error("构造有参数的GET请求url出错 = {}", e.getMessage());
        }

        return null;
    }

    //不带参数的POST请求
    public HttpResult doPost(String url) {
        return new HttpTemplate() {

            @Override
            public CloseableHttpClient getHttpClient() {
                return httpClient;
            }

            @Override
            public RequestConfig getRequestConfig() {
                return requestConfig;
            }

            @Override
            public HttpPost initHttpPostData(HttpPost oldHttpPost) {
                return oldHttpPost;
            }

            @Override
            public HttpGet initHttpGetData(HttpGet oldHttpGet) {
                return null;
            }
        }.execute(url, HttpTemplate.POST);
    }

    //发送JSON的POST请求
    public HttpResult doPost(String url, final String json) {
        return new HttpTemplate() {

            @Override
            public CloseableHttpClient getHttpClient() {
                return httpClient;
            }

            @Override
            public RequestConfig getRequestConfig() {
                return requestConfig;
            }

            @Override
            public HttpPost initHttpPostData(final HttpPost oldHttpPost) throws Exception {
                StringEntity stringEntity = new StringEntity(json);
                stringEntity.setContentType("UTF-8");
                stringEntity.setContentType("application/json");
                oldHttpPost.setEntity(stringEntity);

                //返回新的HttpPost
                return oldHttpPost;
            }

            @Override
            public HttpGet initHttpGetData(HttpGet oldHttpGet) {
                return null;
            }
        }.execute(url, HttpTemplate.POST);

    }

    //带参数的POST请求
    public HttpResult doPost(String url, final Map<String, Object> params) {
        return new HttpTemplate() {
            @Override
            public CloseableHttpClient getHttpClient() {
                return httpClient;
            }

            @Override
            public RequestConfig getRequestConfig() {
                return requestConfig;
            }

            @Override
            public HttpPost initHttpPostData(final HttpPost oldHttpPost) throws Exception {
                List<NameValuePair> list = new ArrayList<>();

                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }

                //构造form表单对象
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");

                //把表单放到POST请求
                oldHttpPost.setEntity(urlEncodedFormEntity);

                //返回新的HttpPost
                return oldHttpPost;
            }

            @Override
            public HttpGet initHttpGetData(HttpGet oldHttpGet) {
                return null;
            }
        }.execute(url, HttpTemplate.POST);
    }

}
