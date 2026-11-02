package org.etlforge.integration.client.server.ui.user.profile;

import java.awt.Color;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.DefaultUiJobProgressMonitor;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.server.login.LoginConfiguration;
import org.etlforge.integration.user.User;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;

public final class UserProfileDialog extends WizardStyleDialog {
   private final LoginConfiguration loginConfig;
   private final DownloadUserProfileJob downloadProcess;
   private final DefaultUiJobProgressMonitor downloadMonitor;
   private DefaultUiJobProgressMonitor uploadMonitor;
   private final UserProfilePanel profilePanel;
   private UiJob currentProcess;
   private JComponent progressOverlay;

   private UserProfileDialog(LoginConfiguration var1, Window var2) {
      super(var2, Strings.get("UserProfile.Title"));
      this.loginConfig = var1;
      this.downloadProcess = new DownloadUserProfileJob(this);
      this.downloadMonitor = new DefaultUiJobProgressMonitor(this.downloadProcess, false);
      this.profilePanel = new UserProfilePanel();
      this.getDefaultLayout().center(wrapUi(this.profilePanel));
      this.getOKButton().setEnabled(false);
      this.pack();
      this.manageLocation();
      this.setResizable(false);
      this.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
   }

   public static void showProfile(final Window var0, final LoginConfiguration var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            UserProfileDialog var1x = new UserProfileDialog(var1, var0);
            var1x.setVisible(true);
         }
      });
   }

   @Override
   protected void onDialogClose() {
      if (this.currentProcess != null) {
         ApplicationUi.getJobManager().cancelJob(this.currentProcess);
      }

      super.onDialogClose();
   }

   @Override
   protected void onDialogOpened() {
      super.onDialogOpened();
      this.downloadProcess.addProgressMonitor(this.downloadMonitor);
      this.currentProcess = this.downloadProcess;
      this.downloadProcess.submit();
      this.displayProgressMonitor(this.downloadMonitor);
   }

   @Override
   protected void onOk() {
      User var1 = this.profilePanel.applyToUser();
      UploadUserProfileJob var2 = new UploadUserProfileJob(this.loginConfig, this, var1);
      this.createAndShowJobProgressDisplayer(var2);
      this.currentProcess = var2;
      var2.submit();
      this.getOKButton().setEnabled(false);
   }

   private void createAndShowJobProgressDisplayer(UiJob var1) {
      this.uploadMonitor = new DefaultUiJobProgressMonitor(var1, false);
      var1.addProgressMonitor(this.uploadMonitor);
      this.displayProgressMonitor(this.uploadMonitor);
   }

   private void displayProgressMonitor(UiProvider var1) {
      this.removeOverlay(this.progressOverlay);
      JPanel var2 = new JPanel();
      ContainerCanvas.decorate(var1, var2);
      var2.setBackground(new Color(250, 250, 254));
      var2.setBorder(new LineBorder(Color.DARK_GRAY));
      var2.putClientProperty("konga.overlay.blocking", Boolean.TRUE);
      this.progressOverlay = var2;
      this.addOverlay(var2, new OverlayCenterPositioner());
   }

   void onUserInfoDownloaded(User var1) {
      this.currentProcess = null;
      this.profilePanel.populate(var1);
      this.removeOverlay(this.progressOverlay);
      this.getOKButton().setEnabled(true);
      this.setOkButtonAsDefault();
   }

   void onUploadFailed() {
      this.removeOverlay(this.progressOverlay);
      this.getOKButton().setEnabled(true);
   }
}
