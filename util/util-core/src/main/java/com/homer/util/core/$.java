package com.homer.util.core;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by arigolub on 3/14/16.
 */
public class $<T> {

    // region initialize

    private final Stream<T> backing;

    private $(Stream<T> backing)
    {
        this.backing = backing;
    }

    public static <T> $<T> of(Collection<T> collection)
    {
        return of(collection.stream());
    }

    public static <T> $<T> of(Iterable<T> iterable)
    {
        return of(StreamSupport.stream(iterable.spliterator(), false));
    }

    public static <T> $<T> of(T element)
    {
        return of(Stream.of(element));
    }

    public static <T> $<T> of(Stream<T> stream)
    {
        return new $<>(stream);
    }

    // endregion

    // region functions

    public T first() {
        return backing.findFirst().orElse(null);
    }

    public T first(Predicate<T> predicate) {
        return backing.filter(predicate).findFirst().orElse(null);
    }

    public List<T> toList() {
        return backing.collect(Collectors.toList());
    }

    public <T, K> List<K> toList(Function<T, K> func) {
        return backing.map(t -> func.apply((T) t)).collect(Collectors.toList());
    }

    public <T extends IId> Map<Long, T> toIdMap() {
        return backing.map(t -> (T) t).collect(Collectors.toMap(T::getId, t -> t));
    }

    public <T extends IId> List<Long> toIdList() {
        return backing.map(t -> ((T) t).getId()).collect(Collectors.toList());
    }

    public List<T> filterToList(Predicate<T> predicate) {
        return backing.filter(predicate).collect(Collectors.toList());
    }

    public $<T> filter(Predicate<T> predicate) {
        return $.of(backing.filter(predicate));
    }

    public int reduceToInt(Function<T, Integer> mapper) {
        return backing.map(mapper).reduce(Integer::sum).orElse(null);
    }

    // endregion
}
