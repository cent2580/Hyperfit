package com.example.hyperfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends BaseActivity {
    private Socket socket;
    private TextView tv;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        
        connectSocket();
        tv = findViewById(R.id.tv_txt);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        connectSocket();
    }

//    启动一个activity
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, WebviewActivity.class);
        context.startActivity(intent);
    }

//    连接socket
    public void connectSocket() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
//            创建一个socket对象
                    socket = new Socket("192.168.8.119", 1989);
                    OutputStream os = socket.getOutputStream();
                    //写入要发送给服务器的数据
                    os.write("hahaha".toString().getBytes());
                    os.flush();
                    socket.shutdownOutput();
//            拿到服务器返回的数据
                    InputStream inputStream = socket.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String s = null;
                    final StringBuffer stringBuffer = new StringBuffer();
                    while ((s = bufferedReader.readLine()) != null) {
                        stringBuffer.append(s);
                        Log.d(TAG, "run:"+stringBuffer.toString());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (stringBuffer.toString().equals("123")) {
                                tv.setText(stringBuffer);
                                actionStart(MainActivity.this);
                            }
                        }
                    });
//                    关闭io资源
                    bufferedReader.close();
                    inputStreamReader.close();
                    inputStream.close();
                    socket.close();
                } catch (
                        UnknownHostException e) {
                    e.printStackTrace();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}
