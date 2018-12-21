package sc.ustc.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 解析XML用到的工具类
 */
public class XMLUtil {

	public static Document getXmlDoc(File file) {

		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(file);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("xml文档路径错误");
		}
		return doc;
	}

    /**
     * 获取第一层根节点
     * @param src_file
     * @return
     */
	public static Element getRootElement(File src_file) {
		return getXmlDoc(src_file).getRootElement();
	}

    /**
     * 获取第二层节点
     * @param file
     * @param elementType
     * @return
     */
    public static List<Element> getSubElementOfRoot(File file, String elementType){
        Element root = getRootElement(file);
        return root.elements(elementType);
    }

    /**
     * 获取第三层节点
     * @param file
     * @param parent
     * @param elementType
     * @return
     */
	public static List<Element> getThirdLevelElements(File file, String parent, String elementType){
		Element root = getRootElement(file);
		Element element = root.element(parent);
		return element.elements(elementType);
	}

    /**
     * 获取当前节点子节点，用来通过第三层节点获取第四层节点
     * @param parent
     * @param elementType
     * @return
     */
	public static List<Element> getSubElements(Element parent, String elementType){
		return parent.elements(elementType);
	}

    /**
     * 解析全部ThirdLevelElememt--parent为第三层被解析节点，获取其类型为elementType中名为elementName子节点及属性
     * @param file
     * @param elementType
     * @param elementName
     * @return
     */
    public static Map<String, Object> parseThirdLevelElememt(File file, String parent, String elementType, String elementName) {
        List<Element> elementList = getThirdLevelElements(file, parent, elementType);

        if (elementList != null) {
            for (Element element : elementList) {
                String element_name = element.attribute("name").getText();
                if (elementName.equals(element_name)) {
                    Map<String, Object> map = getElementsAttrs(element);
                    map.put("element", element);
                    return map;
                }
            }
        }
        return null;
    }
	
	/**
	 * 获取节点的所有属性
	 * @param element
	 * @return
	 */
	public static Map<String, Object> getElementsAttrs(Element element){
		List<Attribute> attributes = element.attributes();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Attribute attribute : attributes) {
			map.put(attribute.getName(), attribute.getValue());
		}
		return map;
	}
	
	/**
	 * 查询所有secondeLevel所有指定属性值
	 * @param file
	 * @param elementType
	 * @param attrName
	 * @return
	 */
	public static List<String> getRElementAttrByName(File file, String elementType, String attrName){
		List<Element> rElements = getSubElementOfRoot(file, elementType);
		List<String> list = new ArrayList<>();
		for (Element rElement : rElements) {
			list.add(rElement.attribute(attrName).getValue());
		}
		return list;
	}
	
	/**
	 * 获取全部secondeLevel全部属性
	 * @param file
	 * @param elementType
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> getAllRElementAttrs(File file, String elementType) throws Exception{
		List<Element> rElements = getSubElementOfRoot(file, elementType);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Element rElement : rElements) {
			List<Attribute> attributes = rElement.attributes();
			Map<String, String> map = new HashMap<String, String>();
			for (Attribute attribute : attributes) {
				map.put(attribute.getName(), attribute.getValue());
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 获取名为resultName的result节点type值和value值
	 * @param actionElement
	 * @param resultName
	 * @return
	 */
	public static Map<String, String> getReslutInfoByName(Element actionElement, String resultName) {

		Map<String, String> map = new HashMap<String, String>();
		List<Element> elements = actionElement.elements("result");
		for (Element element : elements) {
			if (resultName.equals(element.attributeValue("name")) && element.getText() != null) {
				map.put("type", element.attributeValue("type"));
				map.put("value", element.getText());
				return map;
			}
		}
		return null;

	}

	/**
	 * 将map以规定日志格式记录在E:/logger.xml
	 * @param map
	 * @throws Exception
	 */
	public static void map2xml(Map<String, String> map) throws Exception {
		File file = new File("E:/logger.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		Element root = null;
		if (file.exists() && file.length() != 0) {
			doc = getXmlDoc(file);
			root = doc.getRootElement();
		} else {
			doc = DocumentHelper.createDocument();
			root = doc.addElement("log");
		}
		if (map != null) {
			Element actionElement = root.addElement("action");
			Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String, String> entry = entries.next();
				actionElement.addElement(entry.getKey()).addText(entry.getValue());
			}
			writeXML(doc, file);
		}
	}
	
	/**
	 * 将document对象转换为XML
	 * @param document
	 * @param file
	 * @throws Exception
	 */
	public static void writeXML(Document document, File file) throws Exception {
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileWriter(file),outputFormat);
        writer.write(document);
        writer.close();
    }

}
