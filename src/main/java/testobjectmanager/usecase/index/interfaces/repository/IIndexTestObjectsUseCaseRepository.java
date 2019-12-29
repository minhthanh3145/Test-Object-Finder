package testobjectmanager.usecase.index.interfaces.repository;

import testobjectmanager.exceptions.RepositoryException;

public interface IIndexTestObjectsUseCaseRepository {
    void indexTestObjectsToFolder(String pathToFolder) throws RepositoryException;
}
