package testobjectmanager.usecase.search.impl;

import java.util.List;

import testobjectmanager.exceptions.ConstraintViolatedException;
import testobjectmanager.exceptions.RepositoryException;
import testobjectmanager.usecase.base.UseCaseInputPort;
import testobjectmanager.usecase.base.UseCaseOutputPort;
import testobjectmanager.usecase.search.interfaces.input.SearchTestObjectsUseCaseRequest;
import testobjectmanager.usecase.search.interfaces.output.SearchTestObjectsUseCaseResponse;
import testobjectmanager.usecase.search.interfaces.output.SearchTestObjectsUseCaseViewModel;
import testobjectmanager.usecase.search.interfaces.repository.ISearchTestObjectsUseCaseRepository;
import testobjectmanager.usecase.search.interfaces.validator.ISearchTestObjectsUseCaseValidator;

public class SearchTestObjectsUseCase
        implements UseCaseInputPort<SearchTestObjectsUseCaseRequest, SearchTestObjectsUseCaseResponse> {
    private ISearchTestObjectsUseCaseValidator<SearchTestObjectsUseCaseRequest> validator;

    private ISearchTestObjectsUseCaseRepository repository;

    private UseCaseOutputPort<SearchTestObjectsUseCaseResponse, SearchTestObjectsUseCaseViewModel> presenter;

    private String pathToFolderContainingIndices;

    private List<String> testObjectNames;

    public SearchTestObjectsUseCase(ISearchTestObjectsUseCaseValidator<SearchTestObjectsUseCaseRequest> validator,
            ISearchTestObjectsUseCaseRepository repository,
            UseCaseOutputPort<SearchTestObjectsUseCaseResponse, SearchTestObjectsUseCaseViewModel> presenter) {
        this.validator = validator;
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void presentResponse(SearchTestObjectsUseCaseResponse response) {
        presenter.present(response);
    }

    @Override
    public SearchTestObjectsUseCaseResponse constructResponseOnFailure(Exception e) {
        return SearchTestObjectsUseCaseResponse.builder()
                .success(false)
                .message(e.getMessage())
                .testObjectNames(null)
                .build();
    }

    @Override
    public SearchTestObjectsUseCaseResponse constructResponseOnSuccess() {
        return SearchTestObjectsUseCaseResponse.builder().success(true).testObjectNames(testObjectNames).build();
    }

    @Override
    public void callRepositoryToOperateOnData(SearchTestObjectsUseCaseRequest request) throws RepositoryException {
        testObjectNames = repository.searchTestObjects(request.getQueryString(),
                pathToFolderContainingIndices);

    }

    @Override
    public void validateContrainstsOfRequest(SearchTestObjectsUseCaseRequest request)
            throws ConstraintViolatedException {
        validator.validateQuery(request);
        pathToFolderContainingIndices = request.getPathToFolderContainingIndices();
        validator.validatePathToFolderContainingIndicesExist(pathToFolderContainingIndices);
    }

    public UseCaseOutputPort<SearchTestObjectsUseCaseResponse, SearchTestObjectsUseCaseViewModel> getPresenter() {
        return presenter;
    }

}
