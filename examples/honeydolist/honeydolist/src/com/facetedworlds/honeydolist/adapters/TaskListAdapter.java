package com.facetedworlds.honeydolist.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.updatecontrols.Dependent;
import com.updatecontrols.android.Update;
import com.updatecontrols.android.UpdateAdapter;

import facetedworlds.honeydo.model.List;
import facetedworlds.honeydo.model.Task;

public class TaskListAdapter extends ArrayAdapter<TaskAdapter> {

	private List list;
	private Activity context;
	
	private Dependent depListSummaries;
	private ArrayList<TaskAdapter> taskAdapters;
	
	public TaskListAdapter(List list, final Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		
		this.list = list;
		this.context = context;
		
		depListSummaries = Update.whenNecessary(new UpdateAdapter() {
			@Override
			public void update() {
				updateListSummaries();
			}
			
			@Override
			public void assign() {
				assignListSummaries();
			}
		});
	}

	private void updateListSummaries() {
		taskAdapters = new ArrayList<TaskAdapter>();
		for (Task task : list.tasks()) {
			taskAdapters.add(new TaskAdapter(task, context));
		}
	}
	
	private void assignListSummaries() {
		this.clear();
		for (TaskAdapter taskAdapter : taskAdapters) {
			this.add(taskAdapter);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("TaskListAdapter", "getView");
		TaskAdapter taskAdapter = this.getItem(position);
		return taskAdapter.getView();
	}
	
}
