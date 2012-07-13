package com.facetedworlds.honeydolist.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facetedworlds.honeydolist.R;
import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;

import facetedworlds.honeydo.model.List;
import facetedworlds.honeydo.model.Task;
import facetedworlds.honeydo.model.Task__text;

public class TaskListAdapter extends ArrayAdapter<Task> {

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
			this.add(task);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Task task = this.getItem(position);
		View taskSummaryView = parent.findViewById(R.layout.task_summary);
		TextView taskTextView = (TextView)taskSummaryView.findViewById(R.id.task_text);
		taskTextView.setText(getTaskText(task));
		return taskSummaryView;
	}

	private String getTaskText(Task task) {
		String text = "";
		Log.i("TaskListAdapter", "updateListSummaries - add task");
		for (Task__text candidate : task.textCandidates()) {
			text = candidate.getValue();
			Log.i("TaskListAdapter", "updateListSummaries - add task " + text);
			break;
		}
		return text;
	}
	
}
