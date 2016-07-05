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
import android.widget.Toast;

import bupt.sse.thread.toRegOrLogin;

/**
 * Created by Administrator on 2016/5/12.
 */
public class RegisterActivity extends AppCompatActivity {
    //返回按钮
    private ImageButton ib_back;
    //账号输入框
    private EditText et_zhanghao;
    //密码输入框
    private EditText et_password;
    //重复密码按钮
    private EditText et_password2;
    //注册按钮
    private Button button_register;
    //等待条
    private ProgressDialog dlgMixing;
    //消息监听器
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what==0){
                //TODO 弹出对话框，提示注册失败：该用户名已被注册
                System.out.println("注册失败！");
                dlgMixing.cancel();
                Toast.makeText(RegisterActivity.this,"该用户名已被注册!",Toast.LENGTH_SHORT).show();
            }
            if(what==1){
                //TODO 弹出对话框，提示注册成功，用户点击确定后跳转到登陆页面
                System.out.println("注册成功！");
                dlgMixing.cancel();
                Toast.makeText(RegisterActivity.this,"注册成功!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            if(what==2){
                dlgMixing.cancel();
                Toast.makeText(RegisterActivity.this,"发生未知错误，或许由于网络原因导致！",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        et_zhanghao = (EditText)findViewById(R.id.et_zhanghao);
        et_password = (EditText)findViewById(R.id.et_password);
        et_password2 = (EditText)findViewById(R.id.et_password2);
        button_register = (Button) findViewById(R.id.button_register);
        ib_back.setOnClickListener(new OnClickListener() {
            @Override
            //返回登陆页面
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        //登陆事件
        button_register.setOnClickListener(new RegisterListener());

        dlgMixing = new ProgressDialog(this);
        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgMixing.setCancelable(false);
        dlgMixing.setCanceledOnTouchOutside(false);
    }
    //登陆时的监听
    class  RegisterListener implements OnClickListener{
        @Override
        public void onClick(View v){
            String userName = et_zhanghao.getText().toString();
            String password = et_password.getText().toString();
            String password2 = et_password2.getText().toString();
            System.out.println(userName.equals(""));
            if(userName.equals("")||password.equals("")||password2.equals("")){
                System.out.println("填写完所有信息");
                Toast.makeText(RegisterActivity.this,"请填写完所有信息！",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(password2)){
                System.out.println("两次输入的密码不同！");
                Toast.makeText(RegisterActivity.this,"两次输入的密码不同！",Toast.LENGTH_SHORT).show();
                return;
            }
            dlgMixing.setMessage("拼命注册中...");
            dlgMixing.show();
            new toRegOrLogin(userName,password,handler,0).start();
            /*
            注册时验证密码账号等过程
             */
        }
    }
}

