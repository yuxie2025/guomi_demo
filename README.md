# guomi_demo 国密Demo

开源国密通信纯 Java JSSE 实现，国密https网站访问，安卓版，居于gm-jsse，
HttpsURLConnection协议访问国密网站，SM2，SM3，SM4

## 使用案例：
```
//本地测试链接  SM2单证书
public static final String nativeUrl = "https://192.168.10.35:9400/index.html";
//沃通 RAR和SM2双证书
public static final String ovsslUrl = "https://sm2test.ovssl.cn/";
//中国银行 SM2单证书
public static final String bocUrl = "https://ebssec.boc.cn/";
//GDCA 测试链接 RAR和SM2双证书
public static final String gdcaUrl = "https://gmssl.trustauth.cn/";

//更多国密测试链接,见：HttpsURLConnectionUtils
String url = HttpsURLConnectionUtils.gdcaUrl;
HttpsURLConnectionUtils.get(this, url, new RequestCallBack() {
      @Override
     public void onError(Exception e) {
     }

     @Override
     public void onFail(String msg) {
     }

     @Override
     public void onSuccess(String content) {
     }
});
```
[演示demo下载地址](https://github.com/yuxie2025/guomi_demo/blob/main/GmDemo_V1.0_20210803_1757.apk) 

### 或扫码下载演示demo:
![下载链接](https://github.com/yuxie2025/guomi_demo/blob/main/download_qr.png)

### 访问日志截图
![访问日志](https://github.com/yuxie2025/guomi_demo/blob/main/log.png)

### app主页截图
![app主页](https://github.com/yuxie2025/guomi_demo/blob/main/home.png)