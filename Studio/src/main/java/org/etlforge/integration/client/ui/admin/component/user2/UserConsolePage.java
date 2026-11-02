package org.etlforge.integration.client.ui.admin.component.user2;

import java.awt.EventQueue;
import java.awt.Window;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.application.ui.DoNotShowAgainMessage;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.client.ui.admin.prefs.UploadUsersAndGroupsSuccessMessagePreference;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.system.callback.CallbackResultAdapter;
import org.etlforge.ui.event.ApplicationMessageEvent;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;

public final class UserConsolePage extends AbstractConsolePage {
   private final UserConsoleModel model;
   private final UserConsoleView view;
   private final UserConsoleController controller;
   private final ReadOnlyNotice readOnlyNotice;
   private final UnsavedDataProtector unsavedDataProtector;

   public UserConsolePage(UserConsoleView var1, UserConsoleController var2, LoginService var3) {
      super("Admin.Console.User", getNormalTitle());
      this.view = var1;
      this.model = var1.getModel();
      this.configureView();
      this.model.addModelListener(new UserConsolePage.ModelListener());
      this.controller = var2;
      var2.setView(var1);
      var2.setSuccessfulSaveCallback(new UserConsolePage.SaveCallback());
      this.readOnlyNotice = new ReadOnlyNotice(this.model, this.getOverlayContainer());
      this.unsavedDataProtector = new UnsavedDataProtector(this, var3, this.model);
      this.setIcon(ClientIcons.USER_16);
      this.setHelpId("administration.consoles.users");
      this.setMementoPath(new String[]{"Admin", "UserConsole"});
      this.useDefaultCategory();
      this.prepareActions();
      this.addResource(new DisposableWindowElementResource(this.model, var1, var2, this.unsavedDataProtector));
   }

   private void configureView() {
      this.view.setDetailsViewer(new OverlayDetailsViewer(this.model, this.getOverlayContainer()));
      this.view.addChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            UserConsolePage.this.setDirty();
         }
      });
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.controller.activate();
      this.layoutPage();
   }

   @Override
   public void save() throws SaveEditorException {
      try {
         if (this.model.isReadOnly()) {
            this.readOnlyNotice.showNotSaveableMessage();
         } else {
            this.controller.save();
         }
      } catch (InvalidUserDataException var2) {
         throw new SaveEditorException(this, "Failed to save the users & groups on the server: " + var2.getMessage(), var2);
      }
   }

   @Override
   public void setDirty() {
      this.setTitle(getNormalTitle() + "*");
      super.setDirty();
   }

   @Override
   public void setClean() {
      this.setTitle(getNormalTitle());
      super.setClean();
   }

   @Override
   public void setSubTitle(String var1) {
      String var2 = var1;
      if (this.model.isReadOnly()) {
         var2 = var2 + " [read-only]";
      }

      super.setSubTitle(var2);
   }

   private void layoutPage() {
      this.createTitlePanel();
      this.setToolBar(this.createToolBar());
      this.setMainPanel(BorderLayoutBuilder.fillWith(this.view));
   }

   private void prepareActions() {
      for (Action var4 : this.controller.getServerActions()) {
         this.prepareAction(var4);
      }

      for (Action var8 : this.controller.getModelActions()) {
         this.prepareAction(var8);
      }
   }

   private void prepareAction(Action var1) {
      if (var1 instanceof UserConsoleAction) {
         ((UserConsoleAction)var1).setOverlayContainer(this.getOverlayContainer());
      }
   }

   private static String getNormalTitle() {
      return Strings.get("User.ConsolePage.Title");
   }

   private class ModelListener extends UserConsoleModelListener.Adapter {
      private JComponent blockingOverlay;

      private ModelListener() {
      }

      @Override
      public void dirtyStatusChanged(UserConsoleModel var1, boolean var2) {
         if (var2) {
            UserConsolePage.this.setDirty();
         } else {
            UserConsolePage.this.setClean();
         }
      }

      @Override
      public void readOnlyStateChanged(UserConsoleModel var1, boolean var2) {
         if (var2) {
            UserConsolePage.this.readOnlyNotice.showReadOnlyMessage();
         }

         UserConsolePage.this.setSubTitle(var1.getCurrentServer().getName());
      }

      @Override
      public void uploadStatusChanged(UserConsoleModel var1, boolean var2) {
         if (var2) {
            if (this.blockingOverlay == null) {
               this.blockingOverlay = new JLabel(" ");
               this.blockingOverlay.putClientProperty("konga.overlay.blocking", Boolean.TRUE);
               UserConsolePage.this.getOverlayContainer().addOverlay(this.blockingOverlay, new OverlayCenterPositioner());
            }
         } else {
            UserConsolePage.this.getOverlayContainer().removeOverlay(this.blockingOverlay);
            this.blockingOverlay = null;
         }
      }
   }

   private class SaveCallback extends CallbackResultAdapter {
      private SaveCallback() {
      }

      @Override
      public void succeeded(Object var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SaveCallback.this.showSuccessMessage();
            }
         });
      }

      private void showSuccessMessage() {
         BooleanPreference var1 = UploadUsersAndGroupsSuccessMessagePreference.INSTANCE;
         String var2 = Strings.get("User.Save.Success.Message");
         this.postStatusMessage(var2);
         String var3 = Strings.get("User.Save.Success.Title");
         DoNotShowAgainMessage var4 = new DoNotShowAgainMessage(var2, var3, var1);
         boolean var5 = false;
         if (UserConsolePage.this.getState().contains(WindowElementState.OPEN)) {
            var5 = var4.showAsOverlay(UserConsolePage.this.getOverlayContainer());
         } else {
            var5 = var4.showInDialog(this.getDialogOwner());
         }

         if (var5) {
            ApplicationMessageEvent.postInformationMessage(UploadJob.class, "");
         }
      }

      private Window getDialogOwner() {
         ApplicationWindow var1 = UserConsolePage.this.getWindow();
         return var1 != null ? var1.getFrame() : UiUtils.getActiveFrame();
      }

      private void postStatusMessage(String var1) {
         ApplicationMessageEvent.postInformationMessage(UploadJob.class, var1, new ApplicationMessageEvent.LifeTime(5000L));
      }
   }
}
