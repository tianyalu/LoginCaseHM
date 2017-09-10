package com.sty.login.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/5/0005.
 */

public class UserInfoUtil {
    private static final String TAG = UserInfoUtil.class.getSimpleName();

    /**
     * 保存用户名密码到应用私有目录中
     * @param context
     * @param username
     * @param password
     * @return
     */
    public static boolean saveUserInfoToPrivateDir(Context context, String username, String password){

        String userinfo = username + "##" + password;
        //String path = "/data/data/com.sty.login/";

        //通过context对象获取私有目录的一个路径
        String path = context.getFilesDir().getPath(); // /data/user/0/com.sty.login/files
        Log.i(TAG, "private dir:-->" + path);
        File file = new File(path, "userinfo.txt");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(userinfo.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 保存用户名密码到外置存储SD卡中
     * @param username
     * @param password
     * @return
     */
    public static boolean saveUserInfoToSDCard(String username, String password){
        String userinfo = username + "##" + password;

        //通过context对象获取私有目录的一个路径
        String path = Environment.getExternalStorageDirectory().getPath(); // /storage/emulated/0
        Log.i(TAG, "SD card dir:-->" + path);
        File file = new File(path, "userinfo.txt");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(userinfo.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 保存用户名密码到应用私有目录中——Android简单模式
     * @param context
     * @param username
     * @param password
     * @return
     */
    public static boolean saveUserInfoToPrivateDirByAndroid(Context context, String username, String password){

        String userinfo = username + "##" + password;

        FileOutputStream fileOutputStream = null;
        try {
            //得到私有目录下一个文件写入流 name:私有目录文件的名称   mode:文件的操作模式-->私有，追加，全局读，全局写
            fileOutputStream = context.openFileOutput("userinfo.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(userinfo.getBytes()); //将用户名密码写入文件
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 保存用户名密码到SharedPreferences中
     * @param context
     * @param username
     * @param password
     * @return
     */
    public static boolean saveUserInfoToSharedPreferences(Context context, String username, String password){

        try {
            //1.通过Context对象创建一个SharedPreferences对象
            //name:SharedPreferences文件的名称， mode:文件的操作模式
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            //2.通过SharedPreferences对象获取一个Editor对象
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //3.往Editor对象中添加数据
            editor.putString("username", username);
            editor.putString("password", password);
            //4.提交Editor对象
            editor.commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 从应用私有目录中读取用户名密码
     * @param context
     * @return
     */
    public static Map<String, String> getUserInfoFromPrivateDir(Context context){
        //String path = "/data/data/com.sty.login/";
        String path = context.getFilesDir().getPath(); // /data/user/0/com.sty.login/files
        File file = new File(path, "userinfo.txt");

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            //读取一行中包含用户名密码，需要解析
            String readLine = bufferedReader.readLine();
            String[] split = readLine.split("##");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", split[0]);
            hashMap.put("password", split[1]);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 从外置存储SD卡中读取用户名密码
     * @return
     */
    public static Map<String, String> getUserInfoFromSDCard(){
        String path = Environment.getExternalStorageDirectory().getPath(); // /storage/emulated/0
        File file = new File(path, "userinfo.txt");

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            //读取一行中包含用户名密码，需要解析
            String readLine = bufferedReader.readLine();
            String[] split = readLine.split("##");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", split[0]);
            hashMap.put("password", split[1]);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 从应用私有目录中读取用户名密码--Android简单模式
     * @param context
     * @return
     */
    public static Map<String, String> getUserInfoFromPrivateDirByAndroid(Context context){

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = context.openFileInput("userinfo.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            //读取一行中包含用户名密码，需要解析
            String readLine = bufferedReader.readLine();
            String[] split = readLine.split("##");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", split[0]);
            hashMap.put("password", split[1]);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * 从SharedPreferences中读取用户名密码
     * @param context
     * @return
     */
    public static Map<String, String> getUserInfoFromSharedPreferences(Context context){
        try {
            //1.通过Context对象创建一个SharedPreferences对象
            SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            //2.通过SharedPreferences对象获取存放的数据
            //key:存放数据时的key defValue:默认值，根据业务需求来写
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", username);
            hashMap.put("password", password);
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
