package com.wenyizai.wangfuwen.wenyizai.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by user on 15/8/28.
 */
public class HttpUtil {

    private  static  final  String BASIC_URL = "http://123.57.11.138:12306/";

    //实例化对象
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setTimeout(30000);
    }

    public static  AsyncHttpClient getClient(){
        return client;
    }

    public  static void get(String url,TextHttpResponseHandler res){
        client.get(getAbsoluteUrl(url),res);
    }

    public  static void post(String url,TextHttpResponseHandler res){
        client.post(getAbsoluteUrl(url),res);
    }

    public static String getAbsoluteUrl(String relativeUrl){
        return BASIC_URL+relativeUrl;

    }


}
