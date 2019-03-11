package com.wenyizai.wangfuwen.wenyizai;

import android.app.Instrumentation;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.content.pm.ResolveInfo;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiWatcher;
import android.support.test.uiautomator.Until;
import android.util.Log;

import com.bumptech.glide.util.Util;
import com.robotium.solo.SystemUtils;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import utils.DeviceAppInfoUtils;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


/**
 * Created by wangfuwen on 2017/5/25.
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class CalculatorTest {
    private UiDevice mDevice;
    private Context context;
    private static final int LAUNCH_TIMEOUT = 5000;
    private final String BASIC_SAMPLE_PACKAGE = "com.wenyizai.wangfuwen.wenyizai";
    private Instrumentation instrumentation;

    private static final long TIME_OUT = 10 * 60 * 1000;
    long eslcape = 0;

    private long currentTime;
    private boolean flag = true;

    @Before
    public void setUp() {
        //Initialize  Instrumentation  instance
        instrumentation = InstrumentationRegistry.getInstrumentation();
        context = instrumentation.getContext();
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(instrumentation);


        // Start from the home screen
        mDevice.pressHome();

        // Wait for launcher
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);


    }
    @Test
    public void checkPreconditions() {
        assertThat(mDevice, notNullValue());
    }

    @Test
    public void openTest() throws IOException,InterruptedException,UiObjectNotFoundException{

        UiWatcher inComingWatcher = new MyWatcher();
        UiWatcher sanxing = new Sanxing();

        mDevice.registerWatcher("notifitation", inComingWatcher);
        mDevice.registerWatcher("sanxing", sanxing);

        mDevice.wait(Until.findObject(By.text("文艺宅")),20000);
        UiObject2 object2 = mDevice.findObject(By.text("文艺宅"));
        object2.click();

        UiObject object = mDevice.findObject(new UiSelector().text("Apple1"));
        try {
            object.clickAndWaitForNewWindow();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }


        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("是否有监听器触发过：" + mDevice.hasAnyWatcherTriggered());
        System.out.println("监听器是否被触发过：" + mDevice.hasWatcherTriggered("notifitation"));
        mDevice.removeWatcher("notifitation");

    }


    class MyWatcher implements UiWatcher {

        @Override
        public boolean checkForCondition() {
//            UiObject inCall = mDevice.findObject(new UiSelector().resourceId("com.android.dialer:id/IncomingCallRejectButton"));

            UiObject inCall = mDevice.findObject(new UiSelector().text("Banana"));

            if (inCall.exists()) {
                System.out.println("you have a call");
                try {
                    inCall.clickAndWaitForNewWindow();
                    return true;
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }


    class Sanxing implements UiWatcher {

        @Override
        public boolean checkForCondition() {
            UiObject refirm = mDevice.findObject(new UiSelector().resourceId("android:id/button1"));
            UiObject confirm_button = mDevice.findObject(new UiSelector().resourceId("com.android.packageinstaller:id/confirm_button"));


            if (refirm.exists()) {
                System.out.println("you have a call");
                try {
                    refirm.clickAndWaitForNewWindow();
                    return true;
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (confirm_button.exists()) {
                System.out.println("you have a call");
                try {
                    confirm_button.clickAndWaitForNewWindow();
                    return true;
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    @Test
    public void testInstrumentation() throws Exception{
//        Bundle bundle = InstrumentationRegistry.getArguments();
//        instrumentation.sendStatus(888,bundle);
//
//
//        Context cur_context = instrumentation.getContext();
////        Context tar_context = instrumentation.getTargetContext();
//        PackageManager pm = cur_context.getPackageManager();
//        Bundle bundle1 = new Bundle();
//        bundle1.putString("XXXXXXXXXX",pm.getInstalledPackages(0).get(0).toString());
//
//
//        InstrumentationRegistry.registerInstance(instrumentation,bundle1);
//        Bundle bundle2 = InstrumentationRegistry.getArguments();
//        instrumentation.sendStatus(999,bundle2);
//
//        File file = new File("sss");
//        OutputStream outputStream = new FileOutputStream(file,true);
//        mDevice.dumpWindowHierarchy(outputStream);
//        mDevice.wait(Until.findObject(By.text("xxxx")),20000);




    }


    public  static void setWifiStatus(Context context, final boolean status){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final DeviceAppInfoUtils deviceAppInfoUtils = new DeviceAppInfoUtils();

        if(status){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean permissionAssert = false;
                    String brand = deviceAppInfoUtils.getAs();
                    if (brand.equals("huawei")){

                    }

                }
            }).start();
        }
        wifiManager.setWifiEnabled(status);
    }



}