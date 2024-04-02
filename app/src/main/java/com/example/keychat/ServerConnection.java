package com.example.keychat;

import android.util.Log;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ServerConnection {
    private static Socket socket = null;
    public static String serverURL;

    private ServerConnection() {
        IO.Options options = new IO.Options();
        TrustManager[] trustAllCerts= new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        } };

        HostnameVerifier myHostnameVerifier= new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            options.sslContext = sslContext;
            options.hostnameVerifier = myHostnameVerifier;
            socket = IO.socket(serverURL, options);
            socket.connect();
        } catch (Exception e) {
            Log.e("ERROR", "Connection Error", e);
            throw new RuntimeException();
        }
    }

    public static Socket getServerConnection() {
        if(socket == null){
            init();
        }
        return socket;
    }

    private static void init(){
        IO.Options options = new IO.Options();
        TrustManager[] trustAllCerts= new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        } };

        HostnameVerifier myHostnameVerifier= new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);
            options.sslContext = sslContext;
            options.hostnameVerifier = myHostnameVerifier;
            socket = IO.socket(serverURL, options);
            socket.connect();
        } catch (Exception e) {
            Log.e("ERROR", "Connection Error", e);
            throw new RuntimeException();
        }
    }

    public static void setServerURL(String url){
        serverURL = url;
        init();
    }
}
