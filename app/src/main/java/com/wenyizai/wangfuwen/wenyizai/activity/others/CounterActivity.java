package com.wenyizai.wangfuwen.wenyizai.activity.others;

import android.os.Bundle;
import android.widget.TextView;

import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.base.BaseActivity;

//import com.orhanobut.logger.Logger;

/**
 * Created by user on 15/9/17.
 */
public class CounterActivity extends BaseActivity {
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counterview);
        title = (TextView) findViewById(R.id.title_text);
        title.setText("Counter");


    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void loadData() {

    }
}
