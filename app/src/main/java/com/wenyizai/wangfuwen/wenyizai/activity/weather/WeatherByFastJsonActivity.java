package com.wenyizai.wangfuwen.wenyizai.activity.weather;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.base.BaseActivity;
import com.wenyizai.wangfuwen.wenyizai.engine.RemoteService;
import com.wenyizai.wangfuwen.wenyizai.engine.RequestAsyncTask;
import com.wenyizai.wangfuwen.wenyizai.entity.WeatherEntity;
import com.wenyizai.wangfuwen.wenyizai.entity.weatherinfo;
import com.wenyizai.wangfuwen.wenyizai.net.RequestCallback;
import com.wenyizai.wangfuwen.wenyizai.net.RequestParameter;

import java.util.ArrayList;

public class WeatherByFastJsonActivity extends BaseActivity {
	TextView tvCity;
	TextView tvCityId;
	private RequestCallback weatherCallback = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
	}

	@Override
	protected void initVariables() {

	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_weather);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvCityId = (TextView) findViewById(R.id.tvCityId);
	}


	//first version
//	@Override
//	protected void loadData() {
//		String url = "http://www.weather.com.cn/data/sk/101010100.html";
//
//		RequestAsyncTask task = new RequestAsyncTask() {
//
//			@Override
//			public void onSuccess(String result) {
//				Log.i("333",result.toString());
//				// 第2种写法，基于fastJSON
//				//result = "{\"city\":\"北京\",\"cityid\":\"101010100\",\"temp\":\"18\",\"WD\":\"东南风\",\"WS\":\"1级\",\"SD\":\"17%\",\"WSE\":\"1\",\"time\":\"17:05\",\"isRadar\":\"1\",\"Radar\":\"JC_RADAR_AZ9010_JB\",\"njd\":\"暂无实况\",\"qy\":\"1011\",\"rain\":\"0\"}";
//				WeatherEntity weatherEntity = JSON.parseObject(result, WeatherEntity.class);
//				Log.i("333","city:" + weatherEntity.getWeatherInfo().getCity()+" id:"+ weatherEntity.getWeatherInfo().getCityid());
//				if (weatherEntity.getWeatherInfo() != null) {
//					tvCity.setText(weatherEntity.getWeatherInfo().getCity());
//					tvCityId.setText(weatherEntity.getWeatherInfo().getCityid());
//				}
//			}
//
//			@Override
//			public void onFail(String errorMessage) {
//				new AlertDialog.Builder(WeatherByFastJsonActivity.this)
//						.setTitle("出错啦").setMessage(errorMessage)
//						.setPositiveButton("确定", null).show();
//			}
//		};
//		task.execute(url);
//	}



	@Override
	protected void loadData() {
		weatherCallback = new RequestCallback() {

			@Override
			public void onSuccess(String content) {
				WeatherEntity weatherEntity = JSON.parseObject(content,
						WeatherEntity.class);
				if (weatherEntity != null) {
					tvCity.setText(weatherEntity.getWeatherInfo().getCity());
					tvCityId.setText(weatherEntity.getWeatherInfo().getCityid());

				}
			}

			@Override
			public void onFail(String errorMessage) {
				new AlertDialog.Builder(WeatherByFastJsonActivity.this)
						.setTitle("出错啦").setMessage(errorMessage)
						.setPositiveButton("确定", null).show();
			}
		};

		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		RequestParameter rp1 = new RequestParameter("cityid", "101010100");
		RequestParameter rp2 = new RequestParameter("city", "北京");
		params.add(rp1);
		params.add(rp2);

		RemoteService.getInstance().invoke(this, "getWeatherInfo", params, weatherCallback);
	}


}
