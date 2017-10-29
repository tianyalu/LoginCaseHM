package com.sty.login.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sty.login.util.StreamUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shi Tianyi on 2017/10/29/0029.
 */

public class LoginHttpUtils {

    public static void requestNetForGetLogin(final Handler handler, final String username, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用URLConnection请求服务器，将用户名密码发送服务器验证
                try{
                    //1.创建一个URL对象
                    String urlGetStr = "http://192.168.1.8/newsServiceHM/servlet/LoginServlet" + "?username="
                            + username + "&pwd=" + password;
                    URL url = new URL(urlGetStr);
                    //2.通过Url对象获取一个HttpURLConnection对象
                    HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
                    //3.设置HttpURLConnection对象的一些参数，请求方式，连接超时时间
                    openConnection.setRequestMethod("GET");
                    openConnection.setConnectTimeout(10 * 1000);
                    //4.获取响应码，判断响应码是否为200
                    int responseCode = openConnection.getResponseCode();
                    if(responseCode == 200){
                        InputStream inputStream = openConnection.getInputStream();
                        String result = StreamUtils.streamToString(inputStream);
                        Log.i("Tag", result);
                        boolean isSuccess = false;
                        if(result.contains("success")){
                            isSuccess = true;
                        }

                        Message msg = Message.obtain();
                        msg.obj = isSuccess;
                        msg.what = 1; //指定message的code,在接收的时候可以据此判断来源是哪个message对象
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void requestNetForPostLogin(final Handler handler, final String username, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //使用URLConnection请求服务器，将用户名密码发送服务器验证
                try{
                    //1.创建一个URL对象
                    URL url = new URL("http://192.168.1.8/newsServiceHM/servlet/LoginServlet");
                    //2.通过Url对象获取一个HttpURLConnection对象
                    HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
                    //3.设置HttpURLConnection对象的一些参数，请求方式，连接超时时间
                    openConnection.setRequestMethod("POST");
                    openConnection.setConnectTimeout(10 * 1000);
                    //4.设置一些请求头的信息 field:http请求的请求头 newValue:请求头的值
                    String body = "username=" + username + "&pwd=" + password;
                    //openConnection.setRequestProperty("Content-length", body.length() + ""); //这个头信息可以被注释掉而没有影响
                    //openConnection.setRequestProperty("Cache-control", "max-age=0"); //这个头信息可以被注释掉而没有影响
                    //openConnection.setRequestProperty("Origin", "http://192.168.1.8"); //这个头信息可以被注释掉而没有影响
                    //5.设置HttpURLConnection可以写请求的内容
                    openConnection.setDoOutput(true);
                    //6.获取一个outputStream,并将内容写入该流
                    openConnection.getOutputStream().write(body.getBytes());
                    //6.获取响应码，判断响应码是否为200
                    int responseCode = openConnection.getResponseCode();
                    if(responseCode == 200){
                        InputStream inputStream = openConnection.getInputStream();
                        String result = StreamUtils.streamToString(inputStream);
                        Log.i("Tag", result);
                        boolean isSuccess = false;
                        if(result.contains("success")){
                            isSuccess = true;
                        }

                        Message msg = Message.obtain();
                        msg.obj = isSuccess;
                        msg.what = 2; //指定message的code,在接收的时候可以据此判断来源是哪个message对象
                        handler.sendMessage(msg);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
