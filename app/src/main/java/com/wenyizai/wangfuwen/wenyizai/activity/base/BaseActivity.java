package com.wenyizai.wangfuwen.wenyizai.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

//import com.umeng.analytics.MobclickAgent;
import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.home.MainActivity;
import com.wenyizai.wangfuwen.wenyizai.activity.others.Charity;
import com.wenyizai.wangfuwen.wenyizai.activity.others.CounterActivity;
import com.wenyizai.wangfuwen.wenyizai.activity.others.PulmListActivity;
import com.wenyizai.wangfuwen.wenyizai.activity.weather.WeatherByFastJsonActivity;
import com.wenyizai.wangfuwen.wenyizai.net.RequestManager;
import com.wenyizai.wangfuwen.wenyizai.service.MyApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public abstract class BaseActivity extends AppCompatActivity {

    public LinearLayout rootLayout;
    public NavigationView navigationView;
    protected RequestManager requestManager = null;
    private long startTime;
    private long endTime;
    private boolean isReStart = false;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private DrawerLayout mDrawerLayout;
    private boolean isExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestManager = new RequestManager(this);

        super.onCreate(savedInstanceState);
        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.base);
        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        initToolbar();
        initVariables();
        loadData();
        initView(savedInstanceState);



    }

    protected abstract void initVariables();
    protected abstract void initView(Bundle savedInstanceState);
    protected abstract void loadData();


    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.base_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initToolbar();
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setCheckedItem(R.id.nav_header_image);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                //用于辨别此前是否已有选中条目
                MenuItem preMenuItem;

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    //首先将选中条目变为选中状态 即checked为true,后关闭Drawer，以前选中的Item需要变为未选中状态
                    if(preMenuItem!=null)
                        preMenuItem.setChecked(false);
                    item.setChecked(true);
                    preMenuItem=item;
                    //不同item对应不同图片
                    Intent intent;
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            intent = new Intent();
                            intent.setClass(BaseActivity.this, MainActivity.class);
                            startActivity(intent);
                            break;


                        case R.id.vav_friend:
                            intent = new Intent();
                            intent.setClass(BaseActivity.this, PulmListActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.vav_cishan:
                            intent = new Intent();
                            intent.setClass(BaseActivity.this, Charity.class);
                            startActivity(intent);
                            break;

                        case R.id.vav_ConstraintLayout:
                            intent = new Intent(BaseActivity.this, WeatherByFastJsonActivity.class);
                            startActivity(intent);
                            break;

                        default:
                            mDrawerLayout.closeDrawers();
                            break;
                    }

                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            exitByDoubleClick();
        }
        return false;
    }

    private void exitByDoubleClick() {
        Timer tExit=null;
        if(!isExit){
            isExit=true;
            Toast.makeText(BaseActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;//取消退出
                }
            },2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }else{
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        /**
         * 在activity销毁的时候同时设置停止请求，停止线程请求回调
         */
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        /**
         * 在activity停止的时候同时设置停止请求，停止线程请求回调
         */
        if (requestManager != null) {
            requestManager.cancelRequest();
        }
        super.onPause();
//        MobclickAgent.onPause(this);

    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startTime = System.currentTimeMillis();
        isReStart = true;
//        Log.i("end time-----end", "restart");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        endTime = System.currentTimeMillis();
//        long hotinterval = isReStart == false? -1: (MyApplication.isCoolStart == true? -1: endTime - startTime);
        long hotinterval = isReStart == false? -1: endTime - startTime;

        long coolinterval = MyApplication.isCoolStart == false? -1:endTime - MyApplication.coolBeginTime;
        MyApplication.isCoolStart = false;
        isReStart = false;
//        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Log.i("start time-----热启动   ", dateformat.format(interval));
        Log.i("time-----热启动   ", String.valueOf(hotinterval));
        Log.i("time-----冷启动   ", String.valueOf(coolinterval));

    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

}


