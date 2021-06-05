package de.tesd.collection;


import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedSet;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;


public class TreeSet<E> extends java.util.TreeSet<E> {


	private static final long serialVersionUID = 1316942281670940782L;


	// ========== TreeSet ==========
	
	
	public TreeSet() {
		super();
	}
	

	public TreeSet(Collection<? extends E> collection) {
		super(collection);
	}
	

	public TreeSet(Comparator<? super E> comparator) {
		super(comparator);
	}
	

	public TreeSet(SortedSet<E> sortedSet) {
		super(sortedSet);
	}
	

	@SuppressWarnings("null") @Override public @Nullable E ceiling(E key) {
		return super.ceiling(key);
	}


	@SuppressWarnings("null") @Override public @NonNull E first() throws NoSuchElementException {
		return super.first();
	}


	@SuppressWarnings("null") @Override public @Nullable E floor(E key) {
		return super.floor(key);
	}


	@SuppressWarnings("null") @Override public @Nullable E higher(E key) {
		return super.higher(key);
	}


	@SuppressWarnings("null") @Override public @NonNull E last() throws NoSuchElementException {
		return super.last();
	}


	@SuppressWarnings("null") @Override public @Nullable E lower(E key) {
		return super.lower(key);
	}
	
	
	@Override public @NonNull E pollFirst() throws NoSuchElementException {
		E result = super.pollFirst();
		if (result==null) {
			throw new NoSuchElementException();
		}
		return result;
	}


	@Override public @NonNull E pollLast() throws NoSuchElementException {
		E result = super.pollLast();
		if (result==null) {
			throw new NoSuchElementException();
		}
		return result;
	}
	
	
	@SuppressWarnings("null") @Override public NavigableSet<E> subSet(@Nullable E fromElement, boolean fromInclusive, @Nullable E toElement, boolean toInclusive) {
		return super.subSet(fromElement, fromInclusive, toElement, toInclusive);
	}
	
	
	@SuppressWarnings("null") @Override public SortedSet<E> subSet(@Nullable E fromElement, @Nullable E toElement) {
		return super.subSet(fromElement, toElement);
	}
	

}
