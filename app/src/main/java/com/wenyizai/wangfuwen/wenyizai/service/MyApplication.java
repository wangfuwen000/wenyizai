package com.wenyizai.wangfuwen.wenyizai.service;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

//import com.github.anrwatchdog.ANRWatchDog;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.wenyizai.wangfuwen.wenyizai.engine.Constants;
import com.wenyizai.wangfuwen.wenyizai.utils.CrashHandler;
import com.wenyizai.wangfuwen.wenyizai.utils.LogUtil;

//import org.acra.ACRA;
//import org.acra.annotation.ReportsCrashes;
//import org.acra.sender.HttpSender;

import java.util.LinkedList;
import java.util.List;


//@ReportsCrashes(
////        httpMethod = HttpSender.Method.PUT,
////        reportType = HttpSender.Type.JSON,
////        formUri = "http://123.57.11.138:5984/acra-myapp/_design/acra-storage/_update/report",
////        formUriBasicAuthLogin = "bctest",
////        formUriBasicAuthPassword = "12345"
//
////        httpMethod = HttpSender.Method.PUT,
////        reportType = HttpSender.Type.JSON,
////        formUri = "http://192.168.0.107:5984/acra-myapp/_design/acra-storage/_update/report",
////        formUriBasicAuthLogin = "wangfuwen000",
////        formUriBasicAuthPassword = "123456"
//
////
////        httpMethod = HttpSender.Method.PUT,
////        reportType = HttpSender.Type.JSON,
////        formUri = "http://123.57.11.138:5984/acra-wenyi/_design/acra-storage/_update/report",
////        formUriBasicAuthLogin = "wenyi",
////        formUriBasicAuthPassword = "123456"
//
//        formUri = "http://123.57.11.138:5984/acra-myapp/_design/acra-storage/_update/report",
//        reportType = HttpSender.Type.JSON,
//        httpMethod = HttpSender.Method.PUT,
//        formUriBasicAuthLogin = "tester",
//        formUriBasicAuthPassword = "123456",
////        customReportContent = { APP_VERSION, ANDROID_VERSION, PHONE_MODEL, CUSTOM_DATA, STACK_TRACE, LOGCAT },
//        logcatArguments = {"-t", "100", "-v", "time", "BaseActivity:I"}
//
//)

public class MyApplication extends Application {


    private static Context context;
    public static String LogFlag = "true";
    private static MyApplication myApplication;
    private static final String TAG = "MyApplication";
    private List<Activity> mList = new LinkedList<Activity>();
    public static long coolBeginTime;

    public static Boolean havareponse = false;
    public static Boolean isCoolStart = true;


    public static MyApplication getMyApplication() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public void onCreate() {
        super.onCreate();
//        ACRA.init(this);

//        if (!BuildConfig.DEBUG) {
//        new ANRWatchDog().start();
//        }

//
//        Logger
//            .init("cat")                 // 如果仅仅调用 init 不传递参数，默认标签是 PRETTYLOGGER
//            .methodCount(3)                 // 显示调用方法链的数量，默认是2
//            .hideThreadInfo()               // 隐藏线程信息，默认是隐藏
//            .logLevel(LogLevel.NONE)        // 日志等级，其实就是控制是否打印，默认为 LogLevel.FULL
//            .methodOffset(2)                // default 0
//            .logTool(new AndroidLogTool()); // custom log tool, optional


        if (Constants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

        }


        if (Constants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);

        //Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(configuration);
        MyApplication.context = getApplicationContext();


        // [可选]设置是否打开debug输出，上线时请关闭，Logcat标签为"MtaSDK"
        // https://mta.qq.com/
        StatConfig.setDebugEnable(true);
        // 基础统计API
        StatService.registerActivityLifecycleCallbacks(this);

        NBSAppAgent.setLicenseKey("db65894d424e4bc9a30d3a3ef9d5870e")
                .withLocationServiceEnabled(true).start(this.getApplicationContext());//Appkey 请从官网获取


        CrashHandler handler = CrashHandler.getInstance();
        handler.init(getApplicationContext());
        Thread.setDefaultUncaughtExceptionHandler(handler);

    }


    public void addActivity(Activity pActivity) {
        LogUtil.LogMessage(TAG, "addaddActivity:    " + pActivity.toString());
        mList.add(pActivity);
    }

    public void exit() {
        for (Activity iActivity : mList) {
            LogUtil.LogMessage(TAG, "finish activity when exit: " + iActivity.toString());
            iActivity.finish();
        }

        mList.clear();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        coolBeginTime = System.currentTimeMillis();
//        Log.i("start time-----first", String.valueOf(coolBeginTime));
        isCoolStart = true;
    }
}