package com.example.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MintQ on 2018/5/24.
 */
public class XmlUtils {

    private static String xmlPath = "src\\com\\zc\\homeWork18\\MyXml.xml";

    public static void getFamilyMemebers() {

    /*
         * 创建文件工厂实例
         */
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // 如果创建的解析器在解析XML文档时必须删除元素内容中的空格，则为true，否则为false
        dbf.setIgnoringElementContentWhitespace(true);

        try {
            /*
             * 创建文件对象
             */
            DocumentBuilder db = dbf.newDocumentBuilder();// 创建解析器，解析XML文档
            Document doc = db.parse(xmlPath); // 使用dom解析xml文件

            /*
             * 历遍列表，进行XML文件的数据提取
             */
            // 根据节点名称来获取所有相关的节点
            NodeList sonlist = doc.getElementsByTagName("son");
            for (int i = 0; i < sonlist.getLength(); i++) // 循环处理对象
            {
                // 节点属性的处理
                Element son = (Element) sonlist.item(i);
                // 循环节点son内的所有子节点
                for (Node node = son.getFirstChild(); node != null; node = node
                        .getNextSibling()) {
                    // 判断是否为元素节点
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        String name = node.getNodeName();
                        String value = node.getFirstChild().getNodeValue();
                        System.out.println(name + " : " + value);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 修改
    public static void modifySon() {
        // 创建文件工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        try {
            // 从XML文档中获取DOM文档实例
            DocumentBuilder db = dbf.newDocumentBuilder();
            // 获取Document对象
            Document xmldoc = db.parse(xmlPath);

            // 获取根节点
            Element root = xmldoc.getDocumentElement();
            // 定位id为001的节点
            Element per = (Element) selectSingleNode("/father/son[@id='001']",
                    root);
            // 将age节点的内容更改为28
            per.getElementsByTagName("age").item(0).setTextContent("28");
            // 保存
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer former = factory.newTransformer();
            former.transform(new DOMSource(xmldoc), new StreamResult(new File(
                    xmlPath)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 获取目标节点，进行删除，最后保存
    public static void discardSon() {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);

        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmldoc = db.parse(xmlPath);
            // 获取根节点
            Element root = xmldoc.getDocumentElement();
            // 定位根节点中的id=002的节点
            Element son = (Element) selectSingleNode("/father/son[@id='002']",
                    root);
            // 删除该节点
            root.removeChild(son);
            // 保存
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer former = factory.newTransformer();
            former.transform(new DOMSource(xmldoc), new StreamResult(new File(
                    xmlPath)));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 新增节点
    public static void createSon() {
        // 创建文件工厂实例
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(false);

        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            // 创建Document对象
            Document xmldoc = db.parse(xmlPath);
            // 获取根节点
            Element root = xmldoc.getDocumentElement();
            // 创建节点son，设置对应的id为004
            Element son = xmldoc.createElement("son");
            son.setAttribute("id", "004");
            // 创建节点name
            Element name = xmldoc.createElement("name");
            name.setTextContent("小儿子");
            son.appendChild(name);
            // 创建节点age
            Element age = xmldoc.createElement("age");
            age.setTextContent("0");
            son.appendChild(age);
            // 把son添加到根节点中
            root.appendChild(son);
            // 保存
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer former = factory.newTransformer();
            former.transform(new DOMSource(xmldoc), new StreamResult(new File(
                    xmlPath)));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 修改节点信息
    public static Node selectSingleNode(String express, Element source) {
        Node result = null;
        //创建XPath工厂
        XPathFactory xpathFactory = XPathFactory.newInstance();
        //创建XPath对象
        XPath xpath = xpathFactory.newXPath();
        try {
            result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
            System.out.println(result);
        } catch (XPathExpressionException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    // 打印
    public static void main(String[] args) {

        getFamilyMemebers();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        modifySon();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("修改数据");
        getFamilyMemebers();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        discardSon();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("删除数据");
        getFamilyMemebers();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        createSon();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("添加数据");
        getFamilyMemebers();
    }


//    public static void main(String[] args) throws SAXException {
//        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
//        Element theBook=null, theElem=null, root=null;
//        try {
//            factory.setIgnoringElementContentWhitespace(true);
//
//            DocumentBuilder db=factory.newDocumentBuilder();
//            Document xmldoc=db.parse(new File("MyXml.xml"));
//            root=xmldoc.getDocumentElement();
//
//            //--- 新建一本书开始 ----
//            theBook=xmldoc.createElement("book");
//            theElem=xmldoc.createElement("name");
//            theElem.setTextContent("新书");
//            theBook.appendChild(theElem);
//
//            theElem=xmldoc.createElement("price");
//            theElem.setTextContent("20");
//            theBook.appendChild(theElem);
//            theElem=xmldoc.createElement("memo");
//            theElem.setTextContent("新书的更好看。");
//            theBook.appendChild(theElem);
//            root.appendChild(theBook);
//            System.out.println("--- 新建一本书开始 ----");
//            output(xmldoc);
//            //--- 新建一本书完成 ----
//            //--- 下面对《哈里波特》做一些修改。 ----
//            //--- 查询找《哈里波特》----
//            theBook=(Element) selectSingleNode("/books/book[name='哈里波特']", root);
//            System.out.println("--- 查询找《哈里波特》 ----");
//            output(theBook);
//            //--- 此时修改这本书的价格 -----
//            theBook.getElementsByTagName("price").item(0).setTextContent("15");//getElementsByTagName 返回的是NodeList，所以要跟上item(0)。另外，getElementsByTagName("price")相当于xpath 的".//price"。
//            System.out.println("--- 此时修改这本书的价格 ----");
//            output(theBook);
//            //--- 另外还想加一个属性id，值为B01 ----
//            theBook.setAttribute("id", "B01");
//            System.out.println("--- 另外还想加一个属性id，值为B01 ----");
//            output(theBook);
//            //--- 对《哈里波特》修改完成。 ----
//            //--- 要用id属性删除《三国演义》这本书 ----
//            theBook=(Element) selectSingleNode("/books/book[@id='B02']", root);
//            System.out.println("--- 要用id属性删除《三国演义》这本书 ----");
//            output(theBook);
//            theBook.getParentNode().removeChild(theBook);
//            System.out.println("--- 删除后的ＸＭＬ ----");
//            output(xmldoc);
//            //--- 再将所有价格低于10的书删除 ----
//            NodeList someBooks=selectNodes("/books/book[price<10]", root);
//            System.out.println("--- 再将所有价格低于10的书删除 ---");
//            System.out.println("--- 符合条件的书有　"+someBooks.getLength()+"本。 ---");
//            for(int i=0;i<someBooks.getLength();i++) {
//                someBooks.item(i).getParentNode().removeChild(someBooks.item(i));
//            }
//            output(xmldoc);
//            saveXml("Test1_Edited.xml", xmldoc);
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public static void output(Node node) {//将node的XML字符串输出到控制台
        TransformerFactory transFactory=TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("encoding", "utf-8");
            transformer.setOutputProperty("indent", "yes");
            DOMSource source=new DOMSource();
            source.setNode(node);
            StreamResult result=new StreamResult();
            result.setOutputStream(System.out);

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static Node selectSingleNode(String express, Object source) {//查找节点，并返回第一个符合条件节点
        Node result=null;
        XPathFactory xpathFactory=XPathFactory.newInstance();
        XPath xpath=xpathFactory.newXPath();
        try {
            result=(Node) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static NodeList selectNodes(String express, Object source) {//查找节点，返回符合条件的节点集。
        NodeList result=null;
        XPathFactory xpathFactory=XPathFactory.newInstance();
        XPath xpath=xpathFactory.newXPath();
        try {
            result=(NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void saveXml(String fileName, Document doc) {//将Document输出到文件
        TransformerFactory transFactory=TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");
            DOMSource source=new DOMSource();
            source.setNode(doc);
            StreamResult result=new StreamResult();
            result.setOutputStream(new FileOutputStream(fileName));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
