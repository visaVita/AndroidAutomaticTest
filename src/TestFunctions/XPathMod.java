package TestFunctions;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;

public class XPathMod {
    private String myXpathStr;
    private XPathFactory xPathFactory;
    private XPath xPath;
    private XPathExpression xPathExpr;

    private DocumentBuilderFactory domFactory;
    private DocumentBuilder docBuilder;
    private Document doc;

    XPathMod(String XmlUri){
        domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        try {
            docBuilder = domFactory.newDocumentBuilder();
            doc = docBuilder.parse(XmlUri);

            xPathFactory = XPathFactory.newInstance();
            xPath = xPathFactory.newXPath();
            // TODO: 2019/3/3 添加解析好的，或者待解析的xml语句（到时候构造函数可能要加参数）
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String generateXpathByText(String name) throws XPathExpressionException {
        xPathExpr = xPath.compile("//node()[contains(@text," + name + ")]");
        String myText;
        Object result = xPathExpr.evaluate(doc,XPathConstants.NODESET);
        NodeList myNodeList = (NodeList)result;
        if(myNodeList.getLength()>1) {
            for (int i = 0; i < myNodeList.getLength();i++){
                myText = myNodeList.item(i).getAttributes().getNamedItem("text").getNodeValue();
                if (myText.equals(name)){
                    return "//node()[@text=" + name + ")]";
                }
                /*todo 这里没考虑如果用户输入的不规范怎么办？只考虑了比如“登录”和“跳过登录”正则匹配相同的问题。
                 *todo 后面再作考虑
                 */
            }
        }
        return "//node()[contains(@text," + name + ")]";
    }

    public String generateXpathByContentDesc(String desc) throws XPathExpressionException {
        xPathExpr = xPath.compile("//node()[contains(@content-desc," + desc + ")]");
        String myText;
        Object result = xPathExpr.evaluate(doc,XPathConstants.NODESET);
        NodeList myNodeList = (NodeList)result;
        if(myNodeList.getLength()>1) {
            for (int i = 0; i < myNodeList.getLength();i++){
                myText = myNodeList.item(i).getAttributes().getNamedItem("content-desc").getNodeValue();
                if (myText.equals(desc)){
                    return "//node()[@content-desc=" + desc + ")]";
                }
                /*todo 这里没考虑如果用户输入的不规范怎么办？只考虑了比如“登录”和“跳过登录”正则匹配相同的问题。
                 *todo 后面再作考虑
                 */
            }
        }
        return "//node()[contains(@content-desc," + desc + ")]";
    }

    public XPathExpression getxPathExpr() {
        return xPathExpr;
    }
}
