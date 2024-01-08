package com.ddy.dyy.web.lang;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bean copy with happy
 */
public final class BeanUtils2 {

    private BeanUtils2() {

    }


    public static <T, K> List<K> copyList(List<T> source, Class<K> target) {

        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copy(e, target)).collect(Collectors.toList());
    }


    public static <T> T copy(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }
        try {
            T newInstance = target.newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, newInstance);
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
