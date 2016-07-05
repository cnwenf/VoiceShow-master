package com.example.lenovo.voiceshow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bupt.sse.thread.getHotsPic;
import bupt.sse.thread.getHotsString;
import bupt.sse.thread.getMP4;
import config.PathConfig;


public class HotActivity extends Activity implements OnClickListener {
    public String video_name;
    private List<Map<String, Object>> mData;
    private int flag;
    public static int type=1;//热门视频类型
    //热门视频类型名称
    public  String hot_type[]=new String[]{"大咖模仿"};
    //热门视频类型图标
    public  int hot_typepicture[]=new int[]{R.drawable.hot_mofang};

    public  String hot_name1[]=new String[1];
    public  String hot_name2[]=new String[1];
    public  String hot_name3[]=new String[1];
    public  String hot_name4[]=new String[1];

    public   String hot_playnum1[]=new String[]{"10"};
    public   String hot_playnum2[]=new String[]{"10"};
    public   String hot_playnum3[]=new String[]{"10"};
    public   String hot_playnum4[]=new String[]{"10"};
    //视频点赞数，从数据库获取
    public   String hot_likenum1[]=new String[]{"10"};
    public   String hot_likenum2[]=new String[]{"10"};
    public   String hot_likenum3[]=new String[]{"10"};
    public   String hot_likenum4[]=new String[]{"10"};

    // 底部菜单ImageView
    private ImageView iv_discovery;
    private ImageView iv_hot;
    private ImageView iv_person;
    private TextView tv_hot;
    private Bitmap h[]=new Bitmap[4];
    private String select_Name;

