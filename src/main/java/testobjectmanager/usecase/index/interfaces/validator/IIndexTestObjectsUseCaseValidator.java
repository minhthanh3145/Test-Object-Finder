package testobjectmanager.usecase.index.interfaces.validator;

import testobjectmanager.exceptions.ConstraintViolatedException;

public interface IIndexTestObjectsUseCaseValidator {
    void validatePathToFolder(String pathToFolder) throws ConstraintViolatedException;
}
