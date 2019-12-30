package testobjectmanager.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.lang.System;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class App {
    public static void main(String[] args) throws IOException {
        String pathToObjectFolder;
        if (args.length <= 1) {
            pathToObjectFolder = "./Object Repository";
        } else {
            pathToObjectFolder = args[1] + "./Object Repository";
        }

        String queryTag = "randomTag";
        String queryValue = "randomValue";

        long beginTime = System.nanoTime();
        List<String> results = searchForTagWithValueInFolder(queryTag, queryValue, pathToObjectFolder);
        long endTime = System.nanoTime();
        System.out.println("Search elapsed: " + (endTime - beginTime) + "with " + results.size() + " results.");
    }


    private static ArrayList<String> searchForTagWithValueInFolder(
            String queryTag, String queryValue, String pathToObjectFolder) throws IOException {

        ArrayList<File> allRsFiles = getAllRsFilesInFolder(pathToObjectFolder, true);
        ArrayList<String> results = new ArrayList<String>();

        for (File rsFile : allRsFiles) {
            if (xmlContainsQueryWithValue(rsFile, queryTag, queryValue)) {
                results.add(rsFile.getCanonicalPath());
            }
        }

        return results;
    }

    private static ArrayList<File> getAllRsFilesInFolder(String folder, boolean recursive) throws IOException {
        File folderFile = new File(folder);
        ArrayList<File> files = new ArrayList<File>();

        for (File file : folderFile.listFiles()) {
            if (file.isFile()) {
                if (file.getName().toLowerCase().endsWith(".rs")) {
                    files.add(file);
                }
            } else if (recursive) {
                files.addAll(getAllRsFilesInFolder(file.getCanonicalPath(), recursive));
            }
        }

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
				Element element = (Element) nodes.item(nodeIndex);
				if (element.getTextContent().contains(value)) {
					return true;
				}
			}
			return false;
        } catch (Exception e) {
            return false;
        }
    }
}
