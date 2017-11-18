package com.ctosb.study.spider;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

public class HtmlParserTool {

    public static Set<String> extractLinks(URLConnection url, NodeFilter filter) {
        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");
            NodeList nodeList = parser.extractAllNodesThatMatch(filter);
            for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.elementAt(i);
                System.out.println(node.getText());
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            URL url = new URL("http://www.baidu.com");
            NodeFilter linkFilter = new NodeFilter() {

                public boolean accept(Node node) {
                    if (node.getText().indexOf("baidu") > 0) {
                        return true;
                    }
                    return false;
                }

            };
            HtmlParserTool.extractLinks(url.openConnection(), linkFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
