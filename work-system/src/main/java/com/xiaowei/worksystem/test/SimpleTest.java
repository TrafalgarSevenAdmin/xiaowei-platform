package com.xiaowei.worksystem.test;


import com.xiaowei.worksystem.entity.ServiceItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/10/16.
 */
public class SimpleTest {



    @Test
    public void testStream(){
        List<ServiceItem> collect = new ArrayList<>();
        ServiceItem serviceItem1 = new ServiceItem();
        serviceItem1.setCharge(true);
        serviceItem1.setOutToll(1.11);
        collect.add(serviceItem1);
        ServiceItem serviceItem2 = new ServiceItem();
        serviceItem2.setCharge(true);
        serviceItem2.setOutToll(2.22);
        collect.add(serviceItem2);
        ServiceItem serviceItem3 = new ServiceItem();
        serviceItem3.setCharge(true);
        serviceItem3.setOutToll(3.33);
        collect.add(serviceItem3);
        ServiceItem serviceItem4 = new ServiceItem();
        serviceItem4.setCharge(true);
        serviceItem4.setOutToll(4.44);
        collect.add(serviceItem4);
        //根据保外价格计算
        double sum = collect.stream().map(serviceItem -> serviceItem.getOutToll()).mapToDouble(value -> value != null ? value : 0.00).sum();
        Double aDouble1 = collect.stream().map(serviceItem -> serviceItem.getOutToll()).reduce((aDouble, aDouble2) -> aDouble + aDouble2).get();
        System.out.println(sum);
        System.out.println(aDouble1);
    }
}
