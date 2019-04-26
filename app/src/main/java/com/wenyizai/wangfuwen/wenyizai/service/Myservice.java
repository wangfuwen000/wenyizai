package com.wenyizai.wangfuwen.wenyizai.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.FileObserver;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;


/**
 * Created by user on 15/9/17.
 */
public class Myservice extends Service {

    public  static final String TAG = "MyService";

    private static String LOGPATH;
    private FileObserver mFileObserver;
    public  MyBinder myBinder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Oncreate() exceuted");

//        LOGPATH = Environment.getExternalStorageDirectory() + "/" + "Log/";
//        Log.i(TAG,LOGPATH);
//        System.out.println(LOGPATH);
//
//        File dir = new File(LOGPATH);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        File[] files = dir.listFiles();
//        int size = files.length;
//
//        if (size == 0){
//            MyApplication.havareponse = false;
//        }else {
//            MyApplication.havareponse = true;
//        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"OnStartCommand excueted");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"OnDestroy excuted");
        if(null != mFileObserver) mFileObserver.stopWatching(); //停止监听
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        public void recordLog(){
//            new Thread(new Runnable(){
//                @Override
//                public void run() {
//                    Log.i(TAG, "begin to recorder");
//                    if(null == mFileObserver) {
//                        Log.i(TAG, "mFileObserver");
//                        mFileObserver = new SDCardFileObserver(Environment.getExternalStorageDirectory().getPath()+ File.separator+"Log/");
//                        mFileObserver.startWatching(); //开始监听
//                    }
//
//                }
//            }).start();

        }
    }
    static class SDCardFileObserver extends FileObserver {
        //mask:指定要监听的事件类型，默认为FileObserver.ALL_EVENTS
        public SDCardFileObserver(String path, int mask) {
            super(path, mask);
        }

        public SDCardFileObserver(String path) {
            super(path, FileObserver.CLOSE_WRITE | FileObserver.CREATE | FileObserver.DELETE
                    | FileObserver.DELETE_SELF );
        }

        @Override
        public void onEvent(int event, String path) {
            final int action = event & FileObserver.ALL_EVENTS;
            switch (action) {

                case FileObserver.ACCESS:
                    System.out.println("event: 文件或目录被访问, path: " + path);
                    Log.i(TAG, "xxxxxxxxxxx");
                    break;

                case FileObserver.DELETE:
                    MyApplication.havareponse = false;
                    Log.i(TAG, "xxxxxxxxxxx");
                    System.out.println("event: 文件或目录被删除, path: " + path);
                    break;

                case FileObserver.OPEN:
                    Log.i(TAG, "xxxxxxxxxxx");
                    System.out.println("event: 文件或目录被打开, path: " + path);
                    break;

                case FileObserver.MODIFY:
                    Log.i(TAG, "xxxxxxxxxxx");
                    MyApplication.havareponse = true;
                    System.out.println("event: 文件或目录被修改, path: " + path);
                    break;

                case FileObserver.CREATE:
                    MyApplication.havareponse = true;
                    Log.i(TAG, "xxxxxxxxxxx");
                    System.out.println("event: 文件或目录增加, path: " + path);
                    break;
            }
        }

    }


}
