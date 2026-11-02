package org.etlforge.integration.client.ui.jitterpack;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.FolderSelectorPanelBuilder;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImportParameters;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.widget.DefaultComponentAction;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.preference.PreferencesKey;

public final class ImportJitterPackUi implements UiProvider {
   public static final String HELP_ID = "Import_Jitterpak";
   private final ProjectManager projectManager;
   private final ApplicationWindow window;
   private final boolean fixedWidth;
   private JComponent ui;
   private Action doImportAction;
   private final Action examineFileAction;
   private SelectedFileExaminer selectedFileExaminer;
   private final JitterPackBrowser jpBrowser;
   private final ImportParametersDefiner parametersDefiner;
   private final FileSelectorPanel destinationPanel;
   private final JitterPackSearcher jpSearcher;
   private final ImportJitterPackUi.SelectionNotifier selectionNotifier;
   private UndoRedoController undoRedoController;
   private final JitterPackSelectionListeners selectionListeners;

   public ImportJitterPackUi(ProjectManager var1, ApplicationWindow var2) {
      this(var1, var2, true);
   }

   public ImportJitterPackUi(ProjectManager var1, ApplicationWindow var2, boolean var3) {
      this.projectManager = var1;
      this.window = var2;
      this.fixedWidth = var3;
      this.selectionListeners = new JitterPackSelectionListeners();
      this.selectionNotifier = new ImportJitterPackUi.SelectionNotifier();
      this.examineFileAction = new ImportJitterPackUi.ExamineFileAction();
      this.jpBrowser = this.createBrowser();
      File var4 = var1.getProjectDirectory().getLocation();
      this.destinationPanel = this.createDestinationPanel(var4);
      this.parametersDefiner = new ImportParametersDefiner();
      this.jpSearcher = this.createSearcher();
   }

   private JitterPackBrowser createBrowser() {
      JitterPackBrowser var1 = new JitterPackBrowser(this.window);
      var1.addJitterPackSelectionListener(this.selectionNotifier);
      return var1;
   }

   private FileSelectorPanel createDestinationPanel(File var1) {
      FolderSelectorPanelBuilder var2 = new FolderSelectorPanelBuilder();
      var2.setPreferenceKey(new PreferencesKey(ImportJitterPackUi.class, "Destination"));
      var2.setTitle(Strings.getJitterPackString("Import.Destination"));
      var2.setDefaultFolder(var1);
      FileSelectorPanel var3 = var2.buildPanel(this.window);
      var3.setBrowseButtonText(Strings.get("Import.Destination.Browse"));
      LabelDecorator var4 = TextStyles.DefaultText.asLabelDecorator(CommonIcons.FOLDER_CLOSED_16);
      var3.decorateTitle(var4);
      return var3;
   }

   private JitterPackSearcher createSearcher() {
      JitterPackSearcher var1 = new JitterPackSearcher(this.window, this.parametersDefiner);
      var1.addJitterPackSelectionListener(new JitterPackSelectionListener() {
         @Override
         public void selectionChanged(JitterPack var1) {
            ImportJitterPackUi.this.jpBrowser.setSelectedJitterPack(var1);
         }
      });
      return var1;
   }

   public void reset() {
      this.parametersDefiner.reset();
   }

   public ProjectManager getProjectManager() {
      return this.projectManager;
   }

   public JitterPack getJitterPackToImport() {
      return this.jpBrowser.isEnabled() ? this.jpBrowser.getJitterPack() : this.jpSearcher.getJitterPack();
   }

   public ImportParameters getImportParameters() {
      return this.parametersDefiner.getParameters();
   }

   public void dispose() {
      this.disposeUndoRedo();
   }

   private void disposeUndoRedo() {
      if (this.undoRedoController != null) {
         this.undoRedoController.dispose();
      }
   }

   public File getDestinationFolder() {
      return this.destinationPanel.getSelectedFile();
   }

   public boolean isInputValid() {
      return this.getJitterPackToImport() != null && this.getDestinationFolder() != null;
   }

