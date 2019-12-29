package testobjectmanager.usecase.search.interfaces.repository;

import java.util.List;

import testobjectmanager.exceptions.RepositoryException;

public interface ISearchTestObjectsUseCaseRepository {
    List<String> searchTestObjects(String query, String pathToFolderContainingIndices)
            throws RepositoryException;
}
