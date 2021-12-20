package org.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WebSpyder {
    private int sockTmOut;
    private int connTmOut;
    private CloseableHttpClient client;
    private CloseableHttpResponse response;

    public WebSpyder(String url) {
        this.sockTmOut = 1000;
        this.connTmOut = 500;
        this.client = HttpClients.createDefault();
        this.response = null;
        this.updateUrl(url);
    }

    public void updateUrl(String url) {
        HttpGet httpget = new HttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(this.sockTmOut)
                .setConnectTimeout(this.connTmOut)
                .build();
        httpget.setConfig(config);
        try {
            this.response = client.execute(httpget);
            System.out.println(this.response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getContent() {
        String content = "";
        if (this.response != null) {
            HttpEntity entity = this.response.getEntity();
            if (this.response.getStatusLine().getStatusCode() == 200) {
                try {
                    content = EntityUtils.toString(entity, "gbk");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
}
