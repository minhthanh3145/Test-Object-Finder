package testobjectmanager.extension;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.katalon.platform.api.extension.ToolItemWithMenuDescription;

import testobjectmanager.ui.HistoryDialog;
import testobjectmanager.ui.TestObjectFinderDialog;

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

}
