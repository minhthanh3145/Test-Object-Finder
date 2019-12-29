package testobjectmanager.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlobalConfiguration {
    private String pathToFolderContainingIndex = "";

    private static GlobalConfiguration _instance;

    public static GlobalConfiguration getInstance() {
        if (_instance == null) {
            _instance = new GlobalConfiguration();
        }
        return _instance;
    }

    GlobalConfiguration() {

    }
}
