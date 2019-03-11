package com.wenyizai.wangfuwen.wenyizai.dcs;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.duer.dcs.androidapp.DcsSampleApplication;
import com.baidu.duer.dcs.androidapp.DcsSampleOAuthActivity;
import com.baidu.duer.dcs.oauth.api.IOauth;
import com.baidu.duer.dcs.oauth.api.OauthPreferenceUtil;
import com.wenyizai.wangfuwen.wenyizai.service.MyApplication;

/**
 * Created by wangfuwen on 2017/11/21.
 */

public class MyOauthImpl implements IOauth {
    @Override
    public String getAccessToken() {
        Log.i("xxxxxxxxx","getAccessToken");
        Log.i("xxxxxxxxx", MyApplication.getAppContext().toString());
//        return OauthPreferenceUtil.getAccessToken(DcsSampleApplication.getInstance());
        return OauthPreferenceUtil.getAccessToken(MyApplication.getAppContext());
    }

    @Override
    public void authorize() {
        Intent intent = new Intent(MyApplication.getAppContext(), MyDcsSampleOAuthActivity.class);
        intent.putExtra("START_TAG", "RESTART");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getAppContext().startActivity(intent);
    }

    @Override
    public boolean isSessionValid() {
        Log.i("xxxxxxxxx","isSessionValid");
        String accessToken = getAccessToken();
        Log.i("xxxxxxxxx",accessToken);
        long createTime = OauthPreferenceUtil.getCreateTime(MyApplication.getAppContext());
        long expires = OauthPreferenceUtil.getExpires(MyApplication.getAppContext()) + createTime;
        return !TextUtils.isEmpty(accessToken) && expires != 0 && System.currentTimeMillis() < expires;
    }
}