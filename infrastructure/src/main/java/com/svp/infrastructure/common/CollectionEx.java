package com.svp.infrastructure.common;

import java.util.ArrayList;

public class CollectionEx {
    public interface Lambda<T>{
        boolean predicate(T item);
    }
    public static <T> T first(T[] iterable, Lambda<T> first){
        for (T item : iterable){
            if(first.predicate(item)){
                return item;
            }
        }
        return null;
    }

    public static <T> boolean isNullOrEmpty(T[] iterable){
        return iterable == null || iterable.length == 0;
    }

    public static  <T> void AddRange(ArrayList<T> to, T[] from){
        for (int i =0;i < from.length; ++i){
            to.add(from[i]);
        }
    }
}
