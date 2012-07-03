package com.facetedworlds.honeydolist.adapters;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;

import facetedworlds.honeydo.model.List;
import facetedworlds.honeydo.model.Task;
import facetedworlds.honeydo.model.Task__text;

public class TaskListAdapter extends ArrayAdapter<String> {

	private List list;
	
	private Dependent depListSummaries;
	
	public TaskListAdapter(List list, final Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		
		this.list = list;
		depListSummaries = new Dependent(new UpdateMethod() {
			@Override
			public void update() {
				updateListSummaries();
			}
		});
		
		depListSummaries.addInalidatedListener(new InvalidatedListener() {
			@Override
			public void invalidated() {
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						depListSummaries.onGet();
					}
				});
			}
		});
		depListSummaries.onGet();
	}

	private void updateListSummaries() {
		Log.i("TaskListAdapter", "updateListSummaries");
		
		this.clear();
		for (Task task : list.tasks()) {
			String text = "";
			Log.i("TaskListAdapter", "updateListSummaries - add task");
			for (Task__text candidate : task.textCandidates()) {
				text = candidate.getValue();
				Log.i("TaskListAdapter", "updateListSummaries - add task " + text);
				break;
			}
			this.add(text);
		}
	}

}
