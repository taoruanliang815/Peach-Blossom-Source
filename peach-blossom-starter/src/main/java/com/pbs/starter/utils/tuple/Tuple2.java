package com.pbs.starter.utils.tuple;

import lombok.Data;

import java.io.Serializable;

/**
 * @author taoruanliang
 * @date 2022/2/17 10:43
 */
@Data
public class Tuple2<K, V> implements Serializable {

    private static final long serialVersionUID = 4476671416675072079L;
    private K key;

    private V value;


    public Tuple2() {
    }

    public Tuple2(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public static <A, B> Tuple2<A, B> with(A key, B value) {
        return new Tuple2(key, value);
    }

}
