package com.facetedworlds.honeydolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.facetedworlds.honeydolist.adapters.FamilyListAdapter;
import com.facetedworlds.honeydolist.collaboration.SynchronizationService;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        SynchronizationService synchronizationService = SynchronizationService.getInstance();
		try {
			synchronizationService.start(this);
	        
	        View familyListView = findViewById(R.id.familyList);
			ListView familyList = (ListView)familyListView;
			familyList.setAdapter(new FamilyListAdapter(synchronizationService.getIdentity(), this, R.layout.list_summary));
			familyList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onSelectList(position);
				}
			});
		} catch (Throwable t) {
			Log.e("MainActivity", t.getMessage(), t);
		}
    }

	private void onSelectList(int position) {
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		this.startActivity(new Intent(this, TaskListActivity.class).putExtras(bundle));
	}
}