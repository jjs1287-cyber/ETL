package org.etlforge.integration.loadsource.client;

import java.io.File;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.loadsource.client.resources.LoadSourceResources;
import org.etlforge.system.KeyValue;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.option.OptionSelector;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class SelectNewFileOption implements SourceFileOption {
   private static final String INCLUDE_EXTERNAL_FILE_IN_PROJECT = "IncludeInProject";
   private final RecentFileStore fileStore;
   private final KeyValue<String, Boolean> includeExternalFilesInProjectPreference;
   private final FileSelectorPanel fileSelector;
   private final JCheckBox includeInProjectOption;
   private boolean isActive;

   @Override
   public void setFinalSelectionMonitor(Receiver<Void> var1) {
   }

   public SelectNewFileOption(ApplicationWindow var1, RecentFileStore var2, KeyValue<String, Boolean> var3) {
      this.fileStore = var2;
      this.includeExternalFilesInProjectPreference = var3;
      this.fileSelector = this.createFileSelector(var1);
      this.includeInProjectOption = this.createIncludeInProjectOption();
      this.setActive(false);
   }

   private FileSelectorPanel createFileSelector(ApplicationWindow var1) {
      FileSelectorPanelFactory var2 = FileSelectorFactory.getFactory();
      return var2.singleFilePanel(ApplicationFileChooser.getInstance(var1), FileSelectorPanel.OPEN, null, this.fileStore);
   }

   private JCheckBox createIncludeInProjectOption() {
      String var1 = LoadSourceResources.getString("SourceDataFileProvider.IncludeInProject");
      boolean var2 = this.includeExternalFilesInProjectPreference.get("IncludeInProject", true);
      KongaCheckBox var3 = new KongaCheckBox(var1, var2);
      var3.setToolTipText(LoadSourceResources.getString("SourceDataFileProvider.IncludeInProject.ToolTip"));
      return var3;
   }

   @Override
   public boolean includeSelectionInProject() {
      return this.includeInProjectOption.isSelected();
   }

   public File getValue() {
      File var1 = this.fileSelector.getSelectedFile();
      this.updateSettingsPreferences();
      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5);
      var2.north(this.fileSelector).south(this.includeInProjectOption);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fileSelector.requestFocus();
   }

   @Override
   public void decorate(OptionSelector var1) {
      String var2 = LoadSourceResources.getString("SourceDataFileProvider.SelectNew");
      var1.setText(var2);
   }

   @Override
   public void setActive(boolean var1) {
      this.isActive = var1;
      this.fileSelector.setActive(var1);
      this.includeInProjectOption.setEnabled(var1);
   }

   @Override
   public boolean isActive() {
      return this.isActive;
   }

   private void updateSettingsPreferences() {
      this.includeExternalFilesInProjectPreference.set("IncludeInProject", this.includeInProjectOption.isSelected());
   }
}
