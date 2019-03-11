package com.wenyizai.wangfuwen.wenyizai.engine;

import android.util.Log;

import com.wenyizai.wangfuwen.wenyizai.activity.base.BaseActivity;
import com.wenyizai.wangfuwen.wenyizai.net.DefaultThreadPool;
import com.wenyizai.wangfuwen.wenyizai.net.HttpRequest;
import com.wenyizai.wangfuwen.wenyizai.net.RequestCallback;
import com.wenyizai.wangfuwen.wenyizai.net.RequestParameter;
import com.wenyizai.wangfuwen.wenyizai.net.URLData;
import com.wenyizai.wangfuwen.wenyizai.net.UrlConfigManager;

import java.util.List;


public class RemoteService {
	private static RemoteService service = null;

	private RemoteService() {

	}

	public static synchronized RemoteService getInstance() {
		if (RemoteService.service == null) {
			RemoteService.service = new RemoteService();
		}
		return RemoteService.service;
	}

	public void invoke(final BaseActivity activity,
			final String apiKey,
			final List<RequestParameter> params,
			final RequestCallback callBack) {
		final URLData urlData = UrlConfigManager.findURL(activity, apiKey);

		HttpRequest request = activity.getRequestManager().createRequest(
				urlData, params, callBack);
		DefaultThreadPool.getInstance().execute(request);
	}
}