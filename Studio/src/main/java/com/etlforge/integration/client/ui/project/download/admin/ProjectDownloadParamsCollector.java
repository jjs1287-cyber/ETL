package com.etlforge.integration.client.ui.project.download.admin;

import com.etlforge.integration.client.ui.project.download.ProjectDownloadParams;
import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.io.File;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.widget.FolderSelectorPanelBuilder;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.project.ProjectDirectory;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.util.preference.PreferencesKey;

final class ProjectDownloadParamsCollector implements UiProvider {
   private final ProjectDirectory applicationProjectDir;
   private final IntegrationProject project;
   private final FileSelectorPanel destinationSelector;
   private final JCheckBox openWhenDownloadedChoice;

   public ProjectDownloadParamsCollector(ProjectDirectory var1, IntegrationProject var2) {
      this.applicationProjectDir = var1;
      this.project = var2;
      this.destinationSelector = this.createDestinationSelector();
      this.openWhenDownloadedChoice = this.createOpenProjectChoice();
   }

   public ProjectDownloadParams getParams() {
      WizardStyleDialog var1 = new WizardStyleDialog(
         UiUtils.getActiveWindow(), ProjectDownloadResources.getString("Admin.Params.Title"), this, KongaDialog.CONTINUE_CANCEL, true
      );
      var1.setContinueButtonAsDefault();
      var1.setLocationRelativeTo(null);
      var1.setVisible(true);
      return var1.wasOkPressed() ? new ProjectDownloadParams(this.project, this.destinationSelector.getSelectedFile()) : null;
   }

   public boolean isOpenWhenDownloadedSelected() {
      return this.openWhenDownloadedChoice.isSelected();
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = ProjectDownloadResources.getString("Admin.Params.Caption");
      new OneColumnPanel().setWeightX(1.0).of(var2, this.destinationSelector, this.openWhenDownloadedChoice).withInsets(0, 0, 3, 0).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.destinationSelector.requestFocus();
   }

   private FileSelectorPanel createDestinationSelector() {
      FolderSelectorPanelBuilder var1 = new FolderSelectorPanelBuilder();
      var1.setPreferenceKey(new PreferencesKey(ProjectDownloadParamsCollector.class, "Destination"));
      File var2 = this.applicationProjectDir.getLocation();
      var1.setDefaultFolder(var2);
      return var1.buildPanel(null);
   }

   private JCheckBox createOpenProjectChoice() {
      KongaCheckBox var1 = new KongaCheckBox(ProjectDownloadResources.getString("Admin.Params.Open"));
      var1.setBorder(Empty.border(10, 0, 0, 0));
      return var1;
   }
}
