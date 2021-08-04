package com.gdca.gm;

import android.content.Context;
import android.util.Log;

import com.aliyun.gmsse.GMProvider;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * HttpsURLConnection网络请求工具类
 */
public class HttpsURLConnectionUtils {

    public static final String TAG = "HttpsURLConnectionUtils";

    /**
     * 本地测试链接  SM2单证书
     */
    public static final String nativeUrl = "https://192.168.10.35:9400/index.html";
    /**
     * 沃通 RSA和SM2双证书
     */
    public static final String ovsslUrl = "https://sm2test.ovssl.cn/";
    /**
     * 中国银行 SM2单证书
     */
    public static final String bocUrl = "https://ebssec.boc.cn/";
    /**
     * GDCA 测试链接 RSA和SM2双证书
     */
    public static final String gdcaUrl = "https://gmssl.trustauth.cn/";

    /**
     * 获取证书
     *
     * @param mContext
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(Context mContext) {
        try {
            // 初始化 SSLSocketFactory
            GMProvider provider = new GMProvider();
            SSLContext sc = SSLContext.getInstance("TLS", provider);

            BouncyCastleProvider bc = new BouncyCastleProvider();
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            CertificateFactory cf = CertificateFactory.getInstance("X.509", bc);

            InputStream is = mContext.getAssets().open("root.crt");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            ks.load(null, null);
            ks.setCertificateEntry("gmca", cert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509", provider);
            tmf.init(ks);

            sc.init(null, tmf.getTrustManagers(), null);
            return sc.getSocketFactory();
        } catch (Exception e) {
            return null;
        }
    }

    public static void get(Context mContext, String url, final RequestCallBack callBack) {
        get(mContext, url, new HashMap<>(), callBack);
    }

    /**
     * get请求
     *
     * @param mContext
     * @param url
     * @param headers
     * @param callBack
     */
    public static void get(Context mContext, String url, HashMap<String, String> headers, final RequestCallBack callBack) {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            URL serverUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            // 设置 SSLSocketFactory
            conn.setSSLSocketFactory(getSSLSocketFactory(mContext));

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
                Log.i(TAG, "header_key:" + entry.getKey() + ",value:" + entry.getValue());
            }

            conn.connect();
            Log.i(TAG, "used cipher suite:" + conn.getCipherSuite());
            Log.i(TAG, "url:" + url);
            String content = "";
            try {
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream in = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr);
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        content += line;
                    }
                    Log.i(TAG, "content:" + content);
                    callBack.onSuccess(content);
                } else {
                    String msg = "";
                    switch (code) {
                        case 400:
                            msg = "请求中有语法问题，或不能满足请求";
                            break;
                        case 404:
                            msg = "服务器找不到给定的资源";
                            break;
                        case 500:
                            msg = "服务器不能完成请求";
                            break;
                        default:
                            break;
                    }
                    if (callBack != null) {
                        callBack.onFail(msg);
                    }
                }
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onError(e);
            }
        }
    }

    /**
     * post请求
     *
     * @param mContext
     * @param url
     * @param body
     * @param headers
     * @param callBack
     */
    public static void post(Context mContext, String url, String body, HashMap<String, String> headers, final RequestCallBack callBack) {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            URL serverUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            // 设置 SSLSocketFactory
            conn.setSSLSocketFactory(getSSLSocketFactory(mContext));

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
                Log.i(TAG, "header_key:" + entry.getKey() + ",value:" + entry.getValue());
            }

            //加入数据
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            conn.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            conn.setDoInput(true);
            DataOutputStream out = new DataOutputStream(
                    conn.getOutputStream());
            if (body != null)
                out.writeBytes(body);
            out.flush();
            out.close();

            conn.connect();
            Log.i(TAG, "used cipher suite:" + conn.getCipherSuite());
            Log.i(TAG, "url:" + url);
            Log.i(TAG, "body:" + body);
            String content = "";
            try {
                int code = conn.getResponseCode();
                if (code == 200) {
                    InputStream in = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(isr);
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        content += line;
                    }
                    Log.i(TAG, "content:" + content);
                    callBack.onSuccess(content);
                } else {
                    String msg = "";
                    switch (code) {
                        case 400:
                            msg = "请求中有语法问题，或不能满足请求";
                            break;
                        case 404:
                            msg = "服务器找不到给定的资源";
                            break;
                        case 500:
                            msg = "服务器不能完成请求";
                            break;
                        default:
                            break;
                    }
                    if (callBack != null) {
                        callBack.onFail(msg);
                    }
                }
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callBack != null) {
                callBack.onError(e);
            }
        }
    }
}
