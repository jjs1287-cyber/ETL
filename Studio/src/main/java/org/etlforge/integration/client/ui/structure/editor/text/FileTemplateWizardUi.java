package org.etlforge.integration.client.ui.structure.editor.text;

import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.etlforge.application.ui.help.ApplicationUiHelp;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.widget.FileContentDisplayer;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.Prototype;
import org.etlforge.integration.data.structure.text.SegmentType;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.util.file.FileSelectionListener;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.text.KongaTextArea;
import org.etlforge.util.preference.PreferencesKey;

final class FileTemplateWizardUi implements UiProvider, Disposable, FileSelectionListener {
   private static final String HELP_ID = "text_structure_from_file_template";
   private final ApplicationWindow appWin;
   private final JComboBox typeSelector;
   private final DelimiterInputField delimiterField;
   private final FileSelectorPanel fileSelector;
   private final JTextArea fileContentArea;
   private final JButton helpButton;
   private FileContentDisplayer fileReader;
   private Enablable validationListener;
   private boolean includeHelpButtonInLayout;

   public FileTemplateWizardUi(ApplicationWindow var1, SegmentType var2) {
      this.appWin = var1;
      this.typeSelector = this.createTypeSelector(var2);
      this.fileSelector = this.createFileSelector();
      this.delimiterField = this.createDelimiterField(var2);
      this.fileSelector.addFileSelectionListener(this);
      this.fileContentArea = this.createFileContentArea();
      this.helpButton = ApplicationUiHelp.getHelp().createAndConfigureHelpButton("text_structure_from_file_template", true);
      this.includeHelpButtonInLayout = true;
   }

   private JComboBox createTypeSelector(SegmentType var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var1 == SegmentType.DELIMITED) {
         var2.add(new FileTemplateWizardUi.PrototypeItem(Prototype.SampleFlatFileWithHeader));
         var2.add(new FileTemplateWizardUi.PrototypeItem(Prototype.SampleFlatFile));
      }

      var2.add(new FileTemplateWizardUi.PrototypeItem(Prototype.SimpleTextDef));
      KongaComboBox var3 = new KongaComboBox(var2);
      var3.setRenderer(new DefaultListDecorRenderer());
      var3.setSelectedIndex(0);
      var3.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            FileTemplateWizardUi.this.delimiterField.setEnabled(FileTemplateWizardUi.this.getPrototype() != Prototype.SimpleTextDef);
            FileTemplateWizardUi.this.notifyValidationListener();
         }
      });
      return var3;
   }

   private void notifyValidationListener() {
      if (this.validationListener != null) {
         boolean var1 = this.getFile() != null && this.isDelimiterValid();
         this.validationListener.setEnabled(var1);
      }
   }

   private boolean isDelimiterValid() {
      return !this.delimiterField.isEnabled() || this.getDelimiter().isValid();
   }

   private FileSelectorPanel createFileSelector() {
      FileSelectorPanelFactory var1 = FileSelectorFactory.getFactory();
      return var1.recentFilesPanel(
         ApplicationFileChooser.getInstance(this.appWin), FileSelectorPanel.OPEN, PackageResources.FileTemplateWizard.FILE_SELECTOR, this.getRecentFileStore()
      );
   }

   private RecentFileStore getRecentFileStore() {
      PreferencesKey var1 = new PreferencesKey(SimpleStructureFileTemplateWizard.class, "File");
      return new PreferencesFileStore(var1, 10);
   }

   private DelimiterInputField createDelimiterField(SegmentType var1) {
      DelimiterInputField var2 = new DelimiterInputField(FieldDelimiter.COMMA);
      if (var1 == SegmentType.DELIMITED) {
         var2.setSendInputChangeEvents(true);
         var2.addInputChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1) {
               FileTemplateWizardUi.this.notifyValidationListener();
            }
         });
      } else {
         var2.setEnabled(false);
      }

      return var2;
   }

   private JTextArea createFileContentArea() {
      KongaTextArea var1 = KongaTextArea.viewer(Size.rows(6), Size.columns(40));
      var1.setLineWrap(false);
      TextStyles.DefaultMonoSpaced.makeOver(var1);
      return var1;
   }

   @Override
   public void dispose() {
      ApplicationUiHelp.getHelp().disableHelpOnButton(this.helpButton);
   }

   public void setIncludeHelpButtonInLayout(boolean var1) {
      this.includeHelpButtonInLayout = var1;
   }

   public JButton getHelpButton() {
      return this.helpButton;
   }

   public Prototype getPrototype() {
      return ((FileTemplateWizardUi.PrototypeItem)this.typeSelector.getSelectedItem()).prototype;
   }

   public File getFile() {
      return this.fileSelector.getSelectedFile();
   }

   public FieldDelimiter getDelimiter() {
      return this.delimiterField.getDelimiter();
   }

   @Override
   public void selectionChanged(File var1) {
      this.notifyValidationListener();
      if (var1 != null) {
         if (var1.exists() && !var1.isDirectory()) {
            if (this.fileReader != null) {
               this.fileReader.cancel(true);
            }

            this.fileReader = new FileContentDisplayer(var1, this.fileContentArea);
            this.fileReader.execute();
         }
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(10, 5, 15, 0).withBorder(new BorderBuilder().etched().empty(5, 10, 5, 10));
      this.addTypeAndDelimiterDefinersToLayout(var2);
      var2.setWeightX(1.0);
      var2.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
      this.addFileSelectorToLayout(var2);
      this.addFileContentAreaToLayout(var2);
      this.addHelpButtonToLayout(var2);
      var2.decorate(var1);
   }

   private void addTypeAndDelimiterDefinersToLayout(OneColumnPanel var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      var2.withInsets(0, 0, 3, 20);
      var2.withRightAnchor(TwoColumnPanel.SOUTHWEST);
      var2.addRow(new JLabel(PackageResources.FileTemplateWizard.TYPE_SELECTOR), this.delimiterField.getLabel());
      var2.addRow(this.typeSelector, this.delimiterField.getInputComponent());
      var1.withInsets(10, 0, 20, 0);
      var1.noFill(var2);
   }

   private void addFileSelectorToLayout(OneColumnPanel var1) {
      var1.withInsets(0, 0, 10, 0);
      var1.add(this.fileSelector);
   }

   private void addFileContentAreaToLayout(OneColumnPanel var1) {
      var1.withInsets(0, 0, 3, 0);
      var1.add(PackageResources.FileTemplateWizard.FILE_CONTENT);
      var1.withInsets(0, 0, 10, 5);
      var1.fillOut(new JScrollPane(this.fileContentArea));
   }

   private void addHelpButtonToLayout(OneColumnPanel var1) {
      if (this.includeHelpButtonInLayout) {
         var1.setAnchor(OneColumnPanel.SOUTHEAST);
         var1.noFill(this.helpButton);
      }
   }

   @Override
   public void requestFocus() {
      this.typeSelector.requestFocusInWindow();
   }

   public void setValidationListener(Enablable var1) {
      this.validationListener = var1;
   }

   private static final class PrototypeItem implements ListDecorator {
      public final Prototype prototype;

      public PrototypeItem(Prototype var1) {
         this.prototype = var1;
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         String var3 = this.prototype.getDisplayName();
         var1.setText(var3);
         var1.setToolTip(var3);
      }
   }
}
