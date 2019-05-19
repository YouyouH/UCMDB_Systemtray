package com.hyy;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * @author youyouhuang
 * @create 2019-05-17
 * @desc
 **/

public class CustomSSL {

    private static CloseableHttpClient httpClient;


    public synchronized static CloseableHttpClient acquireHttpClient() {
        if (getHttpClient() == null) {
            SSLConnectionSocketFactory sslsf = null;
            try {
                sslsf = getSslConnectionSocketFactory();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
        } else {
            return getHttpClient();
        }
    }

    public static void customSSL() throws Exception {
        // Trust own CA and all self-signed certs

        SSLConnectionSocketFactory sslsf = getSslConnectionSocketFactory();

        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build()) {

            HttpGet httpget = new HttpGet("https://localhost:8443/ping/");

            System.out.println("Executing request " + httpget.getRequestLine());

            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();

                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    String serverStatus = EntityUtils.toString(entity);
                    System.out.println(serverStatus);
                }
            }
        }
    }

    public static SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = new TrustManager[]{new DefaultTruststoreManger()};
        sslcontext.init(null, trustManagers, null);
        // Allow TLSv1 protocol only
        return new SSLConnectionSocketFactory(
                sslcontext,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    private static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    private void setHttpClient(CloseableHttpClient httpClient0) {
        httpClient = httpClient0;
    }


    public static class DefaultTruststoreManger implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


}
