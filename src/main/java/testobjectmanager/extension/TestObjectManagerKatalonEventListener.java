package testobjectmanager.extension;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;

import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.extension.EventListenerInitializer;
import com.katalon.platform.api.model.Entity;
import com.katalon.platform.internal.event.EventConstants;

import testobjectmanager.config.GlobalConfiguration;

public class TestObjectManagerKatalonEventListener implements EventListenerInitializer {

    @Override
    public void registerListener(EventListener listener) {
        listener.on(Event.class, event -> {
            try {
                System.out.println(event.getTopic());
                if (event.getTopic().equals("KATALON_PLUGIN/CURRENT_PROJECT_CHANGED")) {
                    Entity projectEntity = (Entity) event.getProperty(EventConstants.EVENT_DATA_PROPERTY_NAME);
                    String indicesFolder = projectEntity.getFolderLocation() + "/Indices";
                    GlobalConfiguration.getInstance().setPathToFolderContainingIndex(indicesFolder);
                    MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Test Object Manager",
                            "Artifacts used to improve search performance) will be stored in the folder Indices in your project folder");
                }
            } catch (Exception e) {
                System.out.println(ExceptionUtils.getStackTrace(e));
            }
        });
    }

}
