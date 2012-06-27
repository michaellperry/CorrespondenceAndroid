package com.facetedworlds.honeydolist;

import android.app.Activity;

import com.updatecontrols.correspondence.strategy.TaskDispatcher;

public class ActivityTaskDispatcher implements TaskDispatcher {

	private Activity activity;

	public ActivityTaskDispatcher(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void runOnUiThread(Runnable task) {
		activity.runOnUiThread(task);
	}

}
