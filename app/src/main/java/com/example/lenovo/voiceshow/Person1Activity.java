package com.example.lenovo.voiceshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.PathConfig;

public class Person1Activity extends Activity implements OnClickListener {
    private TextView tv_peiyin;//我的配音按钮
    private ImageButton ib_picture;//头像
    public Integer isLogin=0;
    public String username;
    private ImageButton clean;
    private List<Map<String, Object>> mData;
    private int flag;
    //视频名称
    public static String person1_name[]=new String[]{"疯狂动物城","PAPI酱","生活大爆炸","周杰伦","疯狂动物城"};
    //视频时长
    public static String person1_time[]=new String[]{"1:06s","56s","1:20s","34s","34s"};
    //视频缩略图
    public static int person1_picture[]=new int[]{R.drawable.hot01,R.drawable.hot02,R.drawable.hot03,R.drawable.hot04,R.drawable.hot01};

    // 底部菜单3个ImageView
    private ImageView iv_hot;
    private TextView tv_person;
    private ImageView iv_person;
    private ImageView iv_discovery;
    private TextView tv_isLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person1);
        mData = getData();
        ib_picture = (ImageButton)findViewById(R.id.ib_picture);
        tv_peiyin = (TextView)findViewById(R.id.tv_peiyin);
        tv_isLogin=(TextView)findViewById(R.id.tv_isLogin);
        clean=(ImageButton)findViewById(R.id.clean);
        ListView listView = (ListView) findViewById(R.id.person1_listview);

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
        tv_peiyin.setOnClickListener(this);
        ib_picture.setOnClickListener(this);
        clean.setOnClickListener(this);

        // 读出用户名
        isLogin=read_user();
        if(isLogin==0)
            ib_picture.setOnClickListener(this);
       // isLogin(isLogin);

        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
    }

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
    public void onClick(View v) {
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.iv_discovery:
                Intent intent_discovery=new Intent(Person1Activity.this,FindActivity.class);
                startActivity(intent_discovery);
                break;
            case R.id.iv_hot:
                Intent intent_person=new Intent(Person1Activity.this,HotActivity.class);
                startActivity(intent_person);
                break;
            case R.id.tv_peiyin:
                Intent intent = new Intent();
                intent.setClass(Person1Activity.this,Person2Activity.class);
                startActivity(intent);
                break;
            case R.id.ib_picture:
                String fileName= PathConfig. APP_EXTERNAL_ROOT_PATH +"/"+ "用户" + ".txt";
                File file = new File(fileName);
                Boolean test=file.exists();
                if(!test){
                    System.out.println(test);
                    Intent intent2 = new Intent();
                    intent2.setClass(Person1Activity.this,LoginActivity.class);
                    startActivity(intent2);
                }
                else{
                    ib_picture.setOnClickListener(new OnClickListener() {//按键单击事件
                        @Override
                        public void onClick(View v) {
                            // Dialog();
                        }
                    });
                }
                break;

            case R.id.clean:
                //清除文件夹中缓存文件

                Toast.makeText(Person1Activity.this,"清除成功！",Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    //加载数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<person1_name.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("person1_name", person1_name[i]);
            map.put("person1_time", person1_time[i]);
            map.put("person1_picture",person1_picture[i]);
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
                convertView = mInflater.inflate(R.layout.person1_item, null);
                holder.person1_name = (TextView)convertView.findViewById(R.id.person1_name);
                holder.person1_time = (TextView)convertView.findViewById(R.id.person1_time);
                holder.person1_voice = (Button)convertView.findViewById(R.id.person1_voice);
                holder.person1_picture = (ImageView)convertView.findViewById(R.id.person1_picture);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.person1_name.setText((String)mData.get(position).get("person1_name"));
            holder.person1_time.setText((String)mData.get(position).get("person1_time"));
            holder.person1_picture.setImageResource((int)mData.get(position).get("person1_picture"));
            holder.person1_voice.setTag(position);
            //给Button添加单击事件
            holder.person1_voice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击配音跳转到配音页面

                    //根据posiiton播放相关配音视频Voice
                    //playOn(position);
                }
            });
            return convertView;
        }
    }
    public final class ViewHolder {
        public TextView person1_name;
        public TextView person1_time;
        public Button person1_voice;
        public ImageView person1_picture;
    }
    //根据点击按钮播放position对应的相关视频
    public void playOn(int position){
        /*
        播放相关视频
         */
    }
}