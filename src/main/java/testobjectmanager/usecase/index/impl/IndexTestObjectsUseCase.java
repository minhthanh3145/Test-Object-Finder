package testobjectmanager.usecase.index.impl;

import testobjectmanager.exceptions.ConstraintViolatedException;
import testobjectmanager.exceptions.RepositoryException;
import testobjectmanager.usecase.base.UseCaseInputPort;
import testobjectmanager.usecase.base.UseCaseOutputPort;
import testobjectmanager.usecase.index.interfaces.input.IndexTestObjectsUseCaseRequest;
import testobjectmanager.usecase.index.interfaces.output.IndexTestObjectsUseCaseResponse;
import testobjectmanager.usecase.index.interfaces.output.IndexTestObjectsUseCaseViewModel;
import testobjectmanager.usecase.index.interfaces.repository.IIndexTestObjectsUseCaseRepository;
import testobjectmanager.usecase.index.interfaces.validator.IIndexTestObjectsUseCaseValidator;

public class IndexTestObjectsUseCase
        implements UseCaseInputPort<IndexTestObjectsUseCaseRequest, IndexTestObjectsUseCaseResponse> {

    private IIndexTestObjectsUseCaseValidator validator;

    private IIndexTestObjectsUseCaseRepository repository;

    private String pathToFolder;

    private UseCaseOutputPort<IndexTestObjectsUseCaseResponse, IndexTestObjectsUseCaseViewModel> presenter;

    public IndexTestObjectsUseCase(IIndexTestObjectsUseCaseValidator validator,
            IIndexTestObjectsUseCaseRepository repository,
            UseCaseOutputPort<IndexTestObjectsUseCaseResponse, IndexTestObjectsUseCaseViewModel> presenter) {
        this.validator = validator;
        this.repository = repository;
        this.presenter = presenter;
    }

    @Override
    public void presentResponse(IndexTestObjectsUseCaseResponse response) {
        presenter.present(response);
    }

    @Override
    public IndexTestObjectsUseCaseResponse constructResponseOnFailure(Exception e) {
        return IndexTestObjectsUseCaseResponse.builder().success(false).message(e.getMessage()).build();
    }

    @Override
    public IndexTestObjectsUseCaseResponse constructResponseOnSuccess() {
        return IndexTestObjectsUseCaseResponse.builder().success(true).build();
    }

    @Override
    public void callRepositoryToOperateOnData(IndexTestObjectsUseCaseRequest request) throws RepositoryException {
        repository.indexTestObjectsToFolder(pathToFolder);
    }

    @Override
    public void validateContrainstsOfRequest(IndexTestObjectsUseCaseRequest request)
            throws ConstraintViolatedException {
        pathToFolder = request.getPathToFolder();
        validator.validatePathToFolder(pathToFolder);
    }

    public UseCaseOutputPort<IndexTestObjectsUseCaseResponse, IndexTestObjectsUseCaseViewModel> getPresenter() {
        return presenter;
    }

}
