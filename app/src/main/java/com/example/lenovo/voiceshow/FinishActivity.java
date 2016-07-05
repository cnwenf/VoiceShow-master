package com.example.lenovo.voiceshow;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.util.EncodingUtils;

import bupt.sse.thread.sendMP4;
import config.PathConfig;

public class FinishActivity extends Activity implements View.OnClickListener {

    public String video_name=null;
    private VideoView video2;
    MediaController  mediaco;
    public String dataName = null;
    ImageButton cancle;
    ImageButton save;
    private String userName = null;
    //等待条
    private ProgressDialog dlgMixing;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what==0){
                //TODO 弹出对话框，提示登陆失败：用户名或密码错误
                System.out.println("上传失败！");
                dlgMixing.cancel();
                Toast.makeText(FinishActivity.this,"上传失败！",Toast.LENGTH_SHORT).show();
            }
            if(what==1){
                //TODO 弹出对话框，提示注册成功，用户点击确定后跳转到登陆页面
                System.out.println("上传成功！");
                dlgMixing.cancel();
                Toast.makeText(FinishActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
                Intent intent_save = new Intent();
                intent_save.setClass(FinishActivity.this,Person2Activity.class);
                startActivity(intent_save);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_page);
        new PathConfig();
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        dataName = bundle.getString("video_name");

        String fileName= PathConfig. APP_EXTERNAL_ROOT_PATH +"/"+ "用户" + ".txt";
        try {

            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            userName = EncodingUtils.getString(buffer, "UTF-8");
            System.out.println(userName);
            fin.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        video2=(VideoView)findViewById(R.id.video2);
        save=(ImageButton)findViewById(R.id.f_save);
        cancle=(ImageButton)findViewById(R.id.f_cancle);
        mediaco=new MediaController(this);

        File file=new File(PathConfig.DATA_PATH +"/"+ dataName+ "_out.mp4");
        if(file.exists()){
            //VideoView与MediaController进行关联
            video2.setVideoPath(file.getAbsolutePath());
            video2.setMediaController(mediaco);
            mediaco.setMediaPlayer(video2);
            //让VideiView获取焦点
            video2.requestFocus();
        }
        save.setOnClickListener(this);
        cancle.setOnClickListener(this);
        dlgMixing = new ProgressDialog(this);
        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgMixing.setCancelable(false);
        dlgMixing.setCanceledOnTouchOutside(false);
    }

    public void onClick(View source) {
        try
        {
            switch (source.getId())
            {
                // 取消按钮被单击
                case R.id.f_cancle:
                    File file = new File(PathConfig.DATA_PATH + "/" + dataName + "_out.mp4");
                    if(file.isFile()){
                        file.delete();
                    }
                    Intent intent_cancle = new Intent();
                    intent_cancle.setClass(FinishActivity.this,HotActivity.class);
                    startActivity(intent_cancle);
                    break;
                // 保存按钮被单击
                case R.id.f_save:
                    String fileName= PathConfig. APP_EXTERNAL_ROOT_PATH +"/"+ "用户" + ".txt";
                    File file2 = new File(fileName);
                    if(file2.exists()){
                        dlgMixing.setMessage("服务器拼命接收数据中...");
                        dlgMixing.show();
                        new sendMP4(handler,userName,dataName+"_out").start();
                    }
                    else{
                        Toast.makeText(FinishActivity.this,"您还没有登录哦",Toast.LENGTH_SHORT).show();
                        Intent intent_login = new Intent();
                        intent_login.setClass(FinishActivity.this,LoginActivity.class);
                        startActivity(intent_login);

                        break;
                    }
                    break;
                // 停止按钮被单击
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXX");
        }
    }
}
