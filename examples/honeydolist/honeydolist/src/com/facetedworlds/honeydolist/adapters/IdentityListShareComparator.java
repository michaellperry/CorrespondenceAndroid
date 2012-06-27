/**
 * 
 */
package com.facetedworlds.honeydolist.adapters;

import java.util.Comparator;

import com.facetedworlds.honeydolist.model.IdentityListShare;

public final class IdentityListShareComparator implements Comparator<IdentityListShare> {
	@Override
	public int compare(IdentityListShare lhs, IdentityListShare rhs) {
		return lhs.ordinal() - rhs.ordinal();
	}
}