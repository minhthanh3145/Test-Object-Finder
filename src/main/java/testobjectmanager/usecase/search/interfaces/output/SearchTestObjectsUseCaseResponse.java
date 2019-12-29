package testobjectmanager.usecase.search.interfaces.output;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SearchTestObjectsUseCaseResponse {
    private boolean success;

    private List<String> testObjectNames;

    private String message;
}
