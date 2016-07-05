package com.example.lenovo.voiceshow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import config.PathConfig;

public class Person2Activity extends Activity implements OnClickListener {
    private TextView tv_sucai;//我的配音按钮
    private ImageButton ib_picture;//头像
    private List<Map<String, Object>> mData;
    private ImageButton clean2;
    //public String isLogin=null;
    public List<String>  vecFile_user=new ArrayList<String>();
    public Integer same_num=0;
    public Integer vecFile_user_size=0;
    public String username=null;
    public Integer isLogin=0;
    public String video_name=null;
    // 底部菜单3个ImageView
    private ImageView iv_hot;
    private TextView tv_person;
    private TextView tv_isLogin;
    private ImageView iv_person;
    private ImageView iv_discovery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person2);
        tv_isLogin=(TextView)findViewById(R.id.tv_isLogin);
        ib_picture = (ImageButton)findViewById(R.id.ib_picture);
        tv_sucai = (TextView)findViewById(R.id.tv_sucai);
        tv_isLogin=(TextView)findViewById(R.id.tv_isLogin);
        clean2=(ImageButton)findViewById(R.id.clean2);
        ListView listView = (ListView) findViewById(R.id.person2_listview);

        RelativeLayout rl_bttom = (RelativeLayout)findViewById(R.id.layout_bottom);
        // 底部菜单ImageView
        iv_hot = (ImageView)rl_bttom.findViewById(R.id.iv_hot);
        iv_discovery = (ImageView)rl_bttom.findViewById(R.id.iv_discovery);
        iv_person = (ImageView)rl_bttom.findViewById(R.id.iv_person);
        tv_person = (TextView)rl_bttom.findViewById(R.id.tv_person);

        iv_person.setImageResource(R.drawable.ic_tab_person_pressed);
        tv_person.setTextColor(getResources().getColor(R.color.main_color));

        iv_discovery.setOnClickListener(this);
        iv_hot.setOnClickListener(this);
        tv_sucai.setOnClickListener(this);
        clean2.setOnClickListener(this);

        // 读出用户名
        isLogin=read_user();
        if(isLogin==0){
            ib_picture.setOnClickListener(this);
        }
        else{
            GetVideoFileName();
        }

        //
        mData = getData();
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
    }

    //读取用户名
    public Integer read_user(){
        String fileName= PathConfig. APP_EXTERNAL_ROOT_PATH +"/"+ "用户" + ".txt";
        File file = new File(fileName);
        if (!file.exists()){
            tv_isLogin.setText("点击头像登陆");
            return 0;
        }
        else{
            try {
                FileInputStream fin = new FileInputStream(fileName);
                int length = fin.available();
                byte[] buffer = new byte[length];
                fin.read(buffer);
                username = EncodingUtils.getString(buffer, "UTF-8");
                System.out.println(username);
                fin.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            tv_isLogin.setText(username);
            return 1;
        }
    }
    //本地读取视频名和视频路径
    public void GetVideoFileName(){
        String video_name=null;
        String fileAbsolutePath=PathConfig. DATA_PATH ;
        List<String>  vecFile = new ArrayList<String>();//.MP4文件名称
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP4结尾
                if (filename.trim().toLowerCase().endsWith(".mp4")) {
                    vecFile.add(filename);
                    //写入文件的操作
                    System.out.println(filename);
                }
            }
        }
        //识别本地混好音的视频
        for(int i=0;i<vecFile.size();i++){
            video_name =vecFile.get(i);
            String[] video_name_cut = video_name.split("_");//对每个MP4文件进行分割
            if(video_name_cut.length==2){//即为有out
                vecFile_user.add(video_name_cut[0]);
            }
        }
    }

    public void Dialog(){
        // TODO Auto-generated method stu
        new AlertDialog.Builder(Person2Activity.this).setTitle("系统提示")//设置对话框标题
                .setMessage("确认退出吗？")//设置显示的内容
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                    // @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
                        //finish();
                        tv_isLogin.setText("点击头像登陆");

                        String file_name=PathConfig. APP_EXTERNAL_ROOT_PATH +"/"+ "用户" + ".txt";
                        File file = new File(file_name);
                        file.delete();
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {//响应事件
                dialog.dismiss();
            }
        }).show();//在按键响应事件中显示此对话框

    }

    public void onClick(View v) {
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.iv_discovery:
                Intent intent_discovery=new Intent(Person2Activity.this,FindActivity.class);
                startActivity(intent_discovery);
                break;
            case R.id.iv_hot:
                Intent intent_person=new Intent(Person2Activity.this,HotActivity.class);
                startActivity(intent_person);
                break;
            case R.id.tv_sucai:
                Intent intent = new Intent();
                intent.setClass(Person2Activity.this,Person1Activity.class);
                startActivity(intent);
                break;
            case R.id.ib_picture:
                String fileName= PathConfig. APP_EXTERNAL_ROOT_PATH +"/"+ "用户" + ".txt";
                File file = new File(fileName);
               // Boolean test=file.exists();
               /* if(!test){
                    System.out.println(test);
                    Intent intent2 = new Intent();
                    intent2.setClass(Person2Activity.this,LoginActivity.class);
                    startActivity(intent2);

                else{
                    ib_picture.setOnClickListener(new OnClickListener() {//按键单击事件
                        @Override
                        public void onClick(View v) {
                            // Dialog();
                        }
                    });
                } }*/
                //System.out.println(test);
                Intent intent2 = new Intent();
                intent2.setClass(Person2Activity.this,LoginActivity.class);
                startActivity(intent2);
                break;
            case R.id.clean2:
                //清除文件夹中保存的文件
                Toast.makeText(Person2Activity.this,"清除成功！",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }
    //加载数据
    private List<Map<String, Object>> getData() {
        Integer video_num_ok=vecFile_user.size();
        Bitmap h[]=new Bitmap[video_num_ok];
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<vecFile_user.size();i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("person2_name", vecFile_user.get(i));
            h[i]= BitmapFactory.decodeFile(PathConfig.TEMP_PICTURE_PATH+"/"+vecFile_user.get(i)+".png");
            map.put("person2_picture",h[i]);
            list.add(map);
        }
        return list;
    }
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // 获取从数据库汇总查询的数据总条数
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.person2_item, null);
                holder.person2_name = (TextView)convertView.findViewById(R.id.person2_name);
                //   holder.person2_time = (TextView)convertView.findViewById(R.id.person2_time);
                holder.person2_picture = (ImageView)convertView.findViewById(R.id.person2_picture);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                holder.person2_play = (Button)convertView.findViewById(R.id.person2_play);
                holder.person2_delete = (Button)convertView.findViewById(R.id.person2_delete);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.person2_picture.setImageBitmap((Bitmap)mData.get(position).get("person2_picture"));
            holder.person2_name.setText((String)mData.get(position).get("person2_name"));
            //  holder.person2_time.setText((String)mData.get(position).get("person2_time"));
            holder.person2_play.setTag(position);
            holder.person2_delete.setTag(position);
            holder.person2_play.setOnClickListener(new OnClickListener() {
                @Override
                //点击配音跳转到配音页面
                public void onClick(View v) {
                    Intent intent_play=new Intent(Person2Activity.this,PlayActivity.class);
                    //根据position播放相关已经配好音的视频
                    video_name=vecFile_user.get(position)+"_out";
                    //用Bundle携带数据
                    Bundle bundle=new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("video_name", video_name);
                    intent_play.putExtras(bundle);
                    startActivity(intent_play);
                }
            });
            holder.person2_delete.setOnClickListener(new OnClickListener() {
                @Override
                //点击配音跳转到配音页面
                public void onClick(View v) {
                    File file = new File(PathConfig.DATA_PATH + "/" + vecFile_user.get(position) + "_out.mp4");
                    if(file.isFile()){
                        file.delete();
                    }
                    Intent intent_cancle = new Intent();
                    intent_cancle.setClass(Person2Activity.this,Person2Activity.class);
                    startActivity(intent_cancle);
                }
            });
            return convertView;
        }
    }
    public final class ViewHolder {
        public TextView person2_name;//视频名称
        //  public TextView person2_time;//视频/时间
        public Button person2_delete;//删除视频
        public Button person2_play;//播放视频
        public ImageView person2_picture;//视频缩略图
    }
}