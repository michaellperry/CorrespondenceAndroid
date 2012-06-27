package com.updatecontrols;

import java.util.HashMap;
import java.util.Map;

public class RecycleBin<T extends Disposable> implements Disposable {

	private Map<T, T> recyclableObjects = new HashMap<T, T>();
	
	public RecycleBin() {
		// Construct an empty recycle bin.
	}
	
	public RecycleBin(Iterable<T> objects) {
		// Add all objects to the recycle bin.
		for (T object: objects)
			recyclableObjects.put(object, object);
	}
	
	public T extract(T prototype) {
		// See if a matching object is already in the recycle bin.
		T object = recyclableObjects.get(prototype);
		if (object == null) {
			// No, so use the prototype.
			return prototype;
		}
		else {
			// Yes, so extract it.
			prototype.dispose();
			recyclableObjects.remove(object);
			return object;
		}
	}
	
	public void dispose() {
		// Dispose all objects remaining in the recycle bin.
		for (T object: recyclableObjects.values()) {
			object.dispose();
		}
	}
}
