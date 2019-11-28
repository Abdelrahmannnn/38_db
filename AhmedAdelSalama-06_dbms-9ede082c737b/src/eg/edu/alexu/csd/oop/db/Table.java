package eg.edu.alexu.csd.oop.db;

//import com.sun.java.util.jar.pack.Package;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.w3c.dom.Node ;
import  org.w3c.dom.NodeList ;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Table {
    private String name;
    private String databaseName;
    private String columns;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;
    Element root;
    Element element;


    public Table(String databaseName, String name, String columns) {
        this.name = name;
        this.databaseName = databaseName;
        this.columns = columns;
    }


    public void insert_to_table(String columnsf, String values, String filename) {

        String[] column = columns.split("[( , )]+");
        String[] columns = new String[0];
        if (columnsf != null) {
            columns = columnsf.split("[( , )]+");
        }
        String[] value = values.split("[( , )]+");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            File file = new File(databaseName + "\\" + filename + ".xml");
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file.getPath());
            Element root = document.getDocumentElement();
            Element element = document.createElement("row");
//           creating columns
            int j = 1;
            for (int i = 1; i < column.length; i += 2) {
                Element subElement = document.createElement(column[i]);
                Text value1;
                if (columnsf == null) {
                    value1 = document.createTextNode(value[j]);
                    j++;
                } else if (columns[j].contentEquals(column[i])) {
                    value1 = document.createTextNode(value[j]);
                    j++;
                } else
                    value1 = document.createTextNode("null");
                subElement.appendChild(value1);
//               grouping columns
                element.appendChild(subElement);
            }
//           adding element to root
            root.appendChild(element);
//            adding root to documemt
            // document.appendChild(root);
//            writing from document(temp) to file
            DOMSource source = new DOMSource(document);
            Result result = new StreamResult(file.getPath());
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable(boolean dropIfExists) {
        String[] column = columns.split("[( , )]+");

        if (dropIfExists) {
            File file = new File(databaseName + "\\" + name + ".xml");
            file.delete();
        }
//        writing xml file using DOM parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element root = document.createElement(name);
            Element element = document.createElement("row");

//           creating columns
            for (int i = 1; i < column.length; i += 2) {
                Element subElement = document.createElement(column[i]);
                /*
                 * I added below how to add data to element and commented the line to validate the solution
                 * in case if you want to check my code
                 **/


//                Text value = document.createTextNode("Demo");
//                subElement.appendChild(value);


//               grouping columns
                element.appendChild(subElement);
            }
//           adding element to root
            root.appendChild(element);
//            adding root to documemt
            document.appendChild(root);
//            writing from document(temp) to file
            DOMSource source = new DOMSource(document);
            File file = new File(databaseName + "\\" + name + ".xml");
            Result result = new StreamResult(file);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);


            document = builder.newDocument();
            Element xsSchema = document.createElement("xs:schema");
            xsSchema.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
//            xsSchema.setAttribute("targetNamespace" , "http://www.javatpoint.com");
//            xsSchema.setAttribute("xmlns" , "http://www.javatpoint.com");
//            xsSchema.setAttribute("elementFormDefault" , "qualified");
            Element xsElement = document.createElement("xs:element");
            xsElement.setAttribute("name", "row");
            Element xsComplexType = document.createElement("xs:complexType");
            Element xsSequence = document.createElement("xs:sequence");
            for (int i = 1; i < column.length; i++) {
                Element xsCell = document.createElement("xs:element");
                xsCell.setAttribute("name", column[i]);
                i++;
                xsCell.setAttribute("type", column[i]);
                xsSequence.appendChild(xsCell);
            }
            xsComplexType.appendChild(xsSequence);
            xsElement.appendChild(xsComplexType);
            xsSchema.appendChild(xsElement);
            document.appendChild(xsSchema);
            source = new DOMSource(document);
            file = new File(databaseName + "\\" + name + ".xsd");
            result = new StreamResult(file);
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);


//            SchemaFactory schemaFactory =
//                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            Schema schema = schemaFactory.newSchema(new File(databaseName+"\\"+name+".xsd"));
//            Validator validator = schema.newValidator();
//            validator.validate(new StreamSource(new File(databaseName+"\\"+name+".xml")));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public void dropTable(boolean dropIfExists) {
        if (dropIfExists) {
            File file = new File(databaseName + "\\" + name + ".xml");
            file.delete();
        }
    }

    public boolean executeStructureQuery(String query) throws SQLException {
        File file = new File(query);
        return file.exists();
    }

    public void select(String query) {
        File file = new File(databaseName + "\\" + query + ".xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Object[][] arr = new Object[0][];
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            NodeList list = document.getElementsByTagName("row");
            int nocol = 0;

            Node q = list.item(0);
            if (q.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) q;
                NodeList names = element.getChildNodes();
                for (int j = 0; j < names.getLength(); j++) {
                    Node r = names.item(j);
                    if (r.getNodeType() == Node.ELEMENT_NODE) {
                        Element name = (Element) r;
                        nocol++;

                    }
                }
            }
            Object[][] arr1 = new Object[list.getLength()][nocol];
            int count= 0 ;

            for (int i = 0; i < list.getLength(); i++) {
                count = 0 ;
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    NodeList names = element.getChildNodes();
                    for (int j = 0; j < names.getLength(); j++) {
                        Node r = names.item(j);
                        if (r.getNodeType() == Node.ELEMENT_NODE&&i==0) {
                            Element name = (Element) r;
                            arr1[i][count] = name.getTagName();
                            count++ ;
                        } else if(r.getNodeType() == Node.ELEMENT_NODE&&i!=0){
                            Element name = (Element) r;
                            arr1[i][count] = name.getTextContent();
                            count++ ;
                        }
                    }
                }
            }


            for (int k = 0; k < list.getLength(); k++) {
                for (int j = 0; j < nocol; j++) {
                    System.out.print(arr1[k][j]);

                }
                System.out.println();
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
