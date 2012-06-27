package com.updatecontrols;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public abstract class Precedent {

	private class DependentNode {
		public WeakReference<Dependent> dependent;
		public DependentNode next;
	}
	
	private DependentNode firstDependent;
	private List<DependentListener> dependentListeners = new ArrayList<DependentListener>();
	
	protected void gainDependent() {
		for (DependentListener listener : dependentListeners) {
			listener.onGainDependent();
		}
	}
	
	protected void loseDependent() {
		for (DependentListener listener : dependentListeners) {
			listener.onLoseDependent();
		}
	}
	
	protected void recordDependent() {
        // Get the current dependent.
        Dependent update = Dependent.getCurrentUpdate();
        if (update != null && !contains(update) && update.addPrecedent(this)) {
            if (insert(update))
                gainDependent();
        }
        else if (!any()) {
            // Though there is no lasting dependency, someone
            // has shown interest.
            gainDependent();
            loseDependent();
        }
	}
	
	protected void makeDependentsOutOfDate() {
        // When I make a dependent out-of-date, it will
        // call RemoveDependent, thereby removing it from
        // the list.
        Dependent first;
        while ((first = first()) != null) {
            first.makeOutOfDate();
        }
	}
	
	protected void removeDependent(Dependent dependent) {
        if (delete(dependent))
            loseDependent();
	}
	
	public void addDependentListener(DependentListener listener) {
		dependentListeners.add(listener);
	}
	
	public boolean hasDependents() {
		return any();
	}

	private synchronized boolean insert(Dependent update) {
        DependentNode priorFirstDependent = firstDependent;
		firstDependent = new DependentNode();
        firstDependent.dependent = new WeakReference<Dependent>(update);
        firstDependent.next = priorFirstDependent;
        return priorFirstDependent == null;
	}

	private synchronized boolean delete(Dependent dependent) {
        DependentNode prior = null;
        for (DependentNode current = firstDependent; current != null; current = current.next) {
            Dependent currentDependent = current.dependent.get();
			if (currentDependent == null || currentDependent == dependent)
            {
                if (prior == null)
                    firstDependent = current.next;
                else
                    prior.next = current.next;
            }
            else
                prior = current;
        }
        return firstDependent == null;
	}

	private synchronized boolean contains(Dependent update) {
        for (DependentNode current = firstDependent; current != null; current = current.next) {
			Dependent currentDependent = current.dependent.get();
			if (currentDependent == update)
                return true;
		}
        return false;
	}

	private synchronized boolean any() {
        return firstDependent != null;
	}

	private synchronized Dependent first() {
        while (firstDependent != null) {
            Dependent currentDependent = firstDependent.dependent.get();
            if (currentDependent != null)
                return currentDependent;
            else
                firstDependent = firstDependent.next;
        }
        return null;
	}
	
}
