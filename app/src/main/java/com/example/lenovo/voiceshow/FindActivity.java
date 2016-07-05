package com.example.lenovo.voiceshow;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import config.PathConfig;

public class FindActivity extends Activity implements View.OnClickListener {
    ImageButton search;
    // 底部菜单ImageView
    private ImageView iv_discovery;
    private ImageView iv_hot;
    private ImageView iv_person;
    private TextView tv_discovery;
    private TextView tv_find;
    private ImageButton ib_search;//搜索按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_page);
        new PathConfig();

        //顶部标题栏
        AbsoluteLayout al_findhead=(AbsoluteLayout)findViewById(R.id.find_head);
        ib_search=(ImageButton)al_findhead.findViewById(R.id.ib_search);
        tv_find=(TextView)al_findhead.findViewById(R.id.firstText) ;

        // 底部菜单ImageView
        RelativeLayout rl_bottom = (RelativeLayout)findViewById(R.id.layout_bottom);
       iv_hot = (ImageView)rl_bottom.findViewById(R.id.iv_hot);
        iv_person = (ImageView)rl_bottom.findViewById(R.id.iv_person);

        tv_discovery = (TextView) rl_bottom.findViewById(R.id.tv_discovery);
        iv_discovery = (ImageView)rl_bottom.findViewById(R.id.iv_discovery);

        iv_discovery.setImageResource(R.drawable.ic_tab_discovery_pressed);
        tv_discovery.setTextColor(getResources().getColor(R.color.main_color));

        iv_hot.setOnClickListener(this);
        iv_person.setOnClickListener(this);
        ib_search.setOnClickListener(this);
    }
    public void onClick(View v) {
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.iv_hot:
                Intent intent_discovery=new Intent(FindActivity.this,HotActivity.class);
                startActivity(intent_discovery);
                break;
            case R.id.iv_person:
                Intent intent_person=new Intent(FindActivity.this,Person1Activity.class);
                startActivity(intent_person);
                break;
            case R.id.ib_search:
                Intent intent_search=new Intent(FindActivity.this,SearchActivity.class);
                startActivity(intent_search);
                break;
            default:
                break;
        }
    }
}
