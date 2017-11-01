package com.sty.login.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sty.login.util.StreamUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Shi Tianyi on 2017/10/30/0030.
 */

public class LoginHttpClientUtils {

    /**
     * 使用HttpClient请求服务器 get方式
     * @param handler
     * @param username
     * @param password
     */
    public static void requestNetForGetLogin(final Handler handler, final String username, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlGetStr = "http://192.168.1.8/newsServiceHM/servlet/LoginServlet" + "?username="
                        + URLEncoder.encode(username) + "&pwd=" + URLEncoder.encode(password);  //解决服务器接收客户端的中文乱码问题
                try {
                    //1.创建一个HttpClient对象
                    HttpClient httpClient = new DefaultHttpClient();
                    //2.设置请求方式
                    HttpGet httpGet = new HttpGet(urlGetStr);
                    //3.执行一个http请求,返回一个response对象
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    //4.获取请求的状态码，判断获取的内容
                    StatusLine statusLine = httpResponse.getStatusLine();
                    int code = statusLine.getStatusCode();

                    //5.判断状态码后获取内容
                    if(code == 200){
                        HttpEntity entity = httpResponse.getEntity(); //获取实体内容，其中封装的有http请求返回的流信息
                        InputStream inputStream = entity.getContent();
                        //6.将流信息转换成字符串
                        String result = StreamUtils.streamToString(inputStream);
                        Log.i("Tag", result);

                        boolean isSuccess = false;
                        if(result.contains("success")){
                            isSuccess = true;
                        }

                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = isSuccess;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 使用HttpClient请求服务器 post方式
     * @param handler
     * @param username
     * @param password
     */
    public static void requestNetForPostLogin(final Handler handler, final String username, final String password){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlPostStr = "http://192.168.1.8/newsServiceHM/servlet/LoginServlet" ;
                try {
                    //1.创建一个HttpClient对象
                    HttpClient httpClient = new DefaultHttpClient();
                    //2.设置请求方式
                    HttpPost httpPost = new HttpPost(urlPostStr);

                    //3.创建集合封装数据
                    ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
                    BasicNameValuePair nameValuePair = new BasicNameValuePair("username", username);  //此种方式不用编码了
                    arrayList.add(nameValuePair);
                    BasicNameValuePair pwdValuePair = new BasicNameValuePair("pwd", password);  //此种方式不用编码了
                    arrayList.add(pwdValuePair);
                    //4.创建一个entity
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(arrayList, "utf-8");
                    httpPost.setEntity(entity);

                    //5.执行一个http请求,返回一个response对象
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    //6.获取请求的状态码，判断获取的内容
                    StatusLine statusLine = httpResponse.getStatusLine();
                    int code = statusLine.getStatusCode();

                    //7.判断状态码后获取内容
                    if(code == 200){
                        HttpEntity httpEntity = httpResponse.getEntity(); //获取实体内容，其中封装的有http请求返回的流信息
                        InputStream inputStream = httpEntity.getContent();
                        //8.将流信息转换成字符串
                        String result = StreamUtils.streamToString(inputStream);
                        Log.i("Tag", result);

                        boolean isSuccess = false;
                        if(result.contains("success")){
                            isSuccess = true;
                        }

                        Message msg = Message.obtain();
                        msg.what = 2;
                        msg.obj = isSuccess;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
