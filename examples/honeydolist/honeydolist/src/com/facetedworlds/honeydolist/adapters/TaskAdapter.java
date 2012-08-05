package com.facetedworlds.honeydolist.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mallardsoft.query.QuerySpec;
import com.mallardsoft.query.Selector;
import com.updatecontrols.Dependent;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.android.Update;

import facetedworlds.honeydo.model.Domain;
import facetedworlds.honeydo.model.Task;
import facetedworlds.honeydo.model.TaskComplete;
import facetedworlds.honeydo.model.TaskCompleteUndo;
import facetedworlds.honeydo.model.Task__text;

public class TaskAdapter {

	private Task task;
	private Activity context;
	
	private Dependent depText;
	private TextView taskTextView;
	
	private Dependent depCompleted;
	private CheckBox completedCheckBox;
	
	public TaskAdapter(Task task, final Activity context) {
		super();
		this.task = task;
		this.context = context;
	}

	public View getView() {
		LinearLayout taskItem = new LinearLayout(context);
		taskItem.setOrientation(LinearLayout.HORIZONTAL);
		completedCheckBox = new CheckBox(context);
		completedCheckBox.setPadding(0, 4, 0, 0);
		taskItem.addView(completedCheckBox);
		taskTextView = new TextView(context);
		taskTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		taskTextView.setTextSize(22.0f);
		taskTextView.setTextColor(Color.WHITE);
		taskTextView.setPadding(4, 0, 0, 0);
		taskItem.addView(taskTextView);
		
		depText = Update.whenNecessary(new UpdateMethod() {
			@Override
			public void update() {
				taskTextView.setText(getTaskText());
			}
		});
		
		depCompleted = Update.whenNecessary(new UpdateMethod() {
			@Override
			public void update() {
				completedCheckBox.setChecked(getTaskCompleted());
			}
		});
		completedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (Update.isNotHappening())
					setTaskCompleted(isChecked);
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
	
	private boolean getTaskCompleted() {
		return QuerySpec.from(task.complete())
			.any();
	}

	private void setTaskCompleted(boolean isChecked) {
		if (isChecked) {
			Domain theDomain = task.getCommunity().addFact(new Domain());
			task.getCommunity().addFact(new TaskComplete(theDomain, task));
		}
		else {
			for (TaskComplete taskComplete : task.complete()) {
				taskComplete.getCommunity().addFact(new TaskCompleteUndo(taskComplete));
			}
		}
	}
	
}
