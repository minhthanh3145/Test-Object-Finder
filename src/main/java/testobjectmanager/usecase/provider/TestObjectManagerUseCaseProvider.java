package testobjectmanager.usecase.provider;

import lombok.Getter;
import lombok.Setter;
import testobjectmanager.usecase.index.impl.IndexTestObjectsUseCase;
import testobjectmanager.usecase.search.impl.SearchTestObjectsUseCase;

@Setter
@Getter
public class TestObjectManagerUseCaseProvider {
    private static TestObjectManagerUseCaseProvider _instance;

    private SearchTestObjectsUseCase searchTestObjectsUseCase;

    private IndexTestObjectsUseCase indexTestObjectsUseCase;

    public static TestObjectManagerUseCaseProvider getIntance() {
        if (_instance == null) {
            _instance = new TestObjectManagerUseCaseProvider();
        }
        return _instance;
    }

    private TestObjectManagerUseCaseProvider() {

    }
}
