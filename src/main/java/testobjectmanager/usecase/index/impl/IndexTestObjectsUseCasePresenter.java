package testobjectmanager.usecase.index.impl;

import testobjectmanager.usecase.base.UseCaseOutputPort;
import testobjectmanager.usecase.index.interfaces.output.IndexTestObjectsUseCaseResponse;
import testobjectmanager.usecase.index.interfaces.output.IndexTestObjectsUseCaseViewModel;

public class IndexTestObjectsUseCasePresenter
        implements UseCaseOutputPort<IndexTestObjectsUseCaseResponse, IndexTestObjectsUseCaseViewModel> {

    IndexTestObjectsUseCaseViewModel viewModel;

    @Override
    public IndexTestObjectsUseCaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void present(IndexTestObjectsUseCaseResponse response) {
        if (response.isSuccess()) {
            viewModel = IndexTestObjectsUseCaseViewModel.builder().success(true).build();
        } else {
            viewModel = IndexTestObjectsUseCaseViewModel.builder()
                    .success(false)
                    .message(response.getMessage())
                    .build();
        }
    }

}
