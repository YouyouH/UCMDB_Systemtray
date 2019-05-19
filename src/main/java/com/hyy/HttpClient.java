package com.hyy;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.hyy.CustomSSL.acquireHttpClient;

/**
 * @author youyouhuang
 * @create 2019-05-17
 * @desc
 **/

public class HttpClient {

    ExecutorService executorService = Executors.newFixedThreadPool(5);


    public static Consts.UcmdbStatus checkUcmdbStatus() {
        try (CloseableHttpClient httpClient = acquireHttpClient()) {
            HttpGet httpget = new HttpGet("https://localhost:8443/ping/");

            System.out.println("Executing request " + httpget.getRequestLine());

            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();

                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    String serverStatus = EntityUtils.toString(entity);
                    System.out.println(serverStatus);
                    if (Consts.UcmdbStatus.UP.getStatus().equals(serverStatus)) {
                        return Consts.UcmdbStatus.UP;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Consts.UcmdbStatus.DOWN;
    }

    public static Consts.Version checkUcmdbVersion() {
        try (CloseableHttpClient httpClient = acquireHttpClient()) {
            HttpGet httpget = new HttpGet("https://localhost:8443/ucmdb-api/connect");

            System.out.println("Executing request " + httpget.getRequestLine());

            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();

                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    String serverStatus = EntityUtils.toString(entity);
                    System.out.println(serverStatus);
                    Properties properties = new Properties();
                    properties.load(new StringReader(serverStatus));
                    System.out.println(properties.getProperty("ServerVersion"));
                    System.out.println(properties.getProperty("BuildNumber"));
                    return new Consts.Version(properties.getProperty("ServerVersion"), properties.getProperty("BuildNumber"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Consts.Version("Starting", "Starting");
    }
}
