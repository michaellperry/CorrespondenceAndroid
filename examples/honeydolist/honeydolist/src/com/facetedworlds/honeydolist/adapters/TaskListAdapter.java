package com.facetedworlds.honeydolist.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.updatecontrols.Dependent;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.android.Update;

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
		
		depListSummaries = Update.whenNecessary(context, new UpdateMethod() {
			@Override
			public void update() {
				updateListSummaries();
			}
		});
	}

	private void updateListSummaries() {
		this.clear();
		for (Task task : list.tasks()) {
			this.add(new TaskAdapter(task, context));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("TaskListAdapter", "getView");
		TaskAdapter taskAdapter = this.getItem(position);
		return taskAdapter.getView();
	}
	
}
