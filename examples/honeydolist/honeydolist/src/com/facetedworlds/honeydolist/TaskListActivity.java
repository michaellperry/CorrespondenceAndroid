package com.facetedworlds.honeydolist;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facetedworlds.honeydolist.adapters.IdentityListShareComparator;
import com.facetedworlds.honeydolist.collaboration.SynchronizationService;
import com.facetedworlds.honeydolist.model.IdentityListShare;
import com.facetedworlds.honeydolist.model.List__name;

public class TaskListActivity extends Activity {

	private IdentityListShare share;

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
				
				TextView title = (TextView)findViewById(R.id.list_name);
				String name = "";
				for (List__name candidate : share.getList().nameCandidates()) {
					name = candidate.getValue();
					break;
				}
				title.setText(name);
			}
		} catch (Throwable t) {
			Log.e("TaskListActivity", t.getMessage(), t);
		}
	}

}
