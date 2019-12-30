package testobjectmanager.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class App {
    public static void main(String[] args) throws IOException {
        String pathToObjectFolder;
        String queryTag;
        String queryValue;

        if (args.length == 0) {
            pathToObjectFolder = "./Object Repository";
            queryTag = "tag";
            queryValue = "input";
        } else {
            pathToObjectFolder = args[0] + "/Object Repository";
            queryTag = args[1];
            queryValue = args[2];
        }

        long beginTime = System.nanoTime();
        List<String> results = searchForTagWithValueInFolder(queryTag, queryValue, pathToObjectFolder);
        long endTime = System.nanoTime();
        System.out.println(
                "Search elapsed: " + (endTime - beginTime) + " nanoseconds, with " + results.size() + " results.");
        System.out.println("--------------------------------------------");
        System.out.println("Files containing query: " + queryTag + "=" + queryValue);
        System.out.println("--------------------------------------------");
        for (String result : results) {
            System.out.println(result);
        }
    }

    public static List<String> searchForTagWithValueInFolder(String queryTag, String queryValue, String katalonProject)
            throws IOException {
        long beginTime = System.nanoTime();
        String pathToObjectFolder = katalonProject + "/Object Repository";
        List<String> files = Files.find(Paths.get(pathToObjectFolder), 999, (a, p) -> {
            return p.isRegularFile();
        })
                .parallel()
                .map(path -> path.toAbsolutePath().toString())
                .filter(strPath -> xmlContainsQueryWithValue(new File(strPath), queryTag, queryValue))
                .collect(Collectors.toCollection(ArrayList<String>::new));
        System.out.println(queryTag + "=" + queryValue + " " + (System.nanoTime() - beginTime) / 1000000);
        return files;
    }

    private static boolean xmlContainsQueryWithValue(File file, String tag, String value) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName(tag);
            for (int nodeIndex = 0; nodeIndex < nodes.getLength(); ++nodeIndex) {
                if (nodes.item(nodeIndex).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element element = (Element) nodes.item(nodeIndex);
                    if (element.getTextContent().contains(value)) {
                        return true;
                    }
                }
            }

            NodeList nList = doc.getElementsByTagName("webElementProperties");
            for (int i = 0; i < nList.getLength(); i++) {
                org.w3c.dom.Node node = nList.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) node;
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String val = eElement.getElementsByTagName("value").item(0).getTextContent();
                    if (name.equals(tag.trim()) && val.contains(value.trim())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }
}
