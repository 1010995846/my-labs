package cn.cidea.core.utils;

import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSONObject;
import org.w3c.dom.Document;

import javax.xml.bind.*;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author Charlotte
 */
public class XmlUtils {

    public static String toXml(Object obj) throws JAXBException {
        if(obj == null){
            return null;
        }
        // 创建输出流
        StringWriter sw = new StringWriter();
        // 利用jdk中自带的转换类实现
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        // 格式化xml输出的格式
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        // 将对象转换成输出流形式的xml
        marshaller.marshal(obj, sw);
        return sw.toString();
    }

    public static <T> T parseObject(String xmlStr, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(clazz);
        // 进行将Xml转成对象的核心接口
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader reader = new StringReader(xmlStr);
        return  (T) unmarshaller.unmarshal(reader);
    }


}
