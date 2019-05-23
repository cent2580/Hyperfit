package com.example.hyperfit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.hyperfit.tools.ActivityCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebviewActivity extends BaseActivity {
    private Socket socket;
    private WebView webView;
    private static final String TAG = "WebviewActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ActivityCollector.addActivity(this);

        webView = findViewById(R.id.wv_video);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.baidu.com");

        connectSocket();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectSocket();
    }

    public void connectSocket() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
//            创建一个socket对象
                    socket = new Socket("192.168.8.119", 1989);
//                    OutputStream outputStream = socket.getOutputStream();
////            写入要发送给服务器的数据
//                    outputStream.write("hahaha".toString().getBytes());
//                    outputStream.flush();
                    socket.shutdownOutput();
//            拿到服务器返回的数据
                    InputStream inputStream = socket.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String s = null;
                    final StringBuffer stringBuffer = new StringBuffer();
                    while ((s = bufferedReader.readLine()) != null) {
                        stringBuffer.append(s);
                        Log.d(TAG, "run: "+stringBuffer.toString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
//            关闭io资源
                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();
//                    outputStream.close();
                    socket.close();
                } catch (
                        UnknownHostException e) {
                    e.printStackTrace();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
