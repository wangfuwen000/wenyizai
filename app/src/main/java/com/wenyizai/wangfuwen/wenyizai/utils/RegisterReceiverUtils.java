package com.wenyizai.wangfuwen.wenyizai.utils;

/**
 * Created by wangfuwen on 2017/1/6.
 */

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.ArrayMap;

import java.util.Map;

/**
 * 广播注册解注工具
 *
 * @author Sahadev
 *
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class RegisterReceiverUtils {
    private static Map<Class<?>, BroadcastReceiver> MAPS = new ArrayMap<Class<?>, BroadcastReceiver>();

    /**
     * @param context
     * @param broadcastReceiverClass
     * @param action
     */


    public static void registerBroadcastReceiver(Context context, Class<?> broadcastReceiverClass, String action) {
        IntentFilter filter = new IntentFilter(action);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            BroadcastReceiver broadcastReceiver = (BroadcastReceiver) broadcastReceiverClass.newInstance();
            if (broadcastReceiver != null) {
                MAPS.put(broadcastReceiverClass, broadcastReceiver);
                context.registerReceiver(broadcastReceiver, filter);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void unregisterBroadcastReceiver(Context context, Class<?> broadcastReceiverClass) {
        BroadcastReceiver broadcastReceiver = MAPS.get(broadcastReceiverClass);
        context.unregisterReceiver(broadcastReceiver);
        MAPS.remove(broadcastReceiverClass);

    }

}