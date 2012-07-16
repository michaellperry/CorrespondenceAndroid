package com.facetedworlds.honeydolist.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardsoft.query.QuerySpec;
import com.mallardsoft.query.Selector;
import com.updatecontrols.Dependent;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.android.Update;

import facetedworlds.honeydo.model.Task;
import facetedworlds.honeydo.model.Task__text;

public class TaskAdapter {

	private Task task;
	private Activity context;
	
	private Dependent depText;
	private TextView taskTextView;
	
	public TaskAdapter(Task task, final Activity context) {
		super();
		this.task = task;
		this.context = context;
	}

	public View getView() {
		LinearLayout taskItem = new LinearLayout(context);
		taskItem.setOrientation(LinearLayout.HORIZONTAL);
		CheckBox taskCompleted = new CheckBox(context);
		taskCompleted.setPadding(0, 4, 0, 0);
		taskItem.addView(taskCompleted);
		taskTextView = new TextView(context);
		taskTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		taskTextView.setTextSize(22.0f);
		taskTextView.setTextColor(Color.WHITE);
		taskTextView.setPadding(4, 0, 0, 0);
		taskItem.addView(taskTextView);
		
		depText = Update.whenNecessary(context, new UpdateMethod() {
			@Override
			public void update() {
				taskTextView.setText(getTaskText());
			}
		});
		
		return taskItem;
	}

	private String getTaskText() {
		return QuerySpec.from(task.textCandidates())
			.selectFirstOrNull(new Selector<Task__text, String>() {
				@Override
				public String select(Task__text row) {
					return row.getValue();
				}
			});
	}
	
}
