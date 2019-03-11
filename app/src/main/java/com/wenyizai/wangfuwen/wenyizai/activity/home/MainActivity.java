package com.wenyizai.wangfuwen.wenyizai.activity.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.loopj.android.http.TextHttpResponseHandler;
import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.base.BaseActivity;
import com.wenyizai.wangfuwen.wenyizai.adapter.FruitAdapter;
import com.wenyizai.wangfuwen.wenyizai.db.DBManager;
import com.wenyizai.wangfuwen.wenyizai.entity.Fruit;
import com.wenyizai.wangfuwen.wenyizai.service.HttpRequestServer;
import com.wenyizai.wangfuwen.wenyizai.service.Myservice;
import com.wenyizai.wangfuwen.wenyizai.utils.HttpUtil;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout swipeRefresh;

    private Fruit[] fruits = {
            new Fruit("Apple",R.drawable.bird),new Fruit("Banana",R.drawable.bird2),
            new Fruit("Apple",R.drawable.bird),new Fruit("Banana",R.drawable.bird2),
            new Fruit("Apple",R.drawable.bird),new Fruit("Banana",R.drawable.bird2),
    };

    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter fruitAdapter;

    private DBManager mgr;
    private ListView listview;

    private LinearLayout mainl;
    private Button btn1;
    private String LogStr = "";
    private String strLog;
    private Myservice.MyBinder myBinder;

    public ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.v(TAG, "Service connected");
//            myBinder = (Myservice.MyBinder) service;
//            myBinder.recordLog();  myBinder = (Myservice.MyBinder) service;
//            myBinder.recordLog();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.v(TAG, "Service  disconnected");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getSupportActionBar().setTitle("文艺");
        setContentView(R.layout.main_layout);
        initFruits();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        fruitAdapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(fruitAdapter);


        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.primary_color));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });

        Intent service = new Intent(MainActivity.this, Myservice.class);
        bindService(service,connection,Context.BIND_AUTO_CREATE);


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


    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"refreshFruits");
                try {
                Thread.sleep(8000);
                }catch (InterruptedException e){
                    e.printStackTrace();

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        fruitAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }


    private void initFruits(){
        fruitList.clear();
        for(int i=0;i<50;i++){
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    private void initListData() {

        HttpUtil.post("hotel", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.i(TAG, HttpUtil.getAbsoluteUrl("/hotel"));
                        Log.i(TAG, responseString);

        //                LogUtil.LogMessage(TAG,HttpUtil.getAbsoluteUrl("hotel")+"==");
        //                LogUtil.LogMessage(TAG, String.valueOf(responseString) + "\n");

                        LogStr = responseString;
                    }
                }
        );
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }



}