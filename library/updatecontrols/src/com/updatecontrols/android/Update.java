package com.updatecontrols.android;

import android.os.Handler;

import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;

public class Update {
	
	private static int updateCount = 0;
	private static Handler handler;
	
	public static Dependent whenNecessary(final UpdateAdapter updateAdapter) {
		if (handler == null) {
			handler = new Handler();
		}
		
		final Dependent dependent = new Dependent(updateAdapter);
		final Runnable updateRunnable = new Runnable() {
			@Override
			public void run() {
				updateCount++;
				try {
					dependent.onGet();
					updateAdapter.assign();
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
