package testobjectmanager.usecase.index.interfaces.input;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IndexTestObjectsUseCaseRequest {
    private String pathToFolder;
}
