package testobjectmanager.extension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.katalon.platform.api.extension.ToolItemWithMenuDescription;
import com.katalon.platform.api.service.ApplicationManager;

import testobjectmanager.config.GlobalConfiguration;
import testobjectmanager.main.App;
import testobjectmanager.ui.HistoryDialog;
import testobjectmanager.ui.TestObjectFinderDialog;
import testobjectmanager.usecase.index.impl.IndexTestObjectsUseCase;
import testobjectmanager.usecase.index.interfaces.input.IndexTestObjectsUseCaseRequest;
import testobjectmanager.usecase.provider.TestObjectManagerUseCaseProvider;

public class TestObjectManagerToolItem implements ToolItemWithMenuDescription {
    private Menu menu;

    @Override
    public String toolItemId() {
        return "testobjectmanager.testobjectmanagertoolitem";
    }

    @Override
    public String name() {
        return "Test Object Manager";
    }

    @Override
    public String iconUrl() {
        return "platform:/plugin/com.katalon.testobjectmanager/icons/icon32x32.png";
    }

    @Override
    public Menu getMenu(Control parent) {
        if (menu == null) {
            menu = new Menu(parent);
        }

        for (MenuItem item : menu.getItems()) {
            item.dispose();
        }

        MenuItem searchTestObjectsMenuItem = new MenuItem(menu, SWT.PUSH);
        searchTestObjectsMenuItem.setText("Search Test Objects");
        searchTestObjectsMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TestObjectFinderDialog dialog = new TestObjectFinderDialog(Display.getCurrent().getActiveShell());
                if (dialog.open() == Window.OK) {

                }
            }
        });

//        MenuItem tmp = new MenuItem(menu, SWT.PUSH);
//        tmp.setText("Naive Search");
//        tmp.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//                String currentProject = ApplicationManager.getInstance()
//                        .getProjectManager()
//                        .getCurrentProject()
//                        .getFolderLocation();
//                try {
//                    App.searchForTagWithValueInFolder("tag", "input", currentProject);
//                } catch (IOException e1) {
//                    System.out.println(ExceptionUtils.getStackTrace(e1));
//                }
//            }
//        });

        MenuItem historyMenuItem = new MenuItem(menu, SWT.PUSH);
        historyMenuItem.setText("History");
        historyMenuItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HistoryDialog dialog = new HistoryDialog(Display.getCurrent().getActiveShell());
                if (dialog.open() == Window.OK) {

                }
            }
        });

        return menu;
    }

    private void runWithProgressDialog(IRunnableWithProgress runnable) {
        ProgressMonitorDialog pmd = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
        try {
            pmd.run(true, true, runnable);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

}
