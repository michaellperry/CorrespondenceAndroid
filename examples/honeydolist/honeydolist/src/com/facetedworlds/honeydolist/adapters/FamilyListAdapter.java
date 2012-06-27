package com.facetedworlds.honeydolist.adapters;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.facetedworlds.honeydolist.model.Identity;
import com.facetedworlds.honeydolist.model.IdentityListShare;
import com.facetedworlds.honeydolist.model.List__name;
import com.updatecontrols.Dependent;
import com.updatecontrols.InvalidatedListener;
import com.updatecontrols.UpdateMethod;

public class FamilyListAdapter extends ArrayAdapter<String> {
	
	private Identity identity;
	
	private Dependent depListSummaries;

	public FamilyListAdapter(Identity identity, final Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.identity = identity;
		
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
		Log.i("FamilyListAdapter", "updateListSummaries");
		
		ArrayList<IdentityListShare> sortedShares = new ArrayList<IdentityListShare>(identity.activeShares());
		Collections.sort(sortedShares, new IdentityListShareComparator());
		this.clear();
		for (IdentityListShare share : sortedShares) {
			String name = "";
			for (List__name candidate : share.getList().nameCandidates()) {
				name = candidate.getValue();
				break;
			}
			this.add(name);
		}
	}
}
