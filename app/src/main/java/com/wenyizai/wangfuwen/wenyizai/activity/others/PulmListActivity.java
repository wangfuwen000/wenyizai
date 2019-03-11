package com.wenyizai.wangfuwen.wenyizai.activity.others;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.base.BaseActivity;
import com.wenyizai.wangfuwen.wenyizai.activity.home.MainActivity;
import com.wenyizai.wangfuwen.wenyizai.adapter.PulmImplAdapter;
import com.wenyizai.wangfuwen.wenyizai.ui.PulmListView;

import java.util.ArrayList;
import java.util.List;

public class PulmListActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private List<String> mItems = new ArrayList<>();
    private int index;
    private static final int MAX_NUM = 30;

    private PulmListView mPulmListView;
    private PulmImplAdapter mAdapter;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.pulm_main);

        System.out.println("invoked method initView ...................");
        Log.i(TAG, "invoked method initView ...................");
        mPulmListView = (PulmListView) findViewById(R.id.id_pulm_lv);
        mAdapter = new PulmImplAdapter(this, mItems);
        mPulmListView.setAdapter(mAdapter);
        // 设置加载更多的回调
        mPulmListView.setmOnPullUpLoadMoreListner(new PulmListView.OnPullUpLoadMoreListner() {
            @Override
            public void OnPullUpLoadMoreListner() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> newItems = generateItems();
                        Log.e(TAG, "new items num=" + newItems + ", already has items=" + mAdapter.getCount());
                        boolean isPageFinished = index >= MAX_NUM;
                        mPulmListView.onFinishLoading(isPageFinished, newItems, false);
                    }
                }, 2000);

            }
        });


    }

    @Override
    protected void loadData() {
        mItems.addAll(generateItems());
    }


    private List<String> generateItems() {
        List<String> newItems = new ArrayList<>();
        for (int i = 0; i < 12 && index < MAX_NUM; i++, index++) {
            newItems.add("标题是" + index);
        }
        return newItems;
    }


}
