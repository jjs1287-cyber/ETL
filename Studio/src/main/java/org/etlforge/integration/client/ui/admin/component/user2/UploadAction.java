package org.etlforge.integration.client.ui.admin.component.user2;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public class UploadAction extends ActiveModelAction implements UserConsoleAction {
   private final UserConsoleController controller;
   private OverlayContainer overlayContainer;

   public UploadAction(UserConsoleModel var1, UserConsoleController var2) {
      super(var1, Strings.get("User.Upload.Name"), ClientIcons.getIcon("User.Upload.16"));
      this.controller = var2;
      this.putValue("SwingLargeIconKey", ClientIcons.getIcon("User.Upload.32"));
      this.putValue("ShortDescription", Strings.get("User.Upload.ToolTip"));
   }

   @Override
   public void setController(UserConsoleController var1) {
   }

   @Override
   public void setOverlayContainer(OverlayContainer var1) {
      this.overlayContainer = var1;
   }

   @Override
   protected UserConsoleModelListener createListener() {
      return new UploadAction.ModelListener();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.setEnabled(false);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            UploadAction.this.runImpl();
         }
      });
   }

   private void runImpl() {
      try {
         this.disable();
         this.controller.save();
      } catch (InvalidUserDataException var2) {
         new InvalidUserDataAlert(var2, this.overlayContainer).show();
         this.enable();
      }
   }

   private void disable() {
      this.setEnabled(false);
   }

   private void enable() {
      this.setEnabled(true);
   }

   private class ModelListener extends ActiveModelAction.Activator {
      private ModelListener() {
      }

      @Override
      public void uploadStatusChanged(UserConsoleModel var1, boolean var2) {
         UploadAction.this.setEnabled(!var2);
      }
   }
}
