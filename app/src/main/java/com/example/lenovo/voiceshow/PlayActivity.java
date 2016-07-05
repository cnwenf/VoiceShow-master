package com.example.lenovo.voiceshow;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;

import bupt.sse.thread.addGood;
import bupt.sse.thread.getMP4;
import bupt.sse.thread.toRegOrLogin;
import config.PathConfig;

public class PlayActivity extends Activity {
    private VideoView video1;
    private List<Map<String, Object>> mData;
    MediaController mediaco;
    public String dataName = null;
    private ImageButton play_voice;
    private TextView play_name;
    private TextView prise_num;//点赞数
    private ImageButton play_prise;//点赞按钮

    private Integer comment_num;//评论数
    public  List<String> comment_name;
    public  List<String> comment_time;
    public  List<String> comment_content;

    private EditText comment_enter;//评论输入
    private Button comment_send;

    //注意：以下两个变量应该由上一个界面传值过来
    private String defName = "冰雪奇缘1";
    private String userName = "wf";
    private String num = "10";
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what==0){
                Toast.makeText(PlayActivity.this,"点赞失败，数据错误！",Toast.LENGTH_SHORT).show();
            }
            if(what==1){
                Toast.makeText(PlayActivity.this,"点赞+1！",Toast.LENGTH_SHORT).show();
                Bundle b = msg.getData();
                prise_num.setText(b.getString("num"));
            }
        }
    };
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_page);
        new PathConfig();
        Bundle bundle = this.getIntent().getExtras();
        //接收type值
        //视频播放部分
        dataName = bundle.getString("video_name");
        video1 = (VideoView) findViewById(R.id.video1);
        play_name=(TextView)findViewById(R.id.play_name);
        prise_num=(TextView)findViewById(R.id.prise_num);
        comment_enter=(EditText)findViewById(R.id.topic_enter);
        comment_send = (Button)findViewById(R.id.comment_send);
        play_prise = (ImageButton)findViewById(R.id.play_prise);
        String[] video_name_cut2 = dataName.split("_");//对每个MP4文件进行分割

        play_name.setText(video_name_cut2[0]);
        play_voice = (ImageButton) findViewById(R.id.play_voice);
        mediaco = new MediaController(this);
        File file = new File(PathConfig.DATA_PATH + "/" + dataName + ".mp4");
        if (file.exists()) {
            //VideoView与MediaController进行关联
            video1.setVideoPath(file.getAbsolutePath());
            video1.setMediaController(mediaco);
            mediaco.setMediaPlayer(video1);
            //让VideiView获取焦点
            video1.requestFocus();
        }
        play_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PlayActivity.this,VoiceActivity.class);
                String[] video_name_cut = dataName.split("_");//对每个MP4文件进行分割
                Bundle bundle2=new Bundle();//用Bundle携带数据
                bundle2.putString("voice_name",video_name_cut[0]);//传递type参数为position
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //界面数据初始化
        prise_num.setText(num);

        //点赞部分
        play_prise.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new addGood(defName,userName,handler).start();
            }
        });

       comment_send.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               String comment = comment_enter.getText().toString();
               if(comment.equals("")){
                   Toast.makeText(PlayActivity.this,"评论不能为空，请重新输入",Toast.LENGTH_SHORT).show();
                   return;
               }
               else{//发布评论

                   //获取当前系统时间
                   SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
                   Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                   String str = formatter.format(curDate);
                   //获取当前登录的用户
                   //获取当前视频名
               }
           }
       });

    }

    //评论部分
    //加载每个子项数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<comment_num;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("comment_name", comment_name.get(i));
            map.put("comment_time", comment_time.get(i));
            map.put("comment_content",comment_content.get(i));
            list.add(map);
        }
        return list;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        //返回listview一共有多少个item
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        //返回值作为列表项ID
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override

        public View getView(final int position, View convertView, ViewGroup parent) {
            //优化
            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.play_comment, null);
                holder.comment_name = (TextView)convertView.findViewById(R.id.comment_name);
                holder.comment_time = (TextView)convertView.findViewById(R.id.comment_time);
                holder.comment_content = (TextView)convertView.findViewById(R.id.comment_content);

                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            HotActivity a =new HotActivity();
            holder.comment_number.setText(Integer.toString(position+1));
            holder.comment_name.setText((String)mData.get(position).get("comment_name"));
            holder.comment_time.setText((String)mData.get(position).get("comment_time"));
            holder.comment_content.setText((String)mData.get(position).get("comment_content"));

            return convertView;
        }
    }
    public final class ViewHolder {
        public TextView comment_number;//评论的序号
        public TextView comment_name;//评论用户姓名
        public TextView comment_time;//评论时间
        public TextView comment_content;//评论内容
    }


}


