package testobjectmanager.usecase.index.interfaces.output;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IndexTestObjectsUseCaseResponse {
    boolean success;

    String message;
}
