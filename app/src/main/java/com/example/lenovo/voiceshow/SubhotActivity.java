package com.example.lenovo.voiceshow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bupt.sse.thread.getHotsPic;
import bupt.sse.thread.getHotsString;
import bupt.sse.thread.getMP4;
import config.PathConfig;

public class SubhotActivity extends Activity implements OnClickListener {
    private List<Map<String, Object>> mData;
    public String hot_type="";
    public Integer video_num=6;
    public String video_name=null;
    private Bitmap h[]=new Bitmap[video_num];
    //以下数据实际从数据库中获取
    private TextView tv_hottypename;

    public  String subhot_name[]=new String[video_num];
    public  String subhot_time[]=new String[]{"50","50","50","10","10","10"};
 //   public  int subhot_picture[]=new int[video_num];
    public  String subhot_playnum[]=new String[]{"10","10","10","10","10","10"};
    public  String subhot_likenum[]=new String[]{"10","10","10","10","10","10"};

    private ImageButton ib_back;//返回按钮
    // 底部菜单ImageView
    private ImageView iv_discovery;
    private ImageView iv_hot;
    private ImageView iv_person;
    private TextView tv_hot;

    private String select_Name;
    private ProgressDialog dlgMixing;
    private List<String> hots ;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            if(what == 0){
                //imageView.setImageBitmap(bmp);
                new getHotsPic(this,hots).start();
            }
            if(what==1){
                System.out.println("获取热门素材图片成功！");
                dlgMixing.cancel();

                for(int i=0;i< subhot_name.length;i++){
                    subhot_name[i]=hots.get(i);
                }

                mData = getData();
                ListView listView = (ListView) findViewById(R.id.subhot_listview);
                MyAdapter adapter = new MyAdapter(SubhotActivity.this);
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
                Intent intent=new Intent(SubhotActivity.this, PlayActivity.class);
                Bundle bundle=new Bundle();//用Bundle携带数据
                bundle.putString("video_name",select_Name);//传递type参数为position
                intent.putExtras(bundle);
                startActivity(intent);
            }
            //获取评论

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subhot);
        tv_hottypename=(TextView)findViewById(R.id.tv_hottypename);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        hot_type = bundle.getString("hot_type");
        tv_hottypename.setText(hot_type);
        ib_back=(ImageButton)findViewById(R.id.ib_back);

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
        ib_back.setOnClickListener(this);

        dlgMixing = new ProgressDialog(this);
        dlgMixing.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dlgMixing.setCancelable(false);
        dlgMixing.setCanceledOnTouchOutside(false);

        hots = new ArrayList<String>();
        new getHotsString(handler,hots).start();
        dlgMixing.setMessage("拼命加载资源中...");
        dlgMixing.show();
    }

    public void onClick(View v) {
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.iv_discovery:
                Intent intent_discovery=new Intent(SubhotActivity.this,LoginActivity.class);
                startActivity(intent_discovery);
                break;
            case R.id.iv_person:
                Intent intent_person=new Intent(SubhotActivity.this,Person1Activity.class);
                startActivity(intent_person);
                break;
            case R.id.ib_back:
                Intent intent = new Intent();
                intent.setClass(SubhotActivity.this,HotActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
    //加载每个子项数据
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(int i=0;i<subhot_name.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("subhot_name", subhot_name[i]);
            map.put("subhot_time", subhot_time[i]);
            h[i]= BitmapFactory.decodeFile(PathConfig.TEMP_PICTURE_PATH+"/"+hots.get(i)+".png");
            map.put("subhot_picture",h[i]);
            map.put("subhot_likenum",subhot_likenum[i]);
            map.put("subhot_playnum",subhot_playnum[i]);
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
                convertView = mInflater.inflate(R.layout.subhot_item, null);
                holder.subhot_number = (TextView)convertView.findViewById(R.id.tv_number);
                holder.subhot_picture = (ImageView)convertView.findViewById(R.id.iv_picture);
                holder.subhot_name = (TextView)convertView.findViewById(R.id.tv_name);
                holder.subhot_time = (TextView)convertView.findViewById(R.id.tv_time);
                holder.subhot_likenum =  (TextView)convertView.findViewById(R.id.tv_likenum);
                holder.subhot_playnum = (TextView)convertView.findViewById(R.id.tv_playnum);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            HotActivity a =new HotActivity();
            holder.subhot_number.setText(Integer.toString(position+1));
            holder.subhot_picture.setImageBitmap((Bitmap)mData.get(position).get("subhot_picture"));
            holder.subhot_name.setText((String)mData.get(position).get("subhot_name"));
            holder.subhot_time.setText((String)mData.get(position).get("subhot_time"));
            holder.subhot_likenum.setText((String)mData.get(position).get("subhot_likenum"));
            holder.subhot_playnum.setText((String)mData.get(position).get("subhot_playnum"));
            holder.subhot_picture.setTag(position);
            //给Imageview添加单击事件
            holder.subhot_picture.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击视频缩略图跳转到播放页面
                    video_name=subhot_name[position];
                    SubhotActivity.this.select_Name = video_name;
                    dlgMixing.setMessage("拼命下载视频素材中...");
                    dlgMixing.show();
                    new getMP4(handler,video_name,0).start();
                }
            });
            return convertView;
        }
    }
    public final class ViewHolder {
        public TextView subhot_number;//视频序号
        public ImageView subhot_picture;//视频缩略图
        public TextView subhot_name;//视频名称
        public TextView subhot_time;//视频时长
        public TextView subhot_likenum;//视频点赞数
        public TextView subhot_playnum;//视频播放数
    }
}