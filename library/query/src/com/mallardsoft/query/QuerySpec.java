package com.mallardsoft.query;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class QuerySpec<ROW> {

	public static <ROW> QuerySpec<ROW> from(Iterable<ROW> iterable) {
		return new QuerySpec<ROW>(iterable);
	}
	
	private Iterable<ROW> iterable;
	
	private QuerySpec(Iterable<ROW> iterable) {
		this.iterable = iterable;
	}

	public <CHILD> QuerySpec<Join<ROW, CHILD>> join(
			final Relation<ROW, CHILD> relation) {
		return new QuerySpec<Join<ROW,CHILD>>(new Iterable<Join<ROW,CHILD>> () {
			public Iterator<Join<ROW, CHILD>> iterator() {
				return new Iterator<Join<ROW,CHILD>>() {
					
					private Iterator<ROW> parentIterator = iterable.iterator();
					private Iterator<CHILD> childIterator = null;
					private boolean hasNext = false;
					private ROW parent = null;
					private CHILD child = nextChild();
					
					private CHILD nextChild() {
						hasNext = false;
						if (childIterator == null) {
							// Get the first parent.
							if (!nextParent(relation))
								return null;
						}
						
						// When the child list is exhausted, move on to the next parent.
						while (!childIterator.hasNext()) {
							if (!nextParent(relation))
								return null;
						}
						
						hasNext = true;
						return childIterator.next();
					}

					private boolean nextParent(Relation<ROW, CHILD> relation) {
						if (!parentIterator.hasNext())
							return false;
						parent = parentIterator.next();
						
						// Get the children of the first parent.
						childIterator = relation.join(parent).iterator();
						
						return true;
					}

					public boolean hasNext() {
						return hasNext;
					}

					public Join<ROW,CHILD> next() {
						// If we've exhausted the collection, throw.
						if (!hasNext)
							throw new NoSuchElementException();
						
						// Return the next pair and advance.
						Join<ROW,CHILD> retVal = new Join<ROW, CHILD>(parent, child);
						child = nextChild();
						return retVal;
					}

					public void remove() {
					}
				};
			}
		});
	}
	
	public QuerySpec<ROW> where(final Predicate<ROW> predicate) {
		return new QuerySpec<ROW>(new Iterable<ROW>() {
			public Iterator<ROW> iterator() {
				return new Iterator<ROW>() {
					
					private Iterator<ROW> iterator = iterable.iterator();
					private boolean hasNext = false;
					private ROW next = calculateNext();
					
					private ROW calculateNext() {
						// Search for the next matching row.
						hasNext = false;
						while (iterator.hasNext()) {
							ROW candidate = iterator.next();
							if (predicate.where(candidate)) {
								hasNext = true;
								return candidate;
							}
						}
						return null;
					}

					public boolean hasNext() {
						return hasNext;
					}

					public ROW next() {
						// If we've exhausted the collection, throw.
						if (!hasNext)
							throw new NoSuchElementException();
						
						// Return the next row and advance.
						ROW retVal = next;
						next = calculateNext();
						return retVal;
					}

					public void remove() {
					}
				};
			}
		});
	}
	
	public QuerySpec<ROW> distinct() {
				
		return new QuerySpec<ROW>(new Iterable<ROW>() {
			@Override
			public Iterator<ROW> iterator() {
				return new Iterator<ROW>() {
					
					private Iterator<ROW> iterator = iterable.iterator();
					private boolean hasNext = false;
					private Set<ROW> rows = new HashSet<ROW>(); 
					private ROW next = calculateNext();
					
					private ROW calculateNext() {
						// Search for the next matching row.
						hasNext = false;
						while (iterator.hasNext()) {
							ROW candidate = iterator.next();
							if (!rows.contains(candidate)) {
								rows.add(candidate);
								hasNext = true;
								return candidate;
							}
						}
						return null;
					}

					public boolean hasNext() {
						return hasNext;
					}

					public ROW next() {
						// If we've exhausted the collection, throw.
						if (!hasNext)
							throw new NoSuchElementException();
						
						// Return the next row and advance.
						ROW retVal = next;
						next = calculateNext();
						return retVal;
					}

					public void remove() {
					}
				};
			}
		});
	}
	
	public <RESULT> Iterable<RESULT> select(final Selector<ROW, RESULT> selector) {
		return new Iterable<RESULT>() {
			public Iterator<RESULT> iterator() {
				return new Iterator<RESULT>() {
					
					private Iterator<ROW> iterator = iterable.iterator();

					public boolean hasNext() {
						return iterator.hasNext();
					}

					public RESULT next() {
						return selector.select(iterator.next());
					}

					public void remove() {
					}
					
				};
			}
		};
	}

	public Iterable<ROW> selectRow() {
		return iterable;
	}

	public ROW selectFirstOrNull() {
		Iterator<ROW> iterator = iterable.iterator();
		if (iterator.hasNext())
			return iterator.next();
		else
			return null;
	}

	public ROW selectOne() {
		// The iterator must produce only one row.
		Iterator<ROW> iterator = iterable.iterator();
		ROW row = null;
		int count = 0;
		while (iterator.hasNext()) {
			++count;
			row = iterator.next();
		}
		if (count == 0)
			throw new IllegalStateException("This query produces no results.");
		if (count != 1)
			throw new IllegalStateException("This query produces more than one result.");
		return row;
	}
	
	public boolean any() {
		// Return true if the iterator produces at least one result.
		return iterable.iterator().hasNext();
	}
	
}
