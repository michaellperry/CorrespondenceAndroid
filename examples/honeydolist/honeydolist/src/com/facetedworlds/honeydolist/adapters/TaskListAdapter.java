package com.facetedworlds.honeydolist.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;

import facetedworlds.honeydo.model.List;
import facetedworlds.honeydo.model.Task;
import facetedworlds.honeydo.model.Task__text;

public class TaskListAdapter extends ArrayAdapter<Task> {

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
			this.add(task);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Task task = this.getItem(position);
		LinearLayout taskItem = new LinearLayout(context);
		taskItem.setOrientation(LinearLayout.HORIZONTAL);
		CheckBox taskCompleted = new CheckBox(context);
		taskCompleted.setPadding(0, 4, 0, 0);
		taskItem.addView(taskCompleted);
		TextView taskTextView = new TextView(context);
		taskTextView.setText(getTaskText(task));
		taskTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		taskTextView.setTextSize(22.0f);
		taskTextView.setTextColor(Color.WHITE);
		taskTextView.setPadding(4, 0, 0, 0);
		taskItem.addView(taskTextView);
		return taskItem;
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
