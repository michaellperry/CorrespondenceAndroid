package com.updatecontrols.android;

import android.os.Handler;

import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;

public class Update {
	
	private static int updateCount = 0;
	private static Handler handler;
	
	public static Dependent whenNecessary(UpdateMethod updateMethod) {
		if (handler == null) {
			handler = new Handler();
		}
		
		final Dependent dependent = new Dependent(updateMethod);
		final Runnable updateRunnable = new Runnable() {
			@Override
			public void run() {
				updateCount++;
				try {
					dependent.onGet();
				} finally {
					updateCount--;
				}
			}
		};
		dependent.addInalidatedListener(new InvalidatedListener() {
			@Override
			public void invalidated() {
				handler.post(updateRunnable);
			}
		});
		handler.post(updateRunnable);
		return dependent;
	}
	
	public static boolean isNotHappening() {
		return updateCount == 0;
	}

}