   public void setDoImportAction(Action var1) {
      if (this.doImportAction != null) {
         this.jpBrowser.removeActionListener(this.doImportAction);
         this.jpSearcher.removeActionListener(this.doImportAction);
      }

      if (var1 != null) {
         this.jpBrowser.addActionListener(var1);
         this.jpSearcher.addActionListener(var1);
      }

      this.doImportAction = var1;
      this.setDefaultUiAction(var1);
   }

   private void setDefaultUiAction(Action var1) {
      if (this.ui != null) {
         DefaultComponentAction.whenAncestorOfFocusOwner(this.ui, var1);
      }
   }

   public void setFileExaminer(SelectedFileExaminer var1) {
      this.selectedFileExaminer = var1;
      this.updateExamineFileActionEnabledState();
   }

   private void updateExamineFileActionEnabledState() {
      boolean var1 = this.selectedFileExaminer != null && this.jpBrowser.getJitterPack() != null;
      this.examineFileAction.setEnabled(var1);
   }

   public void addJitterPackSelectionListener(JitterPackSelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   public void removeJitterPackSelectionListener(JitterPackSelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   public void addDestinationFolderListener(FileSelectionListener var1) {
      this.destinationPanel.addFileSelectionListener(var1);
   }

   public void removeDestinationFolderListener(FileSelectionListener var1) {
      this.destinationPanel.removeFileSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.ui == null) {
         this.ui = this.createUi();
         this.setDefaultUiAction(this.doImportAction);
         this.installUndoRedo(this.ui);
      }

      var1.display(this.ui);
   }

   private JComponent createUi() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setFill(OneColumnPanel.HORIZONTAL);
      var1.setWeightX(1.0);
      var1.withInsets(0, 0, 10, 0);
      var1.add(this.createBrowserCapsule());
      var1.add(this.destinationPanel);
      var1.add(this.parametersDefiner);
      if (this.doImportAction != null) {
         var1.noFill(this.createImportButtonCapsule());
      }

      JComponent var2 = this.fixedWidth ? InvisiblePanel.of(var1) : new BorderLayoutBuilder().north(var1).container();
      UiUtils.setOpaqueRecursivelySkipDefaults(var2, false);
      var2.setBorder(Empty.border(5));
      return var2;
   }

   private OneColumnPanel createBrowserCapsule() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      var1.add(this.jpBrowser);
      var1.setFill(OneColumnPanel.NONE);
      var1.setWeightX(0.0);
      JButton var2 = ButtonUtils.createLabelButton(this.examineFileAction, StyleSheet.LinkButton.asTextDecorator());
      ButtonUtils.enableRolloverForButton(var2);
      var1.add(var2);
      return var1;
   }

   private JComponent createImportButtonCapsule() {
      KongaButton var1 = new KongaButton(this.doImportAction);
      JPanel var2 = InvisiblePanel.of(var1);
      var2.setOpaque(false);
      return var2;
   }

   private void fireSelectionEvent() {
      this.selectionListeners.call().selectionChanged(this.getJitterPackToImport());
   }

   private void installUndoRedo(JComponent var1) {
      this.disposeUndoRedo();
      this.undoRedoController = UndoRedoControllers.textFactory().newController(var1);
   }

   @Override
   public void requestFocus() {
      this.jpBrowser.requestFocus();
   }

   private final class ExamineFileAction extends AbstractAction {
      public ExamineFileAction() {
         super(Strings.get("Examiner.Name"), CommonIcons.VIEW_DETAILS_16);
         this.putValue("ShortDescription", Strings.getJitterPackString("Examiner.ToolTip"));
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (ImportJitterPackUi.this.selectedFileExaminer != null) {
            JitterPack var2 = ImportJitterPackUi.this.jpBrowser.getJitterPack();
            if (var2 != null) {
               ImportJitterPackUi.this.selectedFileExaminer.examine(var2.getFile(), ImportJitterPackUi.this.getImportParameters());
            }
         }
      }
   }

   private final class SelectionNotifier implements JitterPackSelectionListener {
      private SelectionNotifier() {
      }

      @Override
      public void selectionChanged(JitterPack var1) {
         ImportJitterPackUi.this.fireSelectionEvent();
         ImportJitterPackUi.this.updateExamineFileActionEnabledState();
      }
   }
}

