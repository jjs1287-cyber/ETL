package org.etlforge.integration.client.ui.jitterpack;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputFieldVisitor;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.input.InputPanelSite;
import org.etlforge.ui.input.InputPanelSiteImplementor;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PathValidators;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.util.preference.PreferencesKey;

public final class ExportJitterPackUi implements UiProvider, InputPanelSite {
   public static final String HELP_ID = ClientInfo.isDataLoader() ? "jiitterpaks_exporting_an_operation_as_a_jitterpak_htm" : "Export_Jitterpak";
   private final IntegrationProject project;
   private Action doExportAction;
   private final FileSelectorPanel destinationPanel;
   private final TextInputField commentArea;
   private final ExportParametersDefiner parametersDefiner;
   private final InputPanel inputPanel;
   private final InputPanelSite inputSiteImplementor;
   private UndoRedoController undoRedoController;

   public ExportJitterPackUi(IntegrationProject var1) {
      this.project = var1;
      this.destinationPanel = this.createDestinationUi();
      this.parametersDefiner = new ExportParametersDefiner(var1);
      this.commentArea = this.createCommentArea();
      this.inputPanel = this.createInputPanel();
      this.inputSiteImplementor = new InputPanelSiteImplementor(this.inputPanel);
      this.setInitialState();
   }

   private FileSelectorPanel createDestinationUi() {
      DefaultFileChooser var1 = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
      var1.setOverwriteConfirmer(FileChooser.ALWAYS_ALLOW_OVERWRITES);
      RecentFileStore var2 = getRecentFileStore();
      FileSelectorPanelFactory var3 = FileSelectorFactory.getFactory();
      FileSelectorPanel var4 = var3.singleFilePanel(var1, FileSelectorPanel.SAVE, Strings.getJitterPackString("Export.FileSelector.Title"), var2);
      var4.setFileFilter(JitterPackerUi.FILE_FILTER);
      var4.setPathValidator(PathValidators.FILES_ONLY);
      LabelDecorator var5 = TextStyles.InputLabelText.asLabelDecorator(JitterpackResources.EXPORT_16);
      var4.decorateTitle(var5);
      var4.displayLastFile();
      var4.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (ExportJitterPackUi.this.doExportAction != null) {
               ExportJitterPackUi.this.doExportAction.actionPerformed(var1);
            }
         }
      });
      return var4;
   }

   private static RecentFileStore getRecentFileStore() {
      PreferencesKey var0 = new PreferencesKey(ExportJitterPackUi.class, "SelectedFile");
      return new PreferencesFileStore(var0, 1);
   }

   private TextInputField createCommentArea() {
      KongaTextArea var1 = new KongaTextArea(this.getProjectToExport().getProject().getJitterPackComment(), 10, 40).disableTabs();
      String var2 = Strings.get("Export.Comment.Title");
      var1.setTextWhenEmpty("Enter an optional comment for this backup");
      return new TextInputField(var2, var1, var2);
   }

   private InputPanel createInputPanel() {
      InputPanel var1 = new InputPanel(new FlowLayout(3, 0, 0));
      var1.addInputPanel(this.destinationPanel);
      var1.addInputField(this.commentArea);
      return var1;
   }

   private void setInitialState() {
      this.destinationPanel.setSelectedFile(this.getFileSuggestion());
   }

   private File getFileSuggestion() {
      IntegrationProject var1 = this.getProjectToExport().getProject();
      File var2 = this.destinationPanel.getSelectedFile();
      return ExportDestinationSuggester.instance().getSuggestedDestination(var1, var2);
   }

   public ManagedProject getProjectToExport() {
      return this.project.getExtensionObject(ManagedProject.class);
   }

   public String getComment() {
      return this.commentArea.getValue().trim();
   }

   public File getDestination() {
      File var1 = this.destinationPanel.getSelectedFile();
      if (var1 != null) {
         IntegrationProject var2 = this.getProjectToExport().getProject();
         ExportDestinationSuggester.instance().addDestination(var2, var1);
      }

      return var1;
   }

   public void setDoExportAction(Action var1) {
      this.doExportAction = var1;
   }

   public ExportParameters getExportParameters() {
      return this.parametersDefiner.getParameters();
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.undoRedoController != null) {
         this.undoRedoController.dispose();
      }

      JComponent var2 = this.createUi();
      var1.display(var2);
      this.undoRedoController = UndoRedoControllers.textFactory().newController(var2);
   }

   private JComponent createUi() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(5, 5, 10, 5);
      var1.setWeightX(1.0);
      var1.add(this.createCaptionCapsule());
      var1.add(this.destinationPanel);
      var1.add(this.parametersDefiner);
      var1.fillOut(this.createCommentAreaCapsule());
      this.addExportButton(var1);
      this.inputPanel.setLayout(new BorderLayout());
      var1.addToParent(this.inputPanel.getUi(), "Center");
      UiUtils.setOpaqueRecursivelySkipDefaults(this.inputPanel.getUi(), false);
      return this.inputPanel.getUi();
   }

   private void addExportButton(OneColumnPanel var1) {
      if (this.doExportAction != null) {
         var1.noFill(new KongaButton(this.doExportAction));
      }
   }

   private JComponent createCaptionCapsule() {
      return TextStyles.InfoTextHeader.makeLabel(getCaption(this.getProjectToExport()));
   }

   private JComponent createCommentAreaCapsule() {
      return InputFieldFactory.getCapsule(this.commentArea, true);
   }

   private static String getCaption(ManagedProject var0) {
      return Strings.format("Export.Caption", var0.getProject().getName(), "Backup");
   }

   @Override
   public void requestFocus() {
      this.destinationPanel.requestFocus();
   }

   @Override
   public boolean isInputValid() {
      return this.destinationPanel.isInputValid();
   }

   @Override
   public void setSendInputChangeEvents(boolean var1) {
      this.inputSiteImplementor.setSendInputChangeEvents(var1);
   }

   @Override
   public boolean isSendingInputChangeEvents() {
      return this.inputSiteImplementor.isSendingInputChangeEvents();
   }

   @Override
   public void addInputChangeListener(InputChangeListener var1) {
      this.inputSiteImplementor.addInputChangeListener(var1);
   }

   @Override
   public void removeInputChangeListener(InputChangeListener var1) {
      this.inputSiteImplementor.removeInputChangeListener(var1);
   }

   @Override
   public void setSendUndoEvents(boolean var1) {
      this.inputSiteImplementor.setSendUndoEvents(var1);
   }

   @Override
   public boolean isSendingUndoEvents() {
      return this.inputSiteImplementor.isSendingUndoEvents();
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.inputSiteImplementor.addUndoableEditListener(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.inputSiteImplementor.removeUndoableEditListener(var1);
   }

   @Override
   public void validateInput() throws InputPanelException {
      this.inputSiteImplementor.validateInput();
   }

   @Override
   public void decorateFields() {
      this.inputSiteImplementor.decorateFields();
   }

   @Override
   public void dispose() {
      this.inputSiteImplementor.dispose();
      if (this.undoRedoController != null) {
         this.undoRedoController.dispose();
      }
   }

   @Override
   public void setActive(boolean var1) {
      this.inputSiteImplementor.setActive(var1);
   }

   @Override
   public void visitAllFields(InputFieldVisitor var1) {
      this.inputSiteImplementor.visitAllFields(var1);
   }
}

