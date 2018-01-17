package com.cmazxiaoma.wx.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

public interface HttpCallback {

    CloseableHttpClient getHttpClient();

    RequestConfig getRequestConfig();

    HttpPost initHttpPostData(HttpPost oldHttpPost) throws Exception;

    HttpGet initHttpGetData(HttpGet oldHttpGet) throws Exception;
}
