package com.ztbdz.web.util;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

@Slf4j
public class HttpClient {

    public static String sendPost(String url, String content) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.createDefault();
            int CONNECTION_TIMEOUT = 10 * 1000;
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(CONNECTION_TIMEOUT).build();
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-Type", "application/json");
            StringEntity requestEntity = new StringEntity(content, "UTF-8");
            httpPost.setEntity(requestEntity);
            httpResponse = httpClient.execute(httpPost, new BasicHttpContext());
            HttpEntity entity = httpResponse.getEntity();
            if(entity != null) {
                String resultStr = EntityUtils.toString(entity);
                return resultStr;
            }
        } catch(Exception e) {
            log.error("发送Post请求异常，原因："+e.getMessage(), e);
        } finally {
            close(httpResponse,null, httpPost, httpClient);
        }
        return "";
    }

    public static JSONObject sendGet(String url) {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse httpResponse = null;
        try {
            httpClient = HttpClients.createDefault();
            int CONNECTION_TIMEOUT = 10 * 1000;
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(CONNECTION_TIMEOUT).build();
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Content-Type", "application/json");
            httpResponse = httpClient.execute(httpGet, new BasicHttpContext());
            HttpEntity entity = httpResponse.getEntity();
            if(entity != null) {
                String resultStr = EntityUtils.toString(entity, "UTF-8");
                JSONObject result = JSONObject.fromObject(resultStr);
                return result;
            }
        } catch(Exception e) {
            log.error("发送Get请求异常，原因："+e.getMessage(), e);
        } finally {
            close(httpResponse, httpGet,null, httpClient);
        }
        return null;
    }

    private static void close(CloseableHttpResponse httpResponse, HttpGet httpGet, HttpPost httpPost, CloseableHttpClient httpClient){
            if(httpResponse != null) {
                try {
                    httpResponse.close();
                } catch(Exception e) {
                }
            }
            if(httpGet != null) {
                try {
                    httpGet.releaseConnection();
                } catch(Exception e) {
                }
            }
            if(httpPost != null) {
                try {
                    httpPost.releaseConnection();
                } catch(Exception e) {
                }
            }
            if(httpClient != null) {
                try {
                    httpClient.close();
                } catch(Exception e) {
                }
            }
    }
}
