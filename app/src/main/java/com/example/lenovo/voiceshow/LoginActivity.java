package com.example.lenovo.voiceshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import bupt.sse.thread.toRegOrLogin;
import config.PathConfig;

/**
 * Created by Administrator on 2016/5/12.
 */
public class LoginActivity extends AppCompatActivity {
    private ImageButton ib_back;//返回按钮
    private EditText et_zhanghao;  //账号输入框
    private EditText et_password;//密码输入框
    private Button button_login;   //登陆按钮
    private TextView tv_register;   //注册

    //等待条
    private ProgressDialog dlgMixing;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what==0){
                //TODO 弹出对话框，提示登陆失败：用户名或密码错误
                System.out.println("登陆失败！");
                dlgMixing.cancel();
                Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
            }
            if(what==1){
                //TODO 弹出对话框，提示注册成功，用户点击确定后跳转到登陆页面
                System.out.println("登陆成功！");

                dlgMixing.cancel();
                //用户名存到”用户.txt“中
                String file_name=PathConfig. APP_EXTERNAL_ROOT_PATH +"/"+ "用户" + ".txt";
                File file = new File(file_name);
                try {
                        //在指定的文件夹中创建文件
                        file.createNewFile();
                        System.out.println("创建文件成功");
                    } catch (Exception e) {
                    }
                try {
                   String is_login="1";
                    FileOutputStream fout = new FileOutputStream(file_name);
                    byte[] bytes = et_zhanghao.getText().toString().getBytes();
                    fout.write(bytes);
                    fout.flush();
                    fout.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,Person1Activity.class);
                startActivity(intent);
            }
            if(what==2){
                dlgMixing.cancel();
                Toast.makeText(LoginActivity.this,"发生未知错误，或许由于网络原因导致！",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        et_zhanghao = (EditText)findViewById(R.id.et_zhanghao);
        et_password = (EditText)findViewById(R.id.et_password);
        button_login = (Button)findViewById(R.id.button_login);
        tv_register = (TextView)findViewById(R.id.tv_register);
        ib_back.setOnClickListener(new OnClickListener() {
            @Override
            //返回页面
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,Person1Activity.class);
                startActivity(intent);
            }
        });

        tv_register.setOnClickListener(new OnClickListener() {
            @Override
            //进入注册页面
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //登陆事件
        button_login.setOnClickListener(new LoginListener());

        dlgMixing = new ProgressDialog(this);
        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgMixing.setCancelable(false);
        dlgMixing.setCanceledOnTouchOutside(false);
    }
    //登陆时的监听
    class  LoginListener implements OnClickListener{
        @Override
        public void onClick(View v){
            String userName = et_zhanghao.getText().toString();
            String password = et_password.getText().toString();
            if(userName.equals("")||password.equals("")){
                Toast.makeText(LoginActivity.this,"请输入用户名或密码！",Toast.LENGTH_SHORT).show();
                return;
            }
            dlgMixing.setMessage("拼命登陆中...");
            dlgMixing.show();
            new toRegOrLogin(userName,password,handler,1).start();
            /*
            登录时验证密码账号等过程，
             */
           /* Intent intent_login=new Intent(LoginActivity.this,Person1Activity.class);
            Bundle bundle=new Bundle();//用Bundle携带数据
            bundle.putInt("isLogin",1);//传递是否登陆参数到person页面
            bundle.putString("username",username);
            intent_login.putExtras(bundle);
            startActivity(intent_login);*/
        }
    }
}

