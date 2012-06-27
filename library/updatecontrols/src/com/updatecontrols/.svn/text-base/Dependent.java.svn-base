package com.updatecontrols;

import java.util.ArrayList;
import java.util.List;

public class Dependent extends Precedent {
	
	private static ThreadLocal<Dependent> currentUpdate = new ThreadLocal<Dependent>();
	
	public static Dependent getCurrentUpdate() {
		return currentUpdate.get();
	}
	
	private UpdateMethod updateMethod;
	
	private List<InvalidatedListener> invalidatedListeners = new ArrayList<InvalidatedListener>();
	private enum StatusType {
		OutOfDate,
		UpToDate,
		Updating,
		UpdatingAndOutOfDate,
		Disposed
	};
	private StatusType status = StatusType.OutOfDate;
	private class PrecedentNode {
		public Precedent precedent;
		public PrecedentNode next;
	}
	private PrecedentNode firstPrecedent = null;

	public Dependent(UpdateMethod updateMethod) {
		this.updateMethod = updateMethod;
	}

	public void addInalidatedListener(InvalidatedListener invalidatedListener) {
		invalidatedListeners.add(invalidatedListener);
	}

	public void onGet() {
		// Ensure that the attribute is up-to-date.
		if (makeUpToDate()) {
			// Establish dependency between the current update
			// and this attribute.
			recordDependent();
		}
		else {
			// We're still not up-to-date (because of a concurrent change).
			// The current update should similarly not be up-to-date.
            Dependent currentUpdate = Dependent.currentUpdate.get();
			if (currentUpdate != null)
				currentUpdate.makeOutOfDate();
		}
	}
	
	public void dispose() {
		makeOutOfDate();
		status = StatusType.Disposed;
	}
	
	public synchronized boolean isUpToDate() {
		return status == StatusType.UpToDate;
	}
	
	public synchronized boolean isNotUpdating() {
		return status != StatusType.Updating && status != StatusType.UpdatingAndOutOfDate; 
	}
	
	public void touch() {
		makeUpToDate();
	}

	public synchronized void makeOutOfDate() {
		if (
				status == StatusType.UpToDate ||
				status == StatusType.Updating ) {
			// Tell all precedents to forget about me.
            for (PrecedentNode current = firstPrecedent; current != null; current = current.next)
                current.precedent.removeDependent(this);

            firstPrecedent = null;

			// Make all indirect dependents out-of-date, too.
			makeDependentsOutOfDate();

			if ( status == StatusType.UpToDate )
				status = StatusType.OutOfDate;
			else if ( status == StatusType.Updating )
				status = StatusType.UpdatingAndOutOfDate;

			for (InvalidatedListener listener : invalidatedListeners) {
				listener.invalidated();
			}
		}
	}

	private boolean makeUpToDate() {
		StatusType formerStatus;
		boolean isUpToDate = true;

		synchronized ( this ) {
			// Get the former status.
			formerStatus = status;

			// Reserve the right to update.
			if ( status == StatusType.OutOfDate )
				status = StatusType.Updating;
		}

		if (formerStatus == StatusType.Updating ||
			formerStatus == StatusType.UpdatingAndOutOfDate) {
			// Report cycles.
			reportCycles();
			//MLP: Don't throw, because this will mask any exception in an update which caused reentrance.
			//throw new InvalidOperationException( "Cycle discovered during update." );
		}
		else if (formerStatus == StatusType.OutOfDate) {
			// Push myself to the update stack.
			Dependent stack = currentUpdate.get();
            currentUpdate.set(this);

			// Update the attribute.
			try {
				updateMethod.update();
			}
			finally {
				// Pop myself off the update stack.
                currentUpdate.set(stack);

				synchronized (this) {
					// Look for changes since the update began.
					if (status == StatusType.Updating) {
						status = StatusType.UpToDate;
					}
					else if (status == StatusType.UpdatingAndOutOfDate) {
						status = StatusType.OutOfDate;
						isUpToDate = false;
					}
				}
			}
		}

		return isUpToDate;
	}

	public synchronized boolean addPrecedent(Precedent precedent) {
		if ( status == StatusType.Updating ) {
            PrecedentNode priorFirstPrecedent = firstPrecedent;
            firstPrecedent = new PrecedentNode();
            firstPrecedent.precedent = precedent;
			firstPrecedent.next = priorFirstPrecedent;
            return true;
		}
        return false;
	}

	private void reportCycles() {
		// TODO Report cycles.
	}

}
