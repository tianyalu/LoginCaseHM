package com.sty.login.net;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Shi Tianyi on 2017/10/31/0031.
 */

public class LoginAsyncHttpClientUtils {

    /**
     * 使用AsyncHttpClient请求服务器 get方式[主线程]
     * @param handler
     * @param username
     * @param password
     */
    public static void requestNetForGetLogin(final Context context, final Handler handler, final String username, final String password){
        String urlGetStr = "http://192.168.1.8/newsServiceHM/servlet/LoginServlet" + "?username="
                + URLEncoder.encode(username) + "&pwd=" + URLEncoder.encode(password);  //解决服务器接收客户端的中文乱码问题
        try{
            //创建一个AsyncHttpClient对象
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(urlGetStr, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    //statusCode:状态码   headers:头信息   responseBody:返回的内容，返回的实体
                    //判断状态码
                    if(statusCode == 200){
                        //获取结果
                        try {
                            String result = new String(responseBody, "utf-8");
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("Tag", "------------------------onFailure");
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 使用AsyncHttpClient请求服务器 post方式[主线程]
     * @param handler
     * @param username
     * @param password
     */
    public static void requestNetForPostLogin(final Context context, final Handler handler, final String username, final String password){
        String urlPostStr = "http://192.168.1.8/newsServiceHM/servlet/LoginServlet" ;

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("pwd", password);

        //url:url   params:请求时携带的参数信息   responseHandler:匿名内部类，接收成功或失败
        asyncHttpClient.post(urlPostStr, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //判断状态码
                if(statusCode == 200){
                    //获取结果
                    try {
                        String result = new String(responseBody, "utf-8");
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
