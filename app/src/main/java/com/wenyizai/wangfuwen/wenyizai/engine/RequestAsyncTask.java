package com.wenyizai.wangfuwen.wenyizai.engine;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.util.Log;

import com.wenyizai.wangfuwen.wenyizai.entity.Response;
import com.wenyizai.wangfuwen.wenyizai.utils.ToStringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public abstract class RequestAsyncTask 
		extends AsyncTask<String, Void, Response> {
	public abstract void onSuccess(String content);

	public abstract void onFail(String errorMessage);

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Response doInBackground(String... url) {
		return getResponseFromURL(url[0]);
	}

	@Override
	protected void onPostExecute(Response response) {
		if(response.isError()) {
			onFail(response.getErrorMessage());
		} else {
			onSuccess(response.getResult());
		}
	}

	private Response getResponseFromURL(String url) {
		Response response = new Response();
		HttpURLConnection connection = null;
		URL url1= null;
		String strResponse = null;
		try {
			url1 = new URL(url);
			connection = (HttpURLConnection) url1.openConnection();
			// 设置请求方法，默认是GET
			connection.setRequestMethod("GET");
			// 设置字符集
			connection.setRequestProperty("Charset", "UTF-8");
			// 设置文件类型
			connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			if(connection.getResponseCode() == 200){
				InputStream is = connection.getInputStream();
				strResponse = ToStringUtils.inputStreamToString(is);
				//Log.i("xxxx",strResponse.toString());
			}
		} catch (Exception e) {
			response.setError(true);
			response.setErrorMessage(e.getMessage());
		}

		if (strResponse == null) {
			response.setError(true);
			response.setErrortype(-1);
			response.setErrorMessage("网络异常");
		} else {
			response.setError(false);
			response.setErrortype(0);
			response.setResult(strResponse);

		}

		return response;
	}
}
