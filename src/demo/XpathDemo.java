package demo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.IOException;


public class XpathDemo {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse("file:///G:\\Android_Automatic_Testing\\screen_dump\\max_game_select.uix");

        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr
                = xpath.compile("//node()[contains(@text,'游戏')]");

        //Object result = xpath.evaluate("//node()[contains(@text,'游戏')]",doc, XPathConstants.NODESET);
        Object result = expr.evaluate(doc, XPathConstants.NODE);
        Node myNode = (Node) result;
        String path = myNode.getNodeName();
        System.out.println(myNode.getAttributes().getNamedItem("text"));
        /*while(myNode.getParentNode()!=null){
            //path = +path
        }*/

    }
}
