package eg.edu.alexu.csd.oop.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBMS implements Database {
    private static DBMS firstInstance = null;
    private String databaseName;
    private String[] arr;

    public DBMS(String databaseName) {
        this.databaseName = databaseName;
    }
//    public static DBMS getInstance(String databaseName){
//        if(firstInstance==null){
//            firstInstance = new DBMS();
//            firstInstance.setDatabaseName(databaseName);
//        }
//        return firstInstance;
//    }


    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String createDatabase(String databaseName, boolean dropIfExists) {
        if (dropIfExists) {
            File file = new File(databaseName);
            file.delete();
        }
        File file = new File(databaseName);
        System.getProperty("databaseName.separator");
        file.mkdirs();
        return file.getPath();
    }

    public void dropDatabase(String databaseName, boolean dropIfExists) {
        if (dropIfExists) {
            File file = new File(databaseName);
            File[] files = file.listFiles();
            for (File f : files) {
                f.delete();
            }
            file.delete();
        }
    }

    @Override
    public boolean executeStructureQuery(String query) throws SQLException {
        File file = new File(query);
        return file.exists();
    }

    @Override
    public Object[][] executeQuery(String query) throws SQLException {
        int pivot = 0;
        String word = "from";
        String word2 = "where";
        String w = " ";
        String colname =" " ;
        String ope = " ";
        String re;
        int limit = 0;
        String[] aa = query.split("[ ]+");

        String[] arr2;

        if (aa[1].equals("*")) {
            if (!query.toLowerCase().contains(word2)) {
                pivot = 1;
                w = aa[3];
            } else {
                pivot = 2;
                colname=aa[5] ;
                w = aa[3];
                ope=aa[6] ;
                limit= Integer.parseInt(aa[7]);
                
            }
        } else {
            re = query.substring(0, query.toLowerCase().indexOf(word.toLowerCase()));
            query = query.substring(query.toLowerCase().indexOf(word.toLowerCase()));
            this.arr = re.split("[, ]+");
            arr2 = query.split("[ ]+");
            w = arr2[1];
            pivot = 3;

        }

        File file = new File(databaseName + "\\" + w + ".xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.parse(file);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        int count = 0;

        for (int i = 0; i < list.getLength(); i++) {
            count = 0;
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList names = element.getChildNodes();
                for (int j = 0; j < names.getLength(); j++) {
                    Node r = names.item(j);
                    if (r.getNodeType() == Node.ELEMENT_NODE && i == 0) {
                        Element name = (Element) r;
                        arr1[i][count] = name.getTagName();
                        count++;
                    } else if (r.getNodeType() == Node.ELEMENT_NODE && i != 0) {
                        Element name = (Element) r;
                        arr1[i][count] = name.getTextContent();
                        count++;
                    }
                }
            }
        }


        if (pivot == 1) {
            for (int k = 0; k < list.getLength(); k++) {
                for (int j = 0; j < nocol; j++) {
                    System.out.print(arr1[k][j]);

                }
                System.out.println();
            }
            return arr1;
        } else if (pivot == 3) {
            int b = 0;
            for (int i = 0; i < this.arr.length; i++) {
                for (int j = 0; j < nocol; j++) {
                    if (arr1[0][j].equals(this.arr[i])) {
                        b++;
                        break;

                    }
                }
            }
            Object[][] n = new Object[list.getLength()][b];
            int v = 0;
            for (int t = 0; t < this.arr.length; t++) {
                for (int j = 0; j < nocol; j++) {
                    if (arr1[0][j].equals(this.arr[t])) {
                        for (int k = 0; k < list.getLength(); k++) {
                            n[k][v] = arr1[k][j];

                        }
                        v++;
                    }
                }
            }
            for (int k = 0; k < list.getLength(); k++) {
                for (int j = 0; j < b; j++) {
                    System.out.print(n[k][j]);

                }
                System.out.println();
            }
            return n;

        } else {
            int norows =0 ;
            for(int i =0;i<list.getLength();i++){
                for(int j=0 ;j<nocol;j++){
                    if(arr1[0][j].equals(colname)&&i!=0){
                       switch (ope){
                           case ">":
                               if( Integer.parseInt((String) arr1[i][j]) > limit){
                                   norows ++ ;
                               }
                               break;
                           case "<":
                               if( Integer.parseInt((String) arr1[i][j])< limit){
                                   norows ++ ;
                               }
                               break;
                           case "=":
                               if( Integer.parseInt((String) arr1[i][j]) == limit){
                                   norows ++ ;
                               }
                               break;

                           case "<=":
                               if( Integer.parseInt((String) arr1[i][j]) <= limit){
                                   norows ++ ;
                               }
                               break;
                           case ">=":
                               if( Integer.parseInt((String) arr1[i][j]) >= limit) {
                                   norows++;
                               }
                               break;
                           case "!=":
                               if( Integer.parseInt((String) arr1[i][j]) !=limit){
                                   norows ++ ;
                               }
                               break;
                       }
                    }
                }
            }
            norows++ ;
            Object[][] arr3 = new Object[norows][nocol];
            int e=1;

                for (int j = 0; j < nocol; j++) {


                        for (int i = 1; i < list.getLength(); i++) {
                            if (arr1[0][j].equals(colname)&&i!=0) {

                                switch (ope) {
                                    case ">":
                                        if (Integer.parseInt((String) arr1[i][j]) > limit) {
                                           for(int k=0;k<nocol;k++){
                                               arr3[e][k]=arr1[i][k] ;
                                           }
                                           e++ ;
                                        }
                                        break ;
                                    case "<":
                                        if (Integer.parseInt((String) arr1[i][j]) < limit) {
                                            for(int k=0;k<nocol;k++){
                                                arr3[e][k]=arr1[i][k] ;
                                            }
                                            e++ ;
                                        }
                                        break ;
                                    case "=":
                                        if (Integer.parseInt((String) arr1[i][j]) == limit) {
                                            for(int k=0;k<nocol;k++){
                                                arr3[e][k]=arr1[i][k] ;
                                            }
                                            e++ ;
                                        }
                                    case "!=":
                                        if (Integer.parseInt((String) arr1[i][j]) != limit) {
                                            for(int k=0;k<nocol;k++){
                                                arr3[e][k]=arr1[i][k] ;
                                            }
                                        }
                                        break ;
                                    case "<=":
                                        if (Integer.parseInt((String) arr1[i][j]) <= limit) {
                                            for(int k=0;k<nocol;k++){
                                                arr3[e][k]=arr1[i][k] ;
                                            }
                                            e++ ;
                                        }
                                        break ;
                                    case ">=":
                                        if (Integer.parseInt((String) arr1[i][j]) >= limit) {
                                            for(int k=0;k<nocol;k++){
                                                arr3[e][k]=arr1[i][k] ;
                                            }
                                            e++ ;
                                        }
                                        break ;


                            }
                        }
                    }
                }
                for(int i=0;i<nocol;i++){
                    arr3[0][i]=arr1[0][i];
                }
            for (int k = 0; k < norows; k++) {
                for (int j = 0; j < nocol; j++) {
                    System.out.print(arr3[k][j]);

                }
                System.out.println();
            }
            return arr3;
        }
    }





    @Override
    public int executeUpdateQuery(String query) throws SQLException {
        return 0;
    }

}

