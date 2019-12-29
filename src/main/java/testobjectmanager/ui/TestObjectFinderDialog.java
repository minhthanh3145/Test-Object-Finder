package testobjectmanager.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.katalon.platform.api.model.ProjectEntity;
import com.katalon.platform.api.service.ApplicationManager;
import com.katalon.platform.api.ui.UISynchronizeService;

import testobjectmanager.config.GlobalConfiguration;
import testobjectmanager.constant.GlobalConstants;
import testobjectmanager.usecase.index.impl.IndexTestObjectsUseCase;
import testobjectmanager.usecase.index.interfaces.input.IndexTestObjectsUseCaseRequest;
import testobjectmanager.usecase.provider.TestObjectManagerUseCaseProvider;
import testobjectmanager.usecase.search.impl.SearchTestObjectsUseCase;
import testobjectmanager.usecase.search.interfaces.input.SearchTestObjectsUseCaseRequest;
import testobjectmanager.usecase.search.interfaces.output.SearchTestObjectsUseCaseViewModel;

public class TestObjectFinderDialog extends Dialog {
    private Composite mainComposite;

    private Composite treeViewerComposite;

    private TreeViewer tvTestObjects;

    private Composite treeViewerAndDetailComposite;

    private Text txtQueryString;

    private IndexTestObjectsUseCase indexUseCase;

    private SearchTestObjectsUseCase searchUseCase;

    private UISynchronizeService uiSynchronizeService;

    public TestObjectFinderDialog(Shell parentShell) {
        super(parentShell);
        indexUseCase = TestObjectManagerUseCaseProvider.getIntance().getIndexTestObjectsUseCase();
        searchUseCase = TestObjectManagerUseCaseProvider.getIntance().getSearchTestObjectsUseCase();
        uiSynchronizeService = ApplicationManager.getInstance()
                .getUIServiceManager()
                .getService(UISynchronizeService.class);
    }

    @Override
    public void create() {
        setShellStyle(SWT.SHELL_TRIM);
        super.create();
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText("Test Object Finder");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(new FillLayout(SWT.VERTICAL));
        GridData ldTableComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
        ldTableComposite.widthHint = 1000;
        ldTableComposite.heightHint = 400;
        mainComposite.setLayoutData(ldTableComposite);

        SashForm hSashForm = new SashForm(mainComposite, SWT.NONE);
        hSashForm.setOrientation(SWT.HORIZONTAL);

        Composite leftPanelComposite = new Composite(hSashForm, SWT.NONE);
        GridLayout glHtmlDomComposite = new GridLayout();
        glHtmlDomComposite.marginBottom = 5;
        glHtmlDomComposite.marginRight = 0;
        glHtmlDomComposite.marginWidth = 0;
        glHtmlDomComposite.marginHeight = 0;
        glHtmlDomComposite.horizontalSpacing = 0;
        leftPanelComposite.setLayout(glHtmlDomComposite);

        treeViewerAndDetailComposite = new Composite(leftPanelComposite, SWT.NONE);
        GridData gdTreeViewerAndDetail = new GridData(SWT.FILL, SWT.FILL, true, true);
        treeViewerAndDetailComposite.setLayoutData(gdTreeViewerAndDetail);
        GridLayout glTreeViwerAndDetail = new GridLayout(1, false);
        treeViewerAndDetailComposite.setLayout(glTreeViwerAndDetail);

        txtQueryString = new Text(treeViewerAndDetailComposite, SWT.BORDER);
        txtQueryString.setMessage("Enter your query here");
        txtQueryString.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        createTestObjectTreeViewerComposite();

        Composite rightPanelComposite = new Composite(hSashForm, SWT.NONE);
        GridLayout glObjectComposite = new GridLayout();
        glObjectComposite.marginLeft = 5;
        glObjectComposite.marginWidth = 0;
        glObjectComposite.marginBottom = 0;
        glObjectComposite.marginHeight = 0;
        glObjectComposite.verticalSpacing = 0;
        glObjectComposite.horizontalSpacing = 0;
        rightPanelComposite.setLayout(glObjectComposite);

        Text txtInfo = new Text(rightPanelComposite, SWT.BORDER);
        txtInfo.setText(GlobalConstants.INFO_TEXT);
        txtInfo.setEditable(false);
        txtInfo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        hSashForm.setWeights(new int[] { 10, 5 });
        registerListeners();
        return mainComposite;
    }

