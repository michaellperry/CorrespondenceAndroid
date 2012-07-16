package com.facetedworlds.honeydolist.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;

import facetedworlds.honeydo.model.List;
import facetedworlds.honeydo.model.Task;

public class TaskListAdapter extends ArrayAdapter<TaskAdapter> {

	private List list;
	private Activity context;
	
	private Dependent depListSummaries;
	
	public TaskListAdapter(List list, final Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		
		this.list = list;
		this.context = context;
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
			this.add(new TaskAdapter(task, context));
		}
		Log.i("TaskListAdapter", "updateListSummaries - done");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("TaskListAdapter", "getView");
		TaskAdapter taskAdapter = this.getItem(position);
		return taskAdapter.getView();
	}
	
}
