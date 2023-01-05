package com.yzy.lib_common.util;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * Created by yzy on 2021/2/24.
 */
public class HttpsUtils {

    public static SSLSocketFactory sSLSocketFactory;
    public static X509TrustManager trustManager;

    private static final String KEY_STORE_TYPE_BKS = "bks";
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";


    /**
     * 双向校验中SSLSocketFactory X509TrustManager 参数的生成
     *
     * @param application
     */
    public static void initSslSocketFactory(Application application) {
        try {

            String KEY_STORE_PASSWORD ="";
            String BKS_STORE_PASSWORD = "";
            String server = "";
            String client = "";

            InputStream bksStream = application.getAssets().open(server);//客户端信任的服务器端证书流
            InputStream p12Stream = application.getAssets().open(client);//服务器需要验证的客户端证书流

            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);

            try {
                trustStore.load(bksStream, BKS_STORE_PASSWORD.toCharArray());//加载客户端信任的服务器证书
                keyStore.load(p12Stream, KEY_STORE_PASSWORD.toCharArray());//加载服务器信任的客户端证书
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    bksStream.close();
                    p12Stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            trustManager = chooseTrustManager(trustManagerFactory.getTrustManagers());//生成用来校验服务器真实性的trustManager


            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());//生成服务器用来校验客户端真实性的KeyManager
            //初始化SSLContext
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            sSLSocketFactory = sslContext.getSocketFactory();//通过sslContext获取到SocketFactory

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 单向校验中,通过crt格式的证书生成SSLSocketFactory X509TrustManager 参数的生成
     * 通常在Android中，客户端用于校验服务器真实性的证书是支持BKS格式的，但是往往后台给的证书都是crt格式的
     * 当然我们可以自己生成BKS，但是想更方便一些我们也是可以直接使用crt格式的证书的
     *
     * @param application
     */
    public static void initSslSocketFactorySingleBuyCrt(Application application) {
        try {
            InputStream crtStream = application.getAssets().open("server.bks");//客户端信任的服务器端证书流

            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            Certificate ca = cf.generateCertificate(crtStream);
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore trustStore = KeyStore.getInstance(keyStoreType);

            try {
                trustStore.load(null, null);
                trustStore.setCertificateEntry("ca", ca);
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    crtStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            trustManager = chooseTrustManager(trustManagerFactory.getTrustManagers());//生成用来校验服务器真实性的trustManager

            SSLContext sslContext = SSLContext.getInstance("TLS", "AndroidOpenSSL");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            //初始化SSLContext
            sSLSocketFactory = sslContext.getSocketFactory();//通过sslContext获取到SocketFactory


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }





    //获取这个SSLSocketFactory
    public static SSLSocketFactory getTestSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTestTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    public static TrustManager[] getTestTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {


                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return trustAllCerts;
    }

    //获取HostnameVerifier
    public static HostnameVerifier getTestHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };
        return hostnameVerifier;
    }
}
