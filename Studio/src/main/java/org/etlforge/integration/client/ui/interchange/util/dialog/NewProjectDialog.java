package org.etlforge.integration.client.ui.interchange.util.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComponent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.location.ProjectBrowserLocationDisplayer;
import org.etlforge.integration.client.ui.project.location.ProjectLocationBrowser;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.Empty;
import org.etlforge.ui.Enablabes;
import org.etlforge.ui.Enablable;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class NewProjectDialog {
   private final ProjectManager projectManager;
   private final ProjectBrowserLocationDisplayer inputForm;
   private String currentProjectName;
   private File currentProjectLocation;
   private KongaDialog dialog;

   public NewProjectDialog(ProjectManager var1, boolean var2) {
      this.projectManager = var1;
      this.initNameAndLocation(var2);
      this.inputForm = this.createDisplayer();
      this.createDialog();
      this.bindOkButtonEnabler();
   }

   private void initNameAndLocation(boolean var1) {
      File var2 = this.projectManager.getCurrentProjectLocation();
      this.currentProjectLocation = var2 == null ? null : var2.getParentFile();
      if (var1) {
         this.currentProjectName = this.projectManager.getCurrentProjectName();
      } else if (this.currentProjectName == null) {
         this.currentProjectName = this.projectManager.getProjectDirectory().getSuggestedNewProjectName(this.currentProjectLocation);
      }
   }

   private ProjectBrowserLocationDisplayer createDisplayer() {
      ProjectBrowserLocationDisplayer var1 = ProjectLocationBrowser.getNewProjectBrowser(this.currentProjectName, this.currentProjectLocation);
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            NewProjectDialog.this.dialog.getOKButton().doClick();
         }
      });
      return var1;
   }

   private void createDialog() {
      JComponent var1 = this.layoutDisplayer();
      this.dialog = new WizardStyleDialog(UiUtils.getActiveWindow(), PackageResources.NewProject.TITLE, var1) {
         @Override
         protected void onDialogOpened() {
            NewProjectDialog.this.inputForm.highlightProjectName();
         }

         @Override
         protected void onOk() {
            try {
               NewProjectDialog.this.inputForm.validateProjectName();
               if (this.checkNewProjectLocationValid()) {
                  super.onOk();
               }
            } catch (IllegalNameException var2) {
               Alert.error(this.getOwner(), var2.getMessage(), PackageResources.NewProject.INVALID_PROJECT_NAME);
            }
         }

         private boolean checkNewProjectLocationValid() {
            String var1x = NewProjectDialog.this.getProjectName();
            if (var1x.length() == 0) {
               return false;
            }

            boolean var2 = true;
            File var3 = NewProjectDialog.this.getLocationOnDisk();
            if (NewProjectDialog.differs(var1x, NewProjectDialog.this.currentProjectName)
               || NewProjectDialog.differs(var3, NewProjectDialog.this.currentProjectLocation)) {
               File var4 = new File(var3, var1x);
               if (var4.exists()) {
                  var2 = NewProjectDialog.this.approveOverwrite(var4);
               }
            }

            return var2;
         }
      };
      this.dialog.getOKButton().setEnabled(this.isProjectNameValid());
      this.dialog.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      this.dialog.setOkButtonAsDefault();
      this.dialog.manageLocation();
   }

   private JComponent layoutDisplayer() {
      JComponent var1 = this.inputForm.getComponent();
      var1.setOpaque(true);
      var1.setBorder(Empty.border(10));
      UiUtils.setPanelBackgrounds(var1, Colors.NEUTRAL_BACKGROUND);
      return var1;
   }

   private void bindOkButtonEnabler() {
      Enablable var1 = Enablabes.fromComponent(this.dialog.getOKButton());
      this.inputForm.addValidInputSensor(var1);
   }

   private boolean isProjectNameValid() {
      try {
         NameValidator var1 = NameValidatorStore.getInstance().get(IntegrationEntity.class);
         var1.validateName(this.currentProjectName);
         return true;
      } catch (IllegalNameException var2) {
         return false;
      }
   }

   private String getProjectName() {
      return this.inputForm.getProjectName();
   }

   private File getLocationOnDisk() {
      return this.inputForm.getProjectLocation();
   }

   public boolean open() {
      this.dialog.setVisible(true);
      boolean var1 = this.dialog.wasOkPressed();
      this.dialog = null;
      return var1;
   }

   public ProjectLocation getProjectLocation() {
      this.dialog = null;
      return new ProjectLocation(this.getProjectName(), this.getLocationOnDisk());
   }

   private boolean approveOverwrite(File var1) {
      String var2 = PackageResources.NewProject.getLocationExistsMessage(var1.getAbsolutePath());
      Answer var3 = Ask.yesOrNo(this.dialog, var2, PackageResources.NewProject.LOCATION_EXISTS_TITLE, Alert.Option.NO, Alert.Type.WARNING);
      return var3.isYes();
   }

   private static boolean differs(Object var0, Object var1) {
      return var0 == null ? var1 != null : !var0.equals(var1);
   }
}
