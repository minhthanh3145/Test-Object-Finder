package testobjectmanager.usecase.index.impl;

import java.util.Optional;

import testobjectmanager.exceptions.ConstraintViolatedException;
import testobjectmanager.usecase.index.interfaces.validator.IIndexTestObjectsUseCaseValidator;

public class IndexTestObjectsUseCaseValidator implements IIndexTestObjectsUseCaseValidator {

    @Override
    public void validatePathToFolder(String pathToFolder) throws ConstraintViolatedException {
        Optional.ofNullable(pathToFolder).filter(e -> !e.trim().equals("") && e.length() != 0).orElseThrow(
                () -> new ConstraintViolatedException());
    }

}
