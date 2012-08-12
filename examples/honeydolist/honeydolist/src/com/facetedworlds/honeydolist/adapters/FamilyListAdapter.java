package com.facetedworlds.honeydolist.adapters;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.updatecontrols.Dependent;
import com.updatecontrols.android.Update;
import com.updatecontrols.android.UpdateAdapter;

import facetedworlds.honeydo.model.Identity;
import facetedworlds.honeydo.model.IdentityListShare;
import facetedworlds.honeydo.model.List__name;

public class FamilyListAdapter extends ArrayAdapter<String> {
	
	private Identity identity;
	
	private Dependent depListSummaries;
	private ArrayList<String> listSummaries;

	public FamilyListAdapter(Identity identity, final Activity context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.identity = identity;
		
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
		Log.i("FamilyListAdapter", "updateListSummaries");
		
		listSummaries = new ArrayList<String>();
		ArrayList<IdentityListShare> sortedShares = new ArrayList<IdentityListShare>(identity.activeShares());
		Collections.sort(sortedShares, new IdentityListShareComparator());
		this.clear();
		for (IdentityListShare share : sortedShares) {
			String name = "";
			for (List__name candidate : share.getList().nameCandidates()) {
				name = candidate.getValue();
				break;
			}
			listSummaries.add(name);
		}
	}
	
	private void assignListSummaries() {
		this.clear();
		for (String name : listSummaries) {
			this.add(name);
		}
	}
}
