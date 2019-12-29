package testobjectmanager.usecase.index.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.w3c.dom.NodeList;

import com.katalon.platform.api.controller.FolderController;
import com.katalon.platform.api.model.FolderEntity;
import com.katalon.platform.api.model.ProjectEntity;
import com.katalon.platform.api.service.ApplicationManager;

import testobjectmanager.aspect.collector.PerformanceCollector;
import testobjectmanager.exceptions.RepositoryException;
import testobjectmanager.usecase.index.interfaces.repository.IIndexTestObjectsUseCaseRepository;

public class IndexTestObjectsUseCaseRepository implements IIndexTestObjectsUseCaseRepository {

    private static List<String> singleProperties = Arrays.asList("name", "tag", "elementGuidId", "selectorMethod",
            "useRalativeImagePath", "followRedirects", "httpBody", "httpBodyContent", "httpBodyType",
            "restRequestMethod", "restUrl", "serviceType", "soapBody", "soapHeader", "soapRequestMethod",
            "soapServiceFunction", "wsdlAddress");

    private FolderController folderController;

    private ProjectEntity currentProject;

    private DocumentBuilderFactory dbFactory;

    private DocumentBuilder dBuilder;

    public IndexTestObjectsUseCaseRepository() {
        folderController = ApplicationManager.getInstance()
                .getControllerManager()
                .getController(FolderController.class);
        currentProject = ApplicationManager.getInstance().getProjectManager().getCurrentProject();
        dbFactory = DocumentBuilderFactory.newInstance();
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public void indexTestObjectsToFolder(String pathToFolder) throws RepositoryException {
        IndexWriter iwriter = null;
        long startTime = System.nanoTime();
        try {
            Analyzer analyzer = new StandardAnalyzer();
            IOUtils.rm(Paths.get(pathToFolder));
            Path indexPath = Files.createDirectories(Paths.get(pathToFolder));
            Directory directory = FSDirectory.open(indexPath);
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            iwriter = new IndexWriter(directory, config);
            // Retrieving the root object repository and all its child folders
            FolderEntity objectRepoFolder = folderController.getFolder(currentProject, "Object Repository");
            List<FolderEntity> childFolders = folderController.getChildFolders(currentProject, objectRepoFolder);
            childFolders.add(objectRepoFolder);
            // Walk down all the folders, retrieve test object files and index them
            List<Document> luceneDocs = Files.find(Paths.get(objectRepoFolder.getFolderLocation()), 999, (p, bfa) -> {
                return bfa.isRegularFile();
            }).map(to -> {
                try {
                    String locationToFile = to.toString();
                    Document luceneDoc = new Document();
                    FieldType fieldType = new FieldType(TextField.TYPE_STORED);
                    fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                    luceneDoc.add(new Field("file_location", to.toString(), fieldType));
                    singleProperties.forEach(property -> {
                        index(locationToFile, property, luceneDoc);
                    });
                    indexCompoundProperties(locationToFile, "webElementProperties", luceneDoc);
                    indexCompoundProperties(locationToFile, "webElementXpaths", luceneDoc);
                    indexSelectorCollection(locationToFile, luceneDoc);
                    return luceneDoc;
                } catch (Exception e) {
                    System.out.println(ExceptionUtils.getStackTrace(e));
                }
                return null;
            }).filter(doc -> doc != null).collect(Collectors.toList());
            for (Document luceneDoc : luceneDocs) {
                iwriter.addDocument(luceneDoc);
            }
        } catch (Exception e2) {
            System.out.println(ExceptionUtils.getStackTrace(e2));
            throw new RepositoryException(e2.getMessage());
        } finally {
            try {
                iwriter.close();
            } catch (IOException e) {
                System.out.println(ExceptionUtils.getStackTrace(e));
            }
            // Add the index performance to history
            PerformanceCollector.getInstance().addToHistory("index", "",
                    new Long((System.nanoTime() - startTime) / 1000000), new Date());
        }
    }

    private void index(String fileLocation, String propertyName, Document luceneDoc) {
        try {
            File fXmlFile = new File(fileLocation);
            org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(propertyName);
            for (int i = 0; i < nList.getLength(); i++) {
                org.w3c.dom.Node node = nList.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) node;
                    String value = eElement.getTextContent();
                    FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
                    fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                    luceneDoc.add(new Field(propertyName.toLowerCase(), value, fieldType));
                }
            }
        } catch (Throwable e) {
            // Do nothing here
        }
    }

    private void indexCompoundProperties(String fileLocation, String compoundPropertyName, Document luceneDoc) {
        try {
            File fXmlFile = new File(fileLocation);
            org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(compoundPropertyName);
            for (int i = 0; i < nList.getLength(); i++) {
                org.w3c.dom.Node node = nList.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) node;
                    String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                    String value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
                    fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                    luceneDoc.add(new Field(name.toLowerCase(), value, fieldType));
                }
            }
        } catch (Throwable e) {
            // Do nothing here
        }
    }

    private void indexSelectorCollection(String fileLocation, Document luceneDoc) {
        try {
            File fXmlFile = new File(fileLocation);
            org.w3c.dom.Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("selectorCollection");
            for (int i = 0; i < nList.getLength(); i++) {
                org.w3c.dom.Node node = nList.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    org.w3c.dom.Element eElement = (org.w3c.dom.Element) node;
                    String name = eElement.getElementsByTagName("key").item(0).getTextContent();
                    String value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
                    fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                    luceneDoc.add(new Field(name.toLowerCase(), value, fieldType));
                }
            }
        } catch (Throwable e) {
            // Do nothing here
        }
    }
}
