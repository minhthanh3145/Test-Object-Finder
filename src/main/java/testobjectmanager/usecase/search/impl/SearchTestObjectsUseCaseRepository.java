package testobjectmanager.usecase.search.impl;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import testobjectmanager.aspect.collector.PerformanceCollector;
import testobjectmanager.exceptions.RepositoryException;
import testobjectmanager.usecase.search.interfaces.repository.ISearchTestObjectsUseCaseRepository;

public class SearchTestObjectsUseCaseRepository implements ISearchTestObjectsUseCaseRepository {

    @Override
    public List<String> searchTestObjects(String queryString, String pathToFolderContainingIndices)
            throws RepositoryException {
        long startTime = System.nanoTime();
        List<String> returnedTestObjectFiles = new ArrayList<String>();
        Analyzer analyzer = new StandardAnalyzer();
        DirectoryReader ireader = null;
        Directory directory;
        String[] terms = queryString.split("and");
        try {
            directory = FSDirectory.open(Paths.get(pathToFolderContainingIndices));
            ireader = DirectoryReader.open(directory);
            IndexSearcher isearcher = new IndexSearcher(ireader);
            QueryBuilder parser = new QueryBuilder(analyzer);
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            for (String term : terms) {
                String[] arguments = term.split("=");
                Query query = parser.createPhraseQuery(arguments[0].trim(), arguments[1].trim());
                builder.add(query, Occur.MUST);
            }

            ScoreDoc[] hits = isearcher.search(builder.build(), 999).scoreDocs;
            for (ScoreDoc hit : hits) {
                returnedTestObjectFiles.add(isearcher.doc(hit.doc).get("file_location"));
            }

        } catch (IOException e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        } finally {
            try {
                ireader.close();
            } catch (IOException e) {
                System.out.println(ExceptionUtils.getStackTrace(e));
            }
            // Add the search performance to history
            PerformanceCollector.getInstance().addToHistory("search", queryString,
                    new Long((System.nanoTime() - startTime) / 1000000), new Date());
        }
        return returnedTestObjectFiles;
    }

}
