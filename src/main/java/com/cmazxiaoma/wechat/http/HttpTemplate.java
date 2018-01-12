package com.cmazxiaoma.wechat.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public abstract class HttpTemplate implements HttpCallback {

    public static final String GET = "GET";

    public static final String POST = "POST";

    public final HttpResult execute(String url, String method) {
        CloseableHttpResponse response = null;

        if (method.equalsIgnoreCase(HttpTemplate.POST)) {
            HttpPost httpPost = new HttpPost(url);
            //装载配置
            httpPost.setConfig(getRequestConfig());
            try {
                HttpPost newHttpPost = initHttpPostData(httpPost);
                response = getHttpClient().execute(newHttpPost);
            } catch (Exception e) {
                log.info("执行POST请求失败 = {}", e.getMessage());
            }
        }

        if (method.equalsIgnoreCase(HttpTemplate.GET)) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(getRequestConfig());
            try {
                HttpGet newHttpGet = initHttpGetData(httpGet);
                response = getHttpClient().execute(newHttpGet);
            } catch (Exception e) {
                log.info("执行GET请求失败 = {}", e.getMessage());
            }
        }

        try {
            if (response.getStatusLine().getStatusCode() == 200) {
                return new HttpResult(200, EntityUtils.toString(response.getEntity(), "UTF-8"));
            }
        } catch (Exception e) {
            log.info("获取statusCode失败 = {}", e.getMessage());
        }

        return null;
    }
}
