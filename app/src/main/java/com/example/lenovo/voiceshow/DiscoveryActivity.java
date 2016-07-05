package com.example.lenovo.voiceshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by TongXinSummer on 2016/5/21.
 */
public class DiscoveryActivity extends Activity implements View.OnClickListener {
    ///
    private ImageView iv_discovery;
    private ImageView iv_hot;
    private ImageView iv_person;
    private TextView tv_discovery;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discovery);
        RelativeLayout rl_bttom = (RelativeLayout)findViewById(R.id.layout_bottom);
        // 底部菜单ImageView
        iv_hot = (ImageView)rl_bttom.findViewById(R.id.iv_hot);
        tv_discovery = (TextView) rl_bttom.findViewById(R.id.tv_discovery);
        iv_person = (ImageView)rl_bttom.findViewById(R.id.iv_person);
        iv_discovery = (ImageView)rl_bttom.findViewById(R.id.iv_discovery);

        iv_discovery.setImageResource(R.drawable.ic_tab_discovery_pressed);
        tv_discovery.setTextColor(getResources().getColor(R.color.main_color));

        iv_hot.setOnClickListener(this);
        iv_person.setOnClickListener(this);
    }
    public void onClick(View v) {
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.iv_hot:
                Intent intent_discovery=new Intent(DiscoveryActivity.this,HotActivity.class);
                startActivity(intent_discovery);
                break;
            case R.id.iv_person:
                Intent intent_person=new Intent(DiscoveryActivity.this,Person1Activity.class);
                startActivity(intent_person);
                break;
            default:
                break;
        }
    }
}