    private ProgressDialog dlgMixing;
    private List<String> hots ;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what == 0){
                new getHotsPic(this,hots).start();
            }
            if(what==1){
                System.out.println("获取图片素材成功！");
                dlgMixing.cancel();
                hot_name1[0]=hots.get(0);
                hot_name2[0]=hots.get(1);
                hot_name3[0]=hots.get(2);
                hot_name4[0]=hots.get(3);
                mData = getData();
                ListView listView = (ListView) findViewById(R.id.hot1_listview);
                MyAdapter adapter = new MyAdapter(HotActivity.this);
                listView.setAdapter(adapter);
            }
            if(what==11){
                System.out.println("获取视频素材成功！开始下载音乐素材...");
                dlgMixing.cancel();
                dlgMixing.setMessage("拼命下载音乐素材中...");
                dlgMixing.show();
                new getMP4(handler,select_Name,1).start();
            }
            if(what==12){
                dlgMixing.cancel();
                System.out.println("获取音乐素材成功!");
                Intent intent=new Intent(HotActivity.this, PlayActivity.class);
                Bundle bundle=new Bundle();//用Bundle携带数据
                bundle.putString("video_name",select_Name);//传递type参数为position
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot1);
        new PathConfig();
        RelativeLayout rl_bttom = (RelativeLayout)findViewById(R.id.layout_bottom);
        // 底部菜单ImageView
        iv_hot = (ImageView)rl_bttom.findViewById(R.id.iv_hot);
        tv_hot = (TextView) rl_bttom.findViewById(R.id.tv_hot);
        iv_person = (ImageView)rl_bttom.findViewById(R.id.iv_person);
        iv_discovery = (ImageView)rl_bttom.findViewById(R.id.iv_discovery);

        iv_hot.setImageResource(R.drawable.ic_tab_hot_pressed);
        tv_hot.setTextColor(getResources().getColor(R.color.main_color));

        iv_discovery.setOnClickListener(this);
        iv_person.setOnClickListener(this);
        dlgMixing = new ProgressDialog(this);
        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgMixing.setCancelable(false);
        dlgMixing.setCanceledOnTouchOutside(false);

        hots = new ArrayList<String>();
        new getHotsString(handler,hots).start();
        dlgMixing.setMessage("拼命加载资源中...");
        dlgMixing.show();
    }

    //加载数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<hot_type.length;i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("hot_typepicture", hot_typepicture[0]);
            map.put("hot_type", hot_type[i]);
            map.put("hot_name1", hot_name1[i]);
            map.put("hot_name2", hot_name2[i]);
            map.put("hot_name3", hot_name3[i]);
            map.put("hot_name4", hot_name4[i]);
            map.put("hot_playnum1", hot_playnum1[i]);
            map.put("hot_playnum2", hot_playnum2[i]);
            map.put("hot_playnum3", hot_playnum3[i]);
            map.put("hot_playnum4", hot_playnum4[i]);
            map.put("hot_likenum1", hot_likenum1[i]);
            map.put("hot_likenum2", hot_likenum2[i]);
            map.put("hot_likenum3", hot_likenum3[i]);
            map.put("hot_likenum4", hot_likenum4[i]);
            list.add(map);
        }
        return list;
    }

    public void onClick(View v) {
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.iv_discovery:
                Intent intent_discovery=new Intent(HotActivity.this,FindActivity.class);
                startActivity(intent_discovery);
                break;
            case R.id.iv_person:
                Intent intent_person=new Intent(HotActivity.this,Person1Activity.class);
                startActivity(intent_person);
                break;
            default:
                break;
        }
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
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
                convertView = mInflater.inflate(R.layout.hot1_item, null);
                holder.hot_more=(TextView)convertView.findViewById(R.id.tv_more);
                holder.hot_type = (TextView)convertView.findViewById(R.id.tv_type);
                holder.hot_typepicture = (ImageView) convertView.findViewById(R.id.iv_typepicture);
                holder.hot_picture1 = (ImageView)convertView.findViewById(R.id.iv_picture1);
                holder.hot_picture2 = (ImageView)convertView.findViewById(R.id.iv_picture2);
                holder.hot_picture3 = (ImageView)convertView.findViewById(R.id.iv_picture3);
                holder.hot_picture4 = (ImageView)convertView.findViewById(R.id.iv_picture4);
                holder.hot_name1 = (TextView)convertView.findViewById(R.id.tv_name1);
                holder.hot_name2 = (TextView)convertView.findViewById(R.id.tv_name2);
                holder.hot_name3 = (TextView)convertView.findViewById(R.id.tv_name3);
                holder.hot_name4 = (TextView)convertView.findViewById(R.id.tv_name4);
                holder.hot_playnum1 = (TextView) convertView.findViewById(R.id.tv_playnum1);
                holder.hot_playnum2 = (TextView) convertView.findViewById(R.id.tv_playnum2);
                holder.hot_playnum3 = (TextView) convertView.findViewById(R.id.tv_playnum3);
                holder.hot_playnum4 = (TextView) convertView.findViewById(R.id.tv_playnum4);
                holder.hot_likenum1 = (TextView) convertView.findViewById(R.id.tv_likenum1);
                holder.hot_likenum2 = (TextView) convertView.findViewById(R.id.tv_likenum2);
                holder.hot_likenum3 = (TextView) convertView.findViewById(R.id.tv_likenum3);
                holder.hot_likenum4 = (TextView) convertView.findViewById(R.id.tv_likenum4);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.hot_type.setText((String)mData.get(position).get("hot_type"));
            holder.hot_typepicture.setImageResource((int)mData.get(position).get("hot_typepicture"));
            for(int n=0;n<h.length;n++){
                h[n]=BitmapFactory.decodeFile(PathConfig.TEMP_PICTURE_PATH+"/"+hots.get(n)+".png");
            }
            holder.hot_picture1.setImageBitmap(h[0]);
            holder.hot_picture2.setImageBitmap(h[1]);
            holder.hot_picture3.setImageBitmap(h[2]);
            holder.hot_picture4.setImageBitmap(h[3]);
            holder.hot_name1.setText((String)mData.get(position).get("hot_name1"));
            holder.hot_name2.setText((String)mData.get(position).get("hot_name2"));
            holder.hot_name3.setText((String)mData.get(position).get("hot_name3"));
            holder.hot_name4.setText((String)mData.get(position).get("hot_name4"));
            holder.hot_playnum1.setText((String)mData.get(position).get("hot_playnum1"));
            holder.hot_playnum2.setText((String)mData.get(position).get("hot_playnum2"));
            holder.hot_playnum3.setText((String)mData.get(position).get("hot_playnum3"));
            holder.hot_playnum4.setText((String)mData.get(position).get("hot_playnum4"));
            holder.hot_likenum1.setText((String)mData.get(position).get("hot_likenum1"));
            holder.hot_likenum2.setText((String)mData.get(position).get("hot_likenum2"));
            holder.hot_likenum3.setText((String)mData.get(position).get("hot_likenum3"));
            holder.hot_likenum4.setText((String)mData.get(position).get("hot_likenum4"));
            holder.hot_more.setTag(position);
            holder.hot_picture1.setTag(position); holder.hot_picture2.setTag(position);
            holder.hot_picture3.setTag(position); holder.hot_picture4.setTag(position);
            holder.hot_more.setOnClickListener(new OnClickListener() {
                //点击不同种类的热门视频跳转到相应页面
                @Override
                public void onClick(View v) {
                    type=position;//获得热门视频类型
                    Intent intent=new Intent(HotActivity.this,SubhotActivity.class);
                    Bundle bundle=new Bundle();//用Bundle携带数据
                    bundle.putString("hot_type",hot_type[position]);//传递type参数为position
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            holder.hot_picture1.setOnClickListener(new OnClickListener() {
                //点击某类热门视频的第一个视频进行播放
                @Override
                public void onClick(View v) {
                    video_name=hot_name1[position];
                    HotActivity.this.select_Name = video_name;
                    dlgMixing.setMessage("拼命下载视频素材中...");
                    dlgMixing.show();
                    new getMP4(handler,video_name,0).start();
                }
            });

        holder.hot_picture2.setOnClickListener(new OnClickListener() {
                //点击某类热门视频的第一个视频进行播放
                @Override
                public void onClick(View v) {
                    video_name=hot_name2[position];
                    HotActivity.this.select_Name = video_name;
                    dlgMixing.setMessage("拼命下载视频素材中...");
                    dlgMixing.show();
                    new getMP4(handler,video_name,0).start();
                }
            });
            holder.hot_picture3.setOnClickListener(new OnClickListener() {
                //点击某类热门视频的第一个视频进行播放
                @Override
                public void onClick(View v) {
                    video_name=hot_name3[position];
                    HotActivity.this.select_Name = video_name;
                    dlgMixing.setMessage("拼命下载视频素材中...");
                    dlgMixing.show();
                    new getMP4(handler,video_name,0).start();
                }
            });
            holder.hot_picture4.setOnClickListener(new OnClickListener() {
                //点击某类热门视频的第一个视频进行播放
                @Override
                public void onClick(View v) {
                    video_name=hot_name4[position];
                    HotActivity.this.select_Name = video_name;
                    dlgMixing.setMessage("拼命下载视频素材中...");
                    dlgMixing.show();
                    new getMP4(handler,video_name,0).start();
                }
            });
            return convertView;
        }
    }
    public final class ViewHolder {
        public TextView hot_type;
        public ImageView hot_typepicture;
        public TextView hot_more;
        public TextView hot_name1,hot_name2,hot_name3,hot_name4;
        public TextView hot_playnum1,hot_playnum2,hot_playnum3,hot_playnum4;
        public TextView hot_likenum1,hot_likenum2,hot_likenum3,hot_likenum4;
        public ImageView hot_picture1,hot_picture2,hot_picture3,hot_picture4;
    }
}