package com.xiaowei.core.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhouyang
 * @Date 2017-04-11 下午5:38
 * @Version 1.0
 */
public class JaxbDateAdapter extends XmlAdapter<String, Date> {

    public static String STANDARM_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public Date unmarshal(String value) throws Exception {
        if (value == null) {
            return null;
        }

        DateFormat format = new SimpleDateFormat(STANDARM_DATE_FORMAT);
        return format.parse(value);
    }

    public String marshal(Date value) throws Exception {
        DateFormat format = new SimpleDateFormat(STANDARM_DATE_FORMAT);
        return format.format(value);
    }
}
