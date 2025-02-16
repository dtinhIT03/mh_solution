package org.example.mhcommon.config.http;

import lombok.SneakyThrows;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class OkHttpConfig {
    private static ConnectionPool connectionPool = new ConnectionPool(1000, 15, SECONDS);

    @SneakyThrows
    @Bean(name = "ignore-ssl")
    public OkHttpClient getClientIgnoreSSL() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
        newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
//        newBuilder.hostnameVerifier((hostname, session) -> true);
        return newBuilder.build();
    }

    @Bean(name = "normal")
    public OkHttpClient getClientNormal() {
        return new OkHttpClient.Builder().connectionPool(connectionPool).build();
    }
}
