package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.gdca.gm.HttpsURLConnectionUtils;
import com.gdca.gm.RequestCallBack;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private TextView tvMessage;

    private void showMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvMessage.setText(Html.fromHtml(msg));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessage = findViewById(R.id.tv_message);

        findViewById(R.id.tv_test).setOnClickListener(v -> {
            Runnable r = new Runnable() {
                public void run() {
                    test();
                }
            };
            Thread s = new Thread(r);
            s.start();
        });
    }

    private void test() {
        //更多国密测试链接,见：HttpsURLConnectionUtils
        String url = HttpsURLConnectionUtils.gdcaUrl;
        showMsg("");
        HttpsURLConnectionUtils.get(this, url, new RequestCallBack() {
            @Override
            public void onError(Exception e) {
                showMsg(e.getMessage());
            }

            @Override
            public void onFail(String msg) {
                showMsg(msg);
            }

            @Override
            public void onSuccess(String content) {
                showMsg(content);
            }
        });
    }
}