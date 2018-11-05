package com.xiaowei.accountweb.test;


import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Lenovo on 2017/10/16.
 */
public class SimpleTest {


    @Test
    public void test(){
        Stream.iterate(0, integer -> integer+1)
                .skip(50).limit(100).peek(integer -> System.out.println(integer)).collect(Collectors.toList());
    }
}
