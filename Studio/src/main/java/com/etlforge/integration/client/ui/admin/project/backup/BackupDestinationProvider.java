package com.etlforge.integration.client.ui.admin.project.backup;

import java.io.File;
import javax.swing.JLabel;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.FolderSelectorPanelBuilder;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.project.jitterpack.JitterPackConstants;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.file.WindowsFileNameValidator;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.preference.PreferencesKey;

public final class BackupDestinationProvider {
   private final IntegrationProject project;

   public BackupDestinationProvider(IntegrationProject var1) {
      this.project = var1;
   }

   public File getDestination() {
      BackupDestinationProvider.InputUi var1 = new BackupDestinationProvider.InputUi(this.project);
      KongaDialog var2 = this.createDialog(var1);
      var2.setVisible(true);
      return var2.wasOkPressed() ? var1.getDestination() : null;
   }

   private KongaDialog createDialog(final BackupDestinationProvider.InputUi var1) {
      WizardStyleDialog var2 = new WizardStyleDialog(UiUtils.getActiveWindow(), "Download Backup", var1) {
         @Override
         protected void onOk() {
            File var1x = var1.getDestination();
            if (var1x != null) {
               super.onOk();
            } else {
               Alert.error("Invalid input.", "Invalid Input");
            }
         }
      };
      var2.setOkButtonAsDefault();
      var2.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      var2.manageLocation(BackupDestinationProvider.class);
      return var2;
   }

   private static class InputUi implements UiProvider {
      private final KongaTextField nameField;
      private final FileSelectorPanel folderSelector;

      public InputUi(IntegrationProject var1) {
         this.nameField = new KongaTextField(JitterPackConstants.getSuggestedJitterpakName(var1));
         this.nameField.setSelectAllWhenFocused(true);
         this.folderSelector = this.createFolderSelector();
      }

      public File getDestination() {
         String var1 = this.nameField.getText().trim();
         File var2 = this.folderSelector.getSelectedFile();
         return this.isNameValid(var1) && var2 != null ? new File(var2, var1) : null;
      }

      @Override
      public void decorate(UiCanvas var1) {
         TwoColumnPanel var2 = TwoColumnPanel.forFormEntry(10, 10);
         var2.addRow(new JLabel("Save as Jitterpak:"), this.nameField);
         var2.addRow(UiProviderAdapter.label("In folder:"), this.folderSelector);
         var2.decorate(var1);
         Vertical.Anchor.north(var2).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.nameField.requestFocusInWindow();
      }

      private FileSelectorPanel createFolderSelector() {
         FolderSelectorPanelBuilder var1 = new FolderSelectorPanelBuilder();
         var1.setPreferenceKey(new PreferencesKey(BackupDestinationProvider.class, "Destination"));
         var1.setDefaultFolder(new File(SystemUtils.USER_DIR));
         return var1.buildPanel(null);
      }

      private boolean isNameValid(String var1) {
         try {
            new WindowsFileNameValidator().validate(var1);
            return true;
         } catch (IllegalNameException var3) {
            return false;
         }
      }
   }
}
