package com.facetedworlds.honeydolist.adapters;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.updatecontrols.Dependent;
import com.updatecontrols.UpdateMethod;
import com.updatecontrols.android.Update;

import facetedworlds.honeydo.model.Identity;
import facetedworlds.honeydo.model.IdentityListShare;
import facetedworlds.honeydo.model.List__name;

public class FamilyListAdapter extends ArrayAdapter<String> {
	
	private Identity identity;
	
	private Dependent depListSummaries;

	public FamilyListAdapter(Identity identity, final Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.identity = identity;
		
		depListSummaries = Update.whenNecessary(new UpdateMethod() {
			@Override
			public void update() {
				updateListSummaries();
			}
		});
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
