package com.gdca.gm;


/**
 * 请求
 */
public interface RequestListener {
    void onError(Exception e);

    void onFail(String msg);

    void onSuccess(String content);
}
