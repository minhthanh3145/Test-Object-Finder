package testobjectmanager.usecase.search.impl;

import java.io.File;
import java.util.Optional;

import testobjectmanager.exceptions.ConstraintViolatedException;
import testobjectmanager.usecase.search.interfaces.input.SearchTestObjectsUseCaseRequest;
import testobjectmanager.usecase.search.interfaces.validator.ISearchTestObjectsUseCaseValidator;

public class SearchTestObjectsUseCaseValidator
        implements ISearchTestObjectsUseCaseValidator<SearchTestObjectsUseCaseRequest> {

    @Override
    public void validateQuery(SearchTestObjectsUseCaseRequest request) throws ConstraintViolatedException {
        Optional.ofNullable(request.getQueryString())
                .filter(query -> query.trim().length() != 0)
                .orElseThrow(() -> new ConstraintViolatedException());
    }

    @Override
    public void validatePathToFolderContainingIndicesExist(String path) throws ConstraintViolatedException {
        Optional.ofNullable(path).map(oath -> {
            return (new File(path).exists()) ? true : null;
        }).orElseThrow(() -> new ConstraintViolatedException());
    }

}
