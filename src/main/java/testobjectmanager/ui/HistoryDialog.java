package testobjectmanager.ui;

import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.javatuples.Quartet;

import testobjectmanager.aspect.collector.PerformanceCollector;

public class HistoryDialog extends Dialog {
    protected Composite tablePropertyComposite;

    private TableViewer tbViewer;

    private TableColumnLayout tableColumnLayout;

    List<Quartet<String, String, Long, Date>> history;

    private Table table;

    public HistoryDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public void create() {
        setShellStyle(SWT.SHELL_TRIM);
        super.create();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        tablePropertyComposite = new Composite(parent, SWT.NONE);
        GridData ldTableComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
        ldTableComposite.widthHint = 600;
        ldTableComposite.heightHint = 400;
        tablePropertyComposite.setLayoutData(ldTableComposite);
        tableColumnLayout = new TableColumnLayout();
        tablePropertyComposite.setLayout(tableColumnLayout);

        tbViewer = new TableViewer(tablePropertyComposite, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);

        createColumns();

        tbViewer.setContentProvider(ArrayContentProvider.getInstance());
        loadHistory();

        table = tbViewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        tbViewer.setInput(history);
        return tablePropertyComposite;
    }

    private void loadHistory() {
        history = PerformanceCollector.getInstance().getHistory();
    }

    private void createColumns() {

        TableViewerColumn colObjectId = new TableViewerColumn(tbViewer, SWT.NONE);
        colObjectId.getColumn().setText("Action");
        colObjectId.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                @SuppressWarnings("unchecked")
                Quartet<String, String, Long, Date> quartet = (Quartet<String, String, Long, Date>) element;
                String testObjectId = String.valueOf(quartet.getValue0());
                return testObjectId;
            }
        });

        TableViewerColumn colOldXPath = new TableViewerColumn(tbViewer, SWT.NONE);
        colOldXPath.getColumn().setText("Value");
        colOldXPath.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                @SuppressWarnings("unchecked")
                Quartet<String, String, Long, Date> quartet = (Quartet<String, String, Long, Date>) element;
                String oldXPath = String.valueOf(quartet.getValue1());
                return oldXPath;
            }
        });

        TableViewerColumn colNewXPath = new TableViewerColumn(tbViewer, SWT.NONE);
        colNewXPath.getColumn().setText("Duration (in ms)");
        colNewXPath.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                @SuppressWarnings("unchecked")
                Quartet<String, String, Long, Date> quartet = (Quartet<String, String, Long, Date>) element;
                String newXPath = String.valueOf(quartet.getValue2());
                return newXPath;
            }
        });

        TableViewerColumn colScreenshot = new TableViewerColumn(tbViewer, SWT.NONE);
        colScreenshot.getColumn().setText("Date (ended)");
        colScreenshot.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                @SuppressWarnings("unchecked")
                Quartet<String, String, Long, Date> quartet = (Quartet<String, String, Long, Date>) element;
                String newXPath = String.valueOf(quartet.getValue3());
                return newXPath;
            }
        });

        tableColumnLayout.setColumnData(colObjectId.getColumn(), new ColumnWeightData(25, 100));
        tableColumnLayout.setColumnData(colOldXPath.getColumn(), new ColumnWeightData(25, 100));
        tableColumnLayout.setColumnData(colNewXPath.getColumn(), new ColumnWeightData(25, 100));
        tableColumnLayout.setColumnData(colScreenshot.getColumn(), new ColumnWeightData(25, 100));
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("History");
    }

}
