package com.sty.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.sty.login.net.LoginAsyncHttpClientUtils;
import com.sty.login.net.LoginHttpClientUtils;
import com.sty.login.net.LoginHttpUtils;
import com.sty.login.util.UserInfoUtil;

import java.io.File;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;

    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_remember;
    private Button bt_login;

    private String username;
    private String password;
    private boolean isRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        initViews();
        rememberShowUser();
        setListeners();
    }

    private void initViews(){
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        cb_remember = (CheckBox) findViewById(R.id.cb_remember);
        bt_login = (Button) findViewById(R.id.bt_login);
    }

    private void setListeners(){
        bt_login.setOnClickListener(this);
    }

    //回显用户名密码
    private void rememberShowUser(){
        //Map<String, String> map = UserInfoUtil.getUserInfoFromPrivateDir(mContext);
        //Map<String, String> map = UserInfoUtil.getUserInfoFromSDCard();
        //Map<String, String> map = UserInfoUtil.getUserInfoFromPrivateDirByAndroid(mContext);
        Map<String, String> map = UserInfoUtil.getUserInfoFromSharedPreferences(mContext);
        if(map != null){
            String username = map.get("username");
            String password = map.get("password");
            et_username.setText(username);
            et_password.setText(password);

            cb_remember.setChecked(true);
        }

    }

    //创建一个Handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            boolean isSuccess = (boolean) msg.obj;
            switch (msg.what){
                case 1:
                    Log.i("Tag", "get 方式提交的请求"); //get方式
                    break;
                case 2:
                    Log.i("Tag", "post 方式提交的请求"); //post方式
                    break;
                default:
                    break;
            }
            if(isSuccess) {
                dealWithIsRememberInfo(isRemember, username, password);
            }else {
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void login(){
        //c.获取用户输入的用户名密码和是否记住密码
        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        isRemember = cb_remember.isChecked();

        //d.判断用户名密码是否为空，不为空请求服务器
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            Toast.makeText(mContext, "用户名密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //e.请求服务器 带着用户名密码请求服务器验证密码是否正确
        //HttpURLConnection方式
        //LoginHttpUtils.requestNetForGetLogin(handler, username, password);
        //LoginHttpUtils.requestNetForPostLogin(handler, username, password);

        //HttpClient方式
        //LoginHttpClientUtils.requestNetForGetLogin(handler, username, password);
        //LoginHttpClientUtils.requestNetForPostLogin(handler, username, password);

        //AsyncHttpClient方式
        LoginAsyncHttpClientUtils.requestNetForGetLogin(mContext, handler, username, password);
        //LoginAsyncHttpClientUtils.requestNetForPostLogin(mContext, handler, username, password);

    }

    private void dealWithIsRememberInfo(boolean isRemember, String username, String password){
        //f.判断是否记住密码，如果记住密码，将用户名密码保存到本地
        if(isRemember){
            //判断Sdcard状态是否正常
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //sdcard状态是没有挂载的情况
                Toast.makeText(mContext, "sdcard不存在或未挂载", Toast.LENGTH_SHORT).show();
                return;
            }

            //判断sdcard存储空间是否满足文件的存储要求
            File sdCardFileDir = Environment.getExternalStorageDirectory();//得到SD卡的目录作为一个文件对象
            long usableSpace = sdCardFileDir.getUsableSpace(); //获取文件目录对象剩余空间
            long totalSpace = sdCardFileDir.getTotalSpace();
            //将一个long类型的文件大小格式化成用户可以看懂的M,G字符串
            String usableSpaceStr = Formatter.formatFileSize(mContext, usableSpace);
            String totalSpaceStr = Formatter.formatFileSize(mContext, totalSpace);
            Log.i(TAG, "SD卡剩余空间：" + usableSpaceStr + "/" + totalSpaceStr);
            if(usableSpace < 1024 * 1024 * 200){ //判断剩余空间是否小于200M
                Toast.makeText(mContext, "SD卡剩余空间不足，剩余空间为" + usableSpaceStr + "/" + totalSpaceStr,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            //boolean result = UserInfoUtil.saveUserInfoToPrivateDir(mContext, username, password);
            //boolean result = UserInfoUtil.saveUserInfoToSDCard(username, password);
            //boolean result = UserInfoUtil.saveUserInfoToPrivateDirByAndroid(mContext, username, password);
            boolean result = UserInfoUtil.saveUserInfoToSharedPreferences(mContext, username, password);
            if(result){
                Toast.makeText(mContext, "用户名保存成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "用户名保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_login:
                login();
                break;
            default:
                break;
        }
    }
}
