package com.facetedworlds.honeydolist;

import android.os.Handler;

import com.updatecontrols.correspondence.strategy.TaskDispatcher;

public class ActivityTaskDispatcher implements TaskDispatcher {

	private Handler handler;

	public ActivityTaskDispatcher() {
		this.handler = new Handler();
	}

	@Override
	public void runOnUiThread(Runnable task) {
		handler.post(task);
	}

}
