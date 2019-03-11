package com.wenyizai.wangfuwen.wenyizai.service;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * HTTP请求服务器
 *
 * @author bc
 */
public class HttpRequestServer {

    private static String strurl = "http://123.57.11.138:12306/hotel";

    HttpRequestServer() {

    }

    public static String getStrResponse() {
        String result = null;
        URL url = null;
        try {
            url = new URL(strurl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);

//            urlConnection.setRequestProperty("content-type", "application/json");

            urlConnection.connect();

            DataOutputStream dop = new DataOutputStream(urlConnection.getOutputStream());
            dop.writeBytes("param=" + URLEncoder.encode("haha", "utf-8"));
            dop.flush();
            dop.close();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String readLine="";
            while ((readLine =bufferedReader.readLine()) != null){
                result = readLine;
            }

            bufferedReader.close();
            urlConnection.disconnect();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}

