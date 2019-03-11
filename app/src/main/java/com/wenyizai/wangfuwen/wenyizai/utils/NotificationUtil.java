package com.wenyizai.wangfuwen.wenyizai.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.service.MyApplication;

/**
 * Created by wangfuwen on 2017/1/8.
 */

public class NotificationUtil extends Notification {

    Context context = MyApplication.getAppContext();
    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notification;

    public NotificationUtil(int sign) {
        notification = new NotificationCompat.Builder(context)
                .setContentTitle("This is content titel")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_empty)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.huoying))
                .build();
        notificationManager.notify(sign,notification);
    }

    public NotificationUtil(int sign, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        notification = new NotificationCompat.Builder(context)
                .setContentTitle("This is content titel")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_empty)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.huoying))
                .setContentIntent( pendingIntent )
                .build();
        notificationManager.notify(sign,notification);
    }

    public void canelNotification(int sign){
        notificationManager.cancel(sign);
    }


}
