package de.tesd.collection;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.Nullable;

public class Lists {
	
	
	@SafeVarargs
	public static <T> List<T> concat(List<T>... lists) {
		return Arrays.stream(lists).flatMap(List::stream).collect(Collectors.toList());
	}
	
	
	public static <E> @Nullable E firstOr(List<E> list, E elseValue) {
		return list.isEmpty() ? elseValue : list.get(0);
	}

	
	public static <E> @Nullable E firstOrNull(List<E> list) {
		return list.isEmpty() ? null : list.get(0);
	}



}
