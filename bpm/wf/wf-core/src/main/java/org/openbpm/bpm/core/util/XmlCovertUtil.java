package org.openbpm.bpm.core.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XmlCovertUtil {
    @SafeVarargs
    public static <T> T covert2Object(String xml, Class<? extends Object>... classes) throws JAXBException, UnsupportedEncodingException {
        return (T)JAXBContext.newInstance(classes).createUnmarshaller().unmarshal(new ByteArrayInputStream(xml.getBytes("utf-8")));
    }

    public static String covert2Xml(Object serObj) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(new Class[]{serObj.getClass()});
        StringWriter out = new StringWriter();
        Marshaller m = jc.createMarshaller();
        m.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
        m.setProperty("jaxb.encoding", "utf-8");
        m.marshal(serObj, out);
        return out.toString();
    }
}