    public void createTestObjectTreeViewerComposite() {
        treeViewerComposite = new Composite(treeViewerAndDetailComposite, SWT.NONE);
        GridData gdTreeViewer = new GridData(SWT.FILL, SWT.FILL, false, true);
        treeViewerComposite.setLayoutData(gdTreeViewer);
        treeViewerComposite.setLayout(new GridLayout(1, false));

        tvTestObjects = new TreeViewer(treeViewerComposite, SWT.MULTI | SWT.BORDER);
        tvTestObjects.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
        tvTestObjects.setContentProvider(new ITreeContentProvider() {
            @Override
            public Object[] getChildren(Object arg0) {
                return new Object[0];
            }

            @Override
            public Object[] getElements(Object arg0) {
                if (arg0 == null) {
                    return new Object[0];
                }
                if (arg0.getClass().isArray()) {
                    return (Object[]) arg0;
                }
                if (arg0 instanceof List<?>) {
                    return ((List<?>) arg0).toArray();
                }
                return new Object[0];
            }

            @Override
            public Object getParent(Object arg0) {
                return null;
            }

            @Override
            public boolean hasChildren(Object arg0) {
                return false;
            }
        });

        tvTestObjects.setLabelProvider(new CellLabelProvider() {

            @Override
            public void update(ViewerCell cell) {
                String name = StringUtils.EMPTY;
                if (cell.getElement() != null && cell.getElement() instanceof String) {
                    name = ((String) cell.getElement());
                }
                cell.setText(name);
            }
        });
    }

    private void registerListeners() {
        ProjectEntity currentProject = ApplicationManager.getInstance().getProjectManager().getCurrentProject();
        // Initiate search use case on enter
        txtQueryString.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {

                    runWithProgressDialog(new IRunnableWithProgress() {
                        @Override
                        public void run(IProgressMonitor monitor)
                                throws InvocationTargetException, InterruptedException {
                            monitor.beginTask("Indexing...", 10);
                            indexUseCase.execute(IndexTestObjectsUseCaseRequest.builder()
                                    .pathToFolder(GlobalConfiguration.getInstance().getPathToFolderContainingIndex())
                                    .build());
                            monitor.done();
                        }
                    });

                    runWithProgressDialog(new IRunnableWithProgress() {
                        @Override
                        public void run(IProgressMonitor monitor)
                                throws InvocationTargetException, InterruptedException {
                            monitor.beginTask("Searching...", 10);
                            uiSynchronizeService.asyncExec(() -> {
                                searchUseCase
                                        .execute(SearchTestObjectsUseCaseRequest.builder()
                                                .pathToFolderContainingIndices(GlobalConfiguration.getInstance()
                                                        .getPathToFolderContainingIndex())
                                                .queryString(txtQueryString.getText())
                                                .build());
                                SearchTestObjectsUseCaseViewModel viewModel = searchUseCase.getPresenter()
                                        .getViewModel();
                                if (viewModel.isSuccess()) {
                                    List<String> relativePathsToTestObjects = viewModel.getTestObjectNames()
                                            .stream()
                                            .map(testObjectAbsolutePath -> Paths.get(currentProject.getFolderLocation())
                                                    .relativize(Paths.get(testObjectAbsolutePath))
                                                    .toString())
                                            .collect(Collectors.toList());
                                    tvTestObjects.setInput(relativePathsToTestObjects.toArray());
                                    tvTestObjects.refresh();
                                }
                            });
                            monitor.done();
                        }
                    });

                }
            }
        });
    }

    private void runWithProgressDialog(IRunnableWithProgress runnable) {
        ProgressMonitorDialog pmd = new ProgressMonitorDialog(this.getShell());
        try {
            pmd.run(true, true, runnable);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // Change parent layout data to fill the whole bar
        parent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        createButton(parent, IDialogConstants.FINISH_ID, "Copy Test Object name to clipboard", false);

        getButton(IDialogConstants.FINISH_ID).addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                uiSynchronizeService.syncExec(() -> {
                    IStructuredSelection selection = (IStructuredSelection) tvTestObjects.getStructuredSelection();
                    String name = Paths.get(String.valueOf(selection.getFirstElement())).getFileName().toString();
                    String nameWithoutExtension = name.substring(0, name.lastIndexOf('.'));
                    StringSelection stringSelection = new StringSelection(nameWithoutExtension);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

                });
            }
        });
        GridLayout layout = (GridLayout) parent.getLayout();
        layout.numColumns++;
        layout.makeColumnsEqualWidth = false;
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false);
    }
}
