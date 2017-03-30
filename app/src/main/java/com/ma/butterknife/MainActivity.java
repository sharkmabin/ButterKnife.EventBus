package com.ma.butterknife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvEventMsg)
    TextView tvEventMsg;
    @BindView(R.id.tvEventMsg2)
    TextView tvEventMsg2;
    @BindView(R.id.tvEventMsg3)
    TextView tvEventMsg3;
    @BindView(R.id.btnEvent)
    Button btnEvent;
    @BindView(R.id.btnEvent2)
    Button btnEvent2;
    @BindView(R.id.btnEvent3)
    Button btnEvent3;


    private int mCount = 0;
    @Subscribe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        EventBus.getDefault().register(MainActivity.this);
    }

    //消息接收和处理 001 String字符串
    @Subscribe
    public void onEventMsg(String event) {
        tvEventMsg.setText(event);
    }
    //消息接收和处理 002 消息对象
    @Subscribe
    public void onEventMsg(MsgBean event){
        tvEventMsg2.setText(event.getMsg());
    }
    //消息接收和处理 003 整数类型
    @Subscribe
    public void onEventMsg(Integer event){
        tvEventMsg3.setText(event+"");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解除当前EventBus注册
        EventBus.getDefault().unregister(MainActivity.this);
    }

    @OnClick({R.id.btnEvent, R.id.btnEvent2, R.id.btnEvent3})
    public void onViewClicked(View view) {
        Toast.makeText(MainActivity.this,view.getId()+"",Toast.LENGTH_SHORT).show();
        switch (view.getId()) {
            case R.id.btnEvent:
                EventBus.getDefault().post("msg 001");
                break;
            case R.id.btnEvent2:
                EventBus.getDefault().post(new MsgBean("msg 002"));
                break;
            case R.id.btnEvent3:
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(mCount);
                        if (mCount >= 3) {
                            this.cancel();
                            mCount = 0;
                        }
                        mCount++;
                    }
                }, 0, 1000);
                break;
        }
    }
}
