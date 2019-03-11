package com.wenyizai.wangfuwen.wenyizai.utils;

/**
 * Created by user on 16/1/23.
 */

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

/**
 * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    // 需求是 整个应用程序 只有一个 MyCrash-Handler
    public static CrashHandler INSTANCE;
    public static final String APP_CACHE_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/YougHeart/crash/";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context context;

    //1.私有化构造方法
    private CrashHandler() {

    }

    public void init(Context context) {
        this.context = context;
    }

    public static CrashHandler getInstance() {
        if (INSTANCE == null)
            synchronized (CrashHandler.class){
                if (INSTANCE == null){
                    INSTANCE = new CrashHandler();
                }
            }
        return INSTANCE;
    }

    /**
     * 当UncaughtException 发生时会转入该函数来处理
     * @param thread
     * @param ex
     */


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!handleException(ex) && mDefaultHandler != null){
            mDefaultHandler.uncaughtException(thread,ex);
        }else {
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                Log.e(TAG,"Error : ",e);
            }

        }
        System.out.println("程序挂掉了 ");
        Log.i("CrashHandler","CrashHandler"+ Time.getCurrentTimezone());
        // 在此可以把用户手机的一些信息以及异常信息捕获并上传,由于UMeng在这方面有很程序的api接口来调用，故没有考虑
        
        //干掉当前的程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);

    }

    /**
     * 自定义错误处理，收集错误信息
     * 发送错误日志报告等操作均在此完成
     *
     * @param ex
     * @return true;如果处理了该异常信息；否则返回false;
     *
     */
    private  boolean handleException(Throwable ex){
        if (ex == null){
            return  false;
        }
        //crash发送服务器
        sendCrashToServer(context ,ex);

        // 使用Toast来显示信息
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                Toast.makeText(context,"很抱歉，程序出现异常，即将退出",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();


        //保持日志文件
        saveCrashInfoInFile(ex);
        return true;
        
    }

    private void saveCrashInfoInFile(Throwable ex) {
    }

    private void sendCrashToServer(Context context, Throwable ex) {
    }


}