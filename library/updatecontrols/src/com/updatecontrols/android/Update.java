package com.updatecontrols.android;

import android.app.Activity;

import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;

public class Update {

	public static Dependent whenNecessary(final Activity context, UpdateMethod updateMethod) {
		final Dependent dependent = new Dependent(updateMethod);
		final Runnable updateRunnable = new Runnable() {
			@Override
			public void run() {
				dependent.onGet();
			}
		};
		dependent.addInalidatedListener(new InvalidatedListener() {
			@Override
			public void invalidated() {
				context.runOnUiThread(updateRunnable);
			}
		});
		context.runOnUiThread(updateRunnable);
		return dependent;
	}

}
