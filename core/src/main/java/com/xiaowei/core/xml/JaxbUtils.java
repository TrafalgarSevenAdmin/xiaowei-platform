package com.xiaowei.core.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author zhouyang
 * @Date 2017-04-11 下午5:38
 * @Description xml
 * @Version 1.0
 */
public class JaxbUtils {

	/**
     * 将对象转换成ｘｍｌ
	 *
	 * @param obj
	 * @return
	 */
	public static String objToXml(Object obj) throws JAXBException {
		JAXBContext jbc = JAXBContext.newInstance(obj.getClass());
		Marshaller ms = jbc.createMarshaller();
		ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		ms.marshal(obj, sw);
		return sw.toString();
	}

	/**
	 * 将ＸＭＬ转换成对象
	 * @param xml
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xmlTObj(String xml, Class<T> clazz) throws JAXBException {
		JAXBContext jbc;
		jbc = JAXBContext.newInstance(clazz);
		Unmarshaller ms = jbc.createUnmarshaller();
		T t = (T) ms.unmarshal(new StringReader(xml));
		return t;
	}

}
