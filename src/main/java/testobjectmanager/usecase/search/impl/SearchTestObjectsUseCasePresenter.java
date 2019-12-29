package testobjectmanager.usecase.search.impl;

import testobjectmanager.usecase.base.UseCaseOutputPort;
import testobjectmanager.usecase.search.interfaces.output.SearchTestObjectsUseCaseResponse;
import testobjectmanager.usecase.search.interfaces.output.SearchTestObjectsUseCaseViewModel;

public class SearchTestObjectsUseCasePresenter
        implements UseCaseOutputPort<SearchTestObjectsUseCaseResponse, SearchTestObjectsUseCaseViewModel> {

    private SearchTestObjectsUseCaseViewModel viewModel;

    @Override
    public SearchTestObjectsUseCaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void present(SearchTestObjectsUseCaseResponse response) {
        if (response.isSuccess()) {
            viewModel = SearchTestObjectsUseCaseViewModel.builder()
                    .success(true)
                    .testObjectNames(response.getTestObjectNames())
                    .build();
        } else {
            viewModel = SearchTestObjectsUseCaseViewModel.builder()
                    .success(false)
                    .message(response.getMessage())
                    .build();
        }
    }

}
