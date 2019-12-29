package testobjectmanager.usecase.search.interfaces.validator;

import testobjectmanager.exceptions.ConstraintViolatedException;

public interface ISearchTestObjectsUseCaseValidator<T> {
    void validateQuery(T request) throws ConstraintViolatedException;

    void validatePathToFolderContainingIndicesExist(String path) throws ConstraintViolatedException;
}
