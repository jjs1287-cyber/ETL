package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.application.ui.wizard.NumberedSectionsLayout;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.FileNameField;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.preference.PreferencesKey;

public final class SampleFileToXsdUi implements UiProvider {
   private static final String LATEST_SAMPLE_FILE = "LatestSampleFile";
   private SampleFileToXsdController controller;
   private final KongaRadioButton localFileChoice;
   private final FileSelectorPanel fileSelector;
   private final KongaRadioButton remoteFileChoice;
   private final KongaTextField urlField;
   private final FileNameField xsdFileNameField;
   private final SampleFileToXsdUi.GenerateXsdAction generateXsdAction;
   private final XsdFileReview fileReview;
   private WaitService waitService = WaitService.NONE;
   private Receiver<CreateXsdResult> receiver = NoReceiver.create();
   public boolean includeXsdFileReview = true;

   public SampleFileToXsdUi(FileType var1) {
      this.localFileChoice = new KongaRadioButton("Use a &local " + var1 + " file", true);
      this.fileSelector = this.createFileSelector(var1);
      this.remoteFileChoice = new KongaRadioButton("&URL to a remote " + var1 + " file", true);
      this.urlField = new KongaTextField(40);
      this.urlField.setSelectAllWhenFocused(true);
      this.xsdFileNameField = new FileNameField("XSD File Name", "(Optional) XSD file &name:", 40);
      this.xsdFileNameField.setRequired(false);
      this.xsdFileNameField.setTextWhenEmpty("A file name will be generated automatically if empty");
      ButtonUtils.makeMutuallyExclusive(this.localFileChoice, this.remoteFileChoice);
      this.generateXsdAction = new SampleFileToXsdUi.GenerateXsdAction();
      this.fileReview = new XsdFileReview();
      this.listenToTheUi();
   }

   private FileSelectorPanel createFileSelector(FileType var1) {
      DefaultFileChooser var2 = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
      RecentFileStore var3 = this.getRecentFileStore(var1);
      FileSelectorPanelFactory var4 = FileSelectorFactory.getFactory();
      FileSelectorPanel var5 = var4.recentFilesPanel(var2, FileSelectorPanel.OPEN, "Select the " + var1 + " sample file", var3);
      List var6 = Arrays.asList(var1.getFileExtension(), ".zip", ".gz");
      var5.setFileFilter(new SuffixFileChooserFilter(var6));
      var5.setUseTitleLabel(false);
      var5.setActive(true);
      return var5;
   }

   private RecentFileStore getRecentFileStore(FileType var1) {
      PreferencesKey var2 = new PreferencesKey(this.getClass(), "LatestSampleFile." + var1.name());
      return new PreferencesFileStore(var2, 10);
   }

   private void listenToTheUi() {
      SampleFileToXsdUi.FieldListener var1 = new SampleFileToXsdUi.FieldListener();
      ButtonUtils.addActionListener(var1, this.localFileChoice, this.remoteFileChoice);
      this.fileSelector.addFileSelectionListener(var1);
      this.fileSelector.addActionListener(this.generateXsdAction);
      this.fileSelector.addFocusListener(var1);
      this.urlField.getDocument().addDocumentListener(var1);
      this.urlField.addActionListener(this.generateXsdAction);
      this.urlField.addFocusListener(var1);
      this.xsdFileNameField.addInputChangeListener(var1);
      this.xsdFileNameField.addActionListener(this.generateXsdAction);
   }

   public void showResultingFiles(CreateXsdResult var1) {
      this.fileReview.displayResult(var1);
      this.receiver.handle(var1);
   }

   public void clearResult() {
      this.showResultingFiles(null);
   }

   public void setController(SampleFileToXsdController var1) {
      this.controller = var1;
      this.updateGenerateXsdActionEnabled();
   }

   private void updateGenerateXsdActionEnabled() {
      boolean var1 = false;
      if (this.controller != null && this.xsdFileNameField.isFieldValid()) {
         if (this.localFileChoice.isSelected()) {
            var1 = this.fileSelector.getSelectedFile() != null;
         } else {
            String var2 = this.getUrl();
            var1 = !var2.isEmpty();
         }
      }

      this.generateXsdAction.setEnabled(var1);
   }

   public void setPreselectedFile(File var1) {
      this.fileSelector.setSelectedFile(var1);
   }

   public void setIncludeXsdFileReview(boolean var1) {
      this.includeXsdFileReview = var1;
   }

   public void highlightFileNameField() {
      FieldHighlighter var1 = new FieldHighlighter(this.xsdFileNameField.getInputComponent());
      var1.highlight();
   }

   @Override
   public void decorate(UiCanvas var1) {
      new SampleFileToXsdUi.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fileSelector.requestFocus();
   }

   public void setReceiver(Receiver<CreateXsdResult> var1) {
      this.receiver = var1;
   }

