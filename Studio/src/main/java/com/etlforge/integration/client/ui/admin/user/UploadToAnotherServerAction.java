package com.etlforge.integration.client.ui.admin.user;

import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.look.AlertIcons;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.AnimationPreference;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.admin.component.user2.ActiveModelAction;
import org.etlforge.integration.client.ui.admin.component.user2.InvalidUserDataAlert;
import org.etlforge.integration.client.ui.admin.component.user2.InvalidUserDataException;
import org.etlforge.integration.client.ui.admin.component.user2.UserConsoleAction;
import org.etlforge.integration.client.ui.admin.component.user2.UserConsoleController;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayAlert;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;

public final class UploadToAnotherServerAction extends ActiveModelAction implements UserConsoleAction {
   private final ServerAdministration serverApi;
   private UserConsoleController controller;
   private OverlayContainer overlayContainer;

   public UploadToAnotherServerAction(ServerAdministration var1) {
      super(EnterpriseAdminResources.getString("User.UploadToAnotherServer.Name"), EnterpriseAdminResources.getIcon("MigrateUsers.16"));
      this.serverApi = var1;
      this.putValue("SwingLargeIconKey", EnterpriseAdminResources.getIcon("MigrateUsers.32"));
      this.putValue("ShortDescription", EnterpriseAdminResources.getString("User.UploadToAnotherServer.ToolTip"));
   }

   @Override
   public void setController(UserConsoleController var1) {
      this.controller = var1;
   }

   @Override
   public void setOverlayContainer(OverlayContainer var1) {
      this.overlayContainer = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            UploadToAnotherServerAction.this.runImpl();
         }
      });
   }

   private void runImpl() {
      if (this.validateModel()) {
         DestinationServerSelector var1 = new DestinationServerSelector(this.controller.getServerInfoStore(), this.getModel().getCurrentServer());
         UploadToAnotherServerAction.Editor var2 = this.getEditor(var1);
         var2.getServer(new Receiver<String>() {
            public void handle(String var1) {
               if (StringUtils.isNotBlank(var1)) {
                  UploadToAnotherServerAction.this.uploadToServer(var1);
               }
            }
         });
      }
   }

   private boolean validateModel() {
      try {
         this.getModel().validate();
         return true;
      } catch (InvalidUserDataException var2) {
         new InvalidUserDataAlert(var2, this.overlayContainer).show();
         return false;
      }
   }

   private UploadToAnotherServerAction.Editor getEditor(DestinationServerSelector var1) {
      return this.overlayContainer != null
         ? new UploadToAnotherServerAction.OverlayEditor(var1, this.overlayContainer)
         : new UploadToAnotherServerAction.DialogEditor(var1);
   }

   private void uploadToServer(String var1) {
      final MigrateUsersAndGroupsJob var2 = new MigrateUsersAndGroupsJob(this.serverApi, this.getModel().getUsers(), this.getModel().getGroups(), var1);
      var2.submit();
      ApplicationWorker var3 = Application.getWorker();
      var3.submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            if (var2.waitForSuccess()) {
               UploadToAnotherServerAction.this.showSuccessMessage();
            }
         }
      });
   }

   private void showSuccessMessage() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            String var1 = EnterpriseAdminResources.getString("User.UploadToAnotherServer.Success");
            String var2 = ApplicationResources.STRINGS.getString("Success");
            if (UploadToAnotherServerAction.this.overlayContainer != null) {
               OverlayAlert var3 = new OverlayAlert(var1, var2);
               var3.setTitleIcon(AlertIcons.INFO);
               var3.show(UploadToAnotherServerAction.this.overlayContainer);
            } else {
               Alert.info(var1, var2);
            }
         }
      });
   }

   private static class DialogEditor implements UploadToAnotherServerAction.Editor, InputChangeListener {
      private final DestinationServerSelector selector;
      private final KongaDialog dialog;

      public DialogEditor(DestinationServerSelector var1) {
         this.selector = var1;
         this.dialog = this.createDialog();
      }

      @Override
      public void getServer(Receiver<String> var1) {
         this.selector.addChangeListener(this);
         this.updateOkState();
         this.dialog.setVisible(true);
         this.selector.dispose();
         var1.handle(this.dialog.wasOkPressed() ? this.selector.getDestinationServer() : null);
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         this.updateOkState();
      }

      private void updateOkState() {
         this.dialog.getOKButton().setEnabled(this.selector.isInputValid());
      }

      private KongaDialog createDialog() {
         return UiProviderDialogDisplayer.createDialog(
            UiUtils.getActiveFrame(),
            this.selector,
            EnterpriseAdminResources.getString("User.Upload.Select.Title"),
            KongaDialog.OK_CANCEL,
            UploadToAnotherServerAction.class
         );
      }
   }

   private interface Editor {
      void getServer(Receiver<String> var1);
   }

   private static class OverlayEditor implements UploadToAnotherServerAction.Editor, InputChangeListener {
      private final DestinationServerSelector selector;
      private final OverlayContainer container;
      private Receiver<String> receiver;
      private OverlayDecorator overlay;

      public OverlayEditor(DestinationServerSelector var1, OverlayContainer var2) {
         this.selector = var1;
         this.container = var2;
      }

      @Override
      public void getServer(Receiver<String> var1) {
         this.receiver = var1;
         this.overlay = this.createOverlay();
         this.selector.addChangeListener(this);
         this.overlay.show(this.container, new OverlayCenterPositioner());
         this.updateOkState();
      }

      @Override
      public void inputChanged(InputChangeEvent var1) {
         this.updateOkState();
      }

      private OverlayDecorator createOverlay() {
         JPanel var1 = BorderLayoutBuilder.fillWith(this.selector);
         var1.setBorder(Empty.border(10));
         OverlayDecorator var2 = new OverlayDecorator(var1, EnterpriseAdminResources.getString("User.Upload.Select.Title")) {
            @Override
            protected void onHidden() {
               OverlayEditor.this.selector.dispose();
            }

            @Override
            protected void onOk() {
               OverlayEditor.this.receiver.handle(OverlayEditor.this.selector.getDestinationServer());
            }

            @Override
            protected void onCancel() {
               OverlayEditor.this.receiver.handle(null);
            }
         };
         var2.setFadeIn(AnimationPreference.get());
         var2.useOkAndCancel();
         var2.setBlocking(true);
         return var2;
      }

      private void updateOkState() {
         this.overlay.getOkAction().setEnabled(this.selector.isInputValid());
      }
   }
}
