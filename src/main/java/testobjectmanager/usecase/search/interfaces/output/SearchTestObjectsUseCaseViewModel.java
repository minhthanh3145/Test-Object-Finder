package testobjectmanager.usecase.search.interfaces.output;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchTestObjectsUseCaseViewModel {
    private boolean success;
    private List<String> testObjectNames;
    private String message;
}
