package com.facetedworlds.honeydolist;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.facetedworlds.honeydolist.adapters.IdentityListShareComparator;
import com.facetedworlds.honeydolist.adapters.TaskListAdapter;
import com.facetedworlds.honeydolist.collaboration.SynchronizationService;
import com.mallardsoft.query.QuerySpec;
import com.mallardsoft.query.Selector;
import com.updatecontrols.Dependent;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.android.Update;

import facetedworlds.honeydo.model.IdentityListShare;
import facetedworlds.honeydo.model.List__name;

public class TaskListActivity extends Activity {

	private IdentityListShare share;
	private TextView titleTextView;
	
	private Dependent depTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.task_list);
		SynchronizationService synchronizationService = SynchronizationService.getInstance();
		
		try {
			int position = getIntent().getExtras().getInt("position");
			ArrayList<IdentityListShare> sortedShares = new ArrayList<IdentityListShare>(synchronizationService.getIdentity().activeShares());
			Collections.sort(sortedShares, new IdentityListShareComparator());
			if (position >= 0 && position < sortedShares.size()) {
				share = sortedShares.get(position);
				
				titleTextView = (TextView)findViewById(R.id.list_name);
				depTitle = Update.whenNecessary(this, new UpdateMethod() {
					@Override
					public void update() {
						titleTextView.setText(getListName());
					}
				});
		        
		        View taskListView = findViewById(R.id.taskList);
				ListView taskList = (ListView)taskListView;
				taskList.setAdapter(new TaskListAdapter(share.getList(), this, R.layout.task_summary));
				taskList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						onSelectTask(position);
					}
				});
			}
		} catch (Throwable t) {
			Log.e("TaskListActivity", t.getMessage(), t);
		}
	}

	private String getListName() {
		return QuerySpec.from(share.getList().nameCandidates())
			.selectFirstOrNull(new Selector<List__name, String>() {
				@Override
				public String select(List__name row) {
					return row.getValue();
				}
			});
	}

	private void onSelectTask(int position) {
		// TODO Auto-generated method stub
		
	}

}