   public WaitService getWaitService() {
      return this.waitService;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   private String getUrl() {
      return this.urlField.getText().trim();
   }

   private class FieldListener extends DocumentContentChangeListener implements FileSelectionListener, ActionListener, FocusListener, InputChangeListener {
      private FieldListener() {
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         SampleFileToXsdUi.this.updateGenerateXsdActionEnabled();
      }

      @Override
      public void selectionChanged(File var1) {
         SampleFileToXsdUi.this.updateGenerateXsdActionEnabled();
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         SampleFileToXsdUi.this.updateGenerateXsdActionEnabled();
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.switchFocusedField(var1);
         SampleFileToXsdUi.this.updateGenerateXsdActionEnabled();
      }

      private void switchFocusedField(ActionEvent var1) {
         if (var1.getSource() == SampleFileToXsdUi.this.localFileChoice) {
            SampleFileToXsdUi.this.fileSelector.requestFocus();
         } else {
            SampleFileToXsdUi.this.urlField.requestFocusInWindow();
         }
      }

      @Override
      public void focusGained(FocusEvent var1) {
         if (var1.getSource() == SampleFileToXsdUi.this.urlField) {
            this.selectChoice(SampleFileToXsdUi.this.remoteFileChoice);
         } else {
            this.selectChoice(SampleFileToXsdUi.this.localFileChoice);
         }
      }

      private void selectChoice(KongaRadioButton var1) {
         if (!var1.isSelected()) {
            var1.setSelected(true);
            SampleFileToXsdUi.this.updateGenerateXsdActionEnabled();
         }
      }

      @Override
      public void focusLost(FocusEvent var1) {
      }
   }

   private class GenerateXsdAction extends AbstractAction {
      public GenerateXsdAction() {
         super("&Generate");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.isEnabled()) {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  GenerateXsdAction.this.removeCurrentResult();
                  GenerateXsdAction.this.generateXsd();
               }
            });
         }
      }

      private void removeCurrentResult() {
         SampleFileToXsdUi.this.receiver.handle(null);
         SampleFileToXsdUi.this.fileReview.displayResult(null);
      }

      private void generateXsd() {
         String var1 = this.getXsdFileName();
         if (SampleFileToXsdUi.this.localFileChoice.isSelected()) {
            SampleFileToXsdUi.this.controller.createFromLocalFile(SampleFileToXsdUi.this.fileSelector.getSelectedFile(), var1);
         } else {
            SampleFileToXsdUi.this.controller.createFromUrl(SampleFileToXsdUi.this.getUrl(), var1);
         }
      }

      private String getXsdFileName() {
         String var1 = SampleFileToXsdUi.this.xsdFileNameField.getValue().trim();
         if (!var1.isEmpty() && !var1.toLowerCase().endsWith(".xsd")) {
            var1 = var1 + ".xsd";
         }

         return var1;
      }
   }

   private class Layout {
      private final NumberedSectionsLayout layout = new NumberedSectionsLayout();

      public Layout() {
         this.layoutInputFields();
         this.layoutGenerateXsdSection();
         this.layoutReviewSection();
      }

      private void layoutInputFields() {
         TwoColumnPanel var1 = TwoColumnPanel.forFormEntry(5, 10);
         this.layoutLocalFileChoice(var1);
         this.layoutUrlField(var1);
         this.layout.addSection("Select a Sample File", var1.container());
      }

      public void layoutLocalFileChoice(TwoColumnPanel var1) {
         var1.withInsets(0, 0, 4, 0);
         var1.addRow(SampleFileToXsdUi.this.localFileChoice);
         var1.addRow(this.fakeInset().asUiProvider(), SampleFileToXsdUi.this.fileSelector);
      }

      private KongaLabel fakeInset() {
         return new KongaLabel("       ");
      }

      private void layoutUrlField(TwoColumnPanel var1) {
         var1.withInsets(12, 0, 4, 0);
         var1.addRow(SampleFileToXsdUi.this.remoteFileChoice);
         var1.withInsets(0, 0, 12, 0);
         var1.addRow(this.fakeInset(), SampleFileToXsdUi.this.urlField);
      }

      public void layoutGenerateXsdSection() {
         TwoColumnPanel var1 = TwoColumnPanel.forFormEntry(10, 10).withBorder(Empty.border(4, 0, 0, 0));
         var1.addRow(this.fakeInset().asUiProvider(), SampleFileToXsdUi.this.xsdFileNameField.asUiProvider());
         KongaButton var2 = new KongaButton(SampleFileToXsdUi.this.generateXsdAction);
         WizardStyleSheet.SMALL_CAPTION_STYLE.makeOver(var2);
         var1.addRow(this.fakeInset(), InvisiblePanel.of(var2));
         this.layout.addSection("Generate the XSD", var1);
      }

      private void layoutReviewSection() {
         if (SampleFileToXsdUi.this.includeXsdFileReview) {
            this.layout.addSection("Review the Result", SampleFileToXsdUi.this.fileReview);
         }
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }
}
