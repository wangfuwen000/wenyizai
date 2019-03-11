package com.wenyizai.wangfuwen.wenyizai.activity.weather;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.networkbench.com.google.gson.Gson;
import com.wenyizai.wangfuwen.wenyizai.R;
import com.wenyizai.wangfuwen.wenyizai.activity.base.BaseActivity;
import com.wenyizai.wangfuwen.wenyizai.engine.RequestAsyncTask;
import com.wenyizai.wangfuwen.wenyizai.entity.WeatherEntity;
import com.wenyizai.wangfuwen.wenyizai.entity.weatherinfo;


public class WeatherByGsonActivity extends BaseActivity {
	TextView tvCity;
	TextView tvCityId;

	@Override
	protected void initVariables() {
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.activity_weather);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvCityId = (TextView) findViewById(R.id.tvCityId);
	}

	@Override
	protected void loadData() {
		String url = "http://www.weather.com.cn/data/sk/101010100.html";

		RequestAsyncTask task = new RequestAsyncTask() {

			@Override
			public void onSuccess(String content) {
				// 第3种写法，基于GSON
				Gson gson = new Gson();
				WeatherEntity weatherEntity = gson.fromJson(content,
						WeatherEntity.class);
				weatherinfo weatherInfo = weatherEntity.getWeatherInfo();
				if (weatherInfo != null) {
					tvCity.setText(weatherInfo.getCity());
					tvCityId.setText(weatherInfo.getCityid());
				}
			}

			@Override
			public void onFail(String errorMessage) {
				new AlertDialog.Builder(WeatherByGsonActivity.this)
						.setTitle("出错啦").setMessage(errorMessage)
						.setPositiveButton("确定", null).show();
			}
		};
		task.execute(url);
	}


}
