package testobjectmanager.extension;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.katalon.platform.api.Plugin;
import com.katalon.platform.api.extension.PluginActivationListener;
import com.katalon.platform.api.service.ApplicationManager;

import testobjectmanager.config.GlobalConfiguration;
import testobjectmanager.usecase.index.impl.IndexTestObjectsUseCase;
import testobjectmanager.usecase.index.impl.IndexTestObjectsUseCasePresenter;
import testobjectmanager.usecase.index.impl.IndexTestObjectsUseCaseRepository;
import testobjectmanager.usecase.index.impl.IndexTestObjectsUseCaseValidator;
import testobjectmanager.usecase.provider.TestObjectManagerUseCaseProvider;
import testobjectmanager.usecase.search.impl.SearchTestObjectsUseCase;
import testobjectmanager.usecase.search.impl.SearchTestObjectsUseCasePresenter;
import testobjectmanager.usecase.search.impl.SearchTestObjectsUseCaseRepository;
import testobjectmanager.usecase.search.impl.SearchTestObjectsUseCaseValidator;

public class TestObjectManagerActivationListener implements PluginActivationListener {
    private SearchTestObjectsUseCase getSearchTestObjectsUseCase() {
        return new SearchTestObjectsUseCase(new SearchTestObjectsUseCaseValidator(),
                new SearchTestObjectsUseCaseRepository(), new SearchTestObjectsUseCasePresenter());
    }

    private IndexTestObjectsUseCase getIndextTestObjectUseCase() {
        return new IndexTestObjectsUseCase(new IndexTestObjectsUseCaseValidator(),
                new IndexTestObjectsUseCaseRepository(), new IndexTestObjectsUseCasePresenter());
    }

    @Override
    public void afterActivation(Plugin plugin) {
        TestObjectManagerUseCaseProvider.getIntance().setSearchTestObjectsUseCase(getSearchTestObjectsUseCase());
        TestObjectManagerUseCaseProvider.getIntance().setIndexTestObjectsUseCase(getIndextTestObjectUseCase());
        try {
            String indicesFolder = ApplicationManager.getInstance()
                    .getProjectManager()
                    .getCurrentProject()
                    .getFolderLocation() + "/Indices";
            GlobalConfiguration.getInstance().setPathToFolderContainingIndex(indicesFolder);
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Test Object Manager",
                    "Artifacts used to improve search performance) will be stored in the folder Indices in your project folder");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }
}
