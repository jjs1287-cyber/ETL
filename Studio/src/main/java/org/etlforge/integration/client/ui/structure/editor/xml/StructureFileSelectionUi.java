package org.etlforge.integration.client.ui.structure.editor.xml;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.EnumSet;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureWizard;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.RadioButtonEnumBinding;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.util.preference.PreferencesKey;

public final class StructureFileSelectionUi implements UiProvider {
   private static final String LATEST_LOCAL_FILE = "LatestLocalFile";
   private final KongaRadioButton existingStructureChoice;
   private final KongaRadioButton uploadLocalFileChoice;
   private final FileSelectorPanel localFileSelector;
   private final KongaRadioButton fromSampleFileChoice;
   private final ExistingFileSelectionUi existingFileSelector;

   public StructureFileSelectionUi(StructureFileSelectionModel var1) {
      EnumSet var2 = var1.getAllInputModes();
      if (var2.contains(FileInputMode.LOCAL)) {
         this.existingStructureChoice = new KongaRadioButton("Select an &existing XML structure");
         this.existingFileSelector = ExistingFileSelectionUi.forXml(var1);
         this.uploadLocalFileChoice = new KongaRadioButton("&Upload an XML structure file (DTD or Schema)");
         this.localFileSelector = this.createFileSelector();
         this.fromSampleFileChoice = new KongaRadioButton("Create a &new XML structure from a sample file (next page)");
      } else {
         this.existingStructureChoice = new KongaRadioButton("Select an &existing JSON structure");
         this.existingFileSelector = ExistingFileSelectionUi.forJson(var1);
         this.uploadLocalFileChoice = null;
         this.localFileSelector = null;
         this.fromSampleFileChoice = new KongaRadioButton("Create a &new JSON structure from a sample file (next page)");
      }

      this.listenToUi();
      this.bindProperties(var1);
   }

   private FileSelectorPanel createFileSelector() {
      DefaultFileChooser var1 = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
      RecentFileStore var2 = this.getRecentFileStore();
      FileSelectorPanelFactory var3 = FileSelectorFactory.getFactory();
      FileSelectorPanel var4 = var3.recentFilesPanel(var1, FileSelectorPanel.OPEN, "Select an XSD or DTD file for upload", var2);
      var4.setFileFilter(new SuffixFileChooserFilter(".xsd", ".dtd"));
      var4.setUseTitleLabel(false);
      return var4;
   }

   private RecentFileStore getRecentFileStore() {
      PreferencesKey var1 = new PreferencesKey(XmlStructureWizard.class, "LatestLocalFile");
      return new PreferencesFileStore(var1, 10);
   }

   private void listenToUi() {
      FocusUtils.bindChoiceComponents(this.existingStructureChoice, this.existingFileSelector);
      if (this.localFileSelector != null) {
         FocusUtils.bindChoiceComponents(this.uploadLocalFileChoice, this.localFileSelector);
      }
   }

   private void bindProperties(StructureFileSelectionModel var1) {
      EnumMap var2 = Maps.newEnumMap(FileInputMode.class);
      var2.put(FileInputMode.EXISTING, this.existingStructureChoice);
      if (this.uploadLocalFileChoice != null) {
         var2.put(FileInputMode.LOCAL, this.uploadLocalFileChoice);
      }

      var2.put(FileInputMode.FROM_SAMPLE_FILE, this.fromSampleFileChoice);
      RadioButtonEnumBinding var3 = RadioButtonEnumBinding.forButtons(var1.getInputModeProperty(), var2);
      var3.syncUi();
      if (this.localFileSelector != null) {
         Binding var4 = this.localFileSelector.bindWith(var1.getLocalFileProperty());
         var4.syncUi();
      }
   }

   public void useForEdi() {
      this.existingStructureChoice.setText("&Select an existing transaction set from the server");
      this.uploadLocalFileChoice.setText("&Upload a new transaction set definition file (XSD)");
      this.fromSampleFileChoice.setEnabled(false);
      if (this.fromSampleFileChoice.isSelected()) {
         this.existingStructureChoice.setSelected(true);
      }
   }

   public void setDefaultAction(final Action var1) {
      this.existingFileSelector.setDefaultAction(new AbstractSelectionAction() {
         @Override
         public void actionPerformed(ActionEvent var1x) {
            var1.actionPerformed(var1x);
         }
      });
      if (this.localFileSelector != null) {
         this.localFileSelector.addActionListener(var1);
      }
   }

   public void setRefreshAction(Action var1) {
      this.existingFileSelector.setRefreshAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      Border var2 = Empty.border(0, 0, 10, 0);
      if (this.localFileSelector == null) {
         BorderLayoutBuilder var3 = new BorderLayoutBuilder();
         var3.northToSouth(this.existingStructureChoice, wrapFileSelector(this.existingFileSelector), this.fromSampleFileChoice);
         var3.withBorder(var2);
         var3.decorate(var1);
      } else {
         OneColumnPanel var4 = new OneColumnPanel().withBorder(var2).setWeightX(1.0).setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
         var4.add(this.existingStructureChoice);
         var4.setWeightY(1.0);
         var4.setFill(GridBagLayoutPanel.Fill.BOTH);
         var4.add(wrapFileSelector(this.existingFileSelector));
         var4.setWeightY(0.0);
         var4.setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
         var4.addAll(this.uploadLocalFileChoice, wrapFileSelector(this.localFileSelector));
         var4.add(this.fromSampleFileChoice);
         var4.decorate(var1);
      }
   }

   private static JComponent wrapFileSelector(UiProvider var0) {
      JPanel var1 = BorderLayoutBuilder.fillWith(var0);
      var1.setBorder(Empty.border(8, 32, 24, 0));
      var1.setOpaque(false);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.existingFileSelector.requestFocus();
   }

   public void setFileSelectionAction(SelectionAction var1) {
      this.existingFileSelector.setDefaultAction(var1);
   }
}
