package org.etlforge.integration.client.ui.admin.component.user2;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.user.UpdateUserManagerCallback;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

class DownloadAction extends AbstractAction {
   private final UserConsoleModel model;
   private final UserAdmin serverApi;

   public DownloadAction(UserConsoleModel var1, UserAdmin var2) {
      super(Strings.get("User.Download.Name"), ClientIcons.getIcon("User.Download.16"));
      this.model = var1;
      this.serverApi = var2;
      this.putValue("SwingLargeIconKey", ClientIcons.getIcon("User.Download.32"));
      this.putValue("ShortDescription", Strings.get("User.Download.ToolTip"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.setDownloadingFlag(true);
      this.model.getUserManager().updateUsersAndGroups(this.serverApi, true, new DownloadAction.Callback());
   }

   private void setDownloadingFlag(final boolean var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DownloadAction.this.setEnabled(!var1);
            DownloadAction.this.model.setDownloadingInfo(var1);
         }
      });
   }

   private class Callback implements UpdateUserManagerCallback {
      private Callback() {
      }

      @Override
      public void updateCompleted(final ServerInfo var1, final List<User> var2, final List<Group> var3) {
         this.callReturned();
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               DownloadAction.this.model.setCurrentServer(var1);
               DownloadAction.this.model.setNewUserData(var2, var3, var1.getGuid());
            }
         });
      }

      @Override
      public void cancelled() {
         this.callReturned();
      }

      @Override
      public void failed(Throwable var1) {
         this.callReturned();
         ErrorLog.attention(DownloadAction.class, Strings.get("User.Download.Failed"), var1);
      }

      private void callReturned() {
         DownloadAction.this.setDownloadingFlag(false);
      }
   }
}
