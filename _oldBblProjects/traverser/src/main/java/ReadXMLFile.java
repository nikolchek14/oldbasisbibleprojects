import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadXMLFile {
        private static PrintWriter writer;
        private static List<String> lista =new ArrayList<>();

        public static void main(String argv[]) {
                try {
                        File file = new File("src/main/resources/BblXML.xml");
                        writer = new PrintWriter("src/main/resources/BblXMLOutput.txt", "UTF-8");

                        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                        Document doc = dBuilder.parse(file);

                        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

                        if (doc.hasChildNodes()) {

                                printNote(doc.getChildNodes());

                        }
                        writer.println(lista);
                        writer.close();
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }
        }

        private static void printNote(NodeList nodeList) {

                for (int count = 0; count < nodeList.getLength(); count++) {

                        Node tempNode = nodeList.item(count);

                        // make sure it's element node.
                        if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                                // get node name and value
                                //writer.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                               // writer.println("Node Value =" + tempNode.getTextContent());

                                writer.println("\nNode Name =" + tempNode.getNodeName());
                                if(!lista.contains(tempNode.getNodeName())){
                                     lista.add(tempNode.getNodeName());
                                }
                                if (tempNode.hasAttributes()) {

                                        // get attributes names and values
                                        NamedNodeMap nodeMap = tempNode.getAttributes();

                                        for (int i = 0; i < nodeMap.getLength(); i++) {

                                                Node node = nodeMap.item(i);
                                                //writer.println("attr name : " + node.getNodeName());
                                               // writer.println("attr value : " + node.getNodeValue());

                                        }

                                }

                                if (tempNode.hasChildNodes()) {

                                        // loop again if has child nodes
                                        printNote(tempNode.getChildNodes());

                                }

                               // writer.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

                        }

                }

        }
}