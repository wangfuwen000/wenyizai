package utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application{ 
	
    private static Context context; 
    public static String LogFlag = "true";
    private static MyApplication myApplication;
    private static final String TAG = "MyApplication";
    private List<Activity> mList = new LinkedList<Activity>();
    
    public static MyApplication getMyApplication() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }
    
    public static Context getAppContext() { 
        return MyApplication.context; 
    } 
    
    public void onCreate(){ 
        super.onCreate(); 
        MyApplication.context = getApplicationContext();
    } 

 
    
    public void addActivity(Activity pActivity) {
        LogUtil.LogMessage(TAG, "addaddActivity: " + pActivity.toString());
        mList.add(pActivity);
    }

    public void exit() {
        for (Activity iActivity : mList) {
            LogUtil.LogMessage(TAG, "finish activity when exit: " + iActivity.toString());
            iActivity.finish();
        }
        
        mList.clear();
    }
} 