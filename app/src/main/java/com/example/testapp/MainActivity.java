package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.gdca.gm.HttpsURLConnectionUtils;
import com.gdca.gm.RequestCallBack;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private TextView tvMessage;
    private AppCompatEditText etUrl;

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
        etUrl = findViewById(R.id.et_url);

        //更多国密测试链接,见：HttpsURLConnectionUtils
        String url = HttpsURLConnectionUtils.gdcaUrl;
        etUrl.setText(url);

        findViewById(R.id.tv_test).setOnClickListener(v -> {

            String urlStr = etUrl.getText().toString();
            if (TextUtils.isEmpty(urlStr)) {
                Toast.makeText(this, "请输入地址！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!urlStr.contains("https://")) {
                Toast.makeText(this, "请输入正确的https地址！", Toast.LENGTH_SHORT).show();
                return;
            }

            Runnable r = new Runnable() {
                public void run() {
                    test(urlStr);
                }
            };
            Thread s = new Thread(r);
            s.start();
        });
    }

    private void test(String url) {
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