package testobjectmanager.usecase.search.interfaces.input;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchTestObjectsUseCaseRequest {
    String pathToFolderContainingIndices;

    String queryString;
}
