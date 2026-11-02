package org.etlforge.integration.client.ui.admin.core.console;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.annotation.Nullable;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.DefaultEditorController;
import org.etlforge.application.ui.window.editor.EditorController;
import org.etlforge.application.ui.window.editor.EditorHistoryPoint;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.integration.client.server.ConnectionStatus;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ui.ServerConnectionListenerResource;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;
import org.etlforge.integration.user.User;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public abstract class AbstractConsolePage extends GenericEditor {
   private EditorHistoryPoint historyPoint;
   private ServerConnectionService connectionService;
   private final Object connectionLock = new Object();
   private ServerInfo currentServer;
   private User currentUser;

   protected AbstractConsolePage(String var1, String var2) {
      super(var1, var2);
      EditorController var3 = this.createController();
      this.setController(var3);
   }

   private final EditorController createController() {
      DefaultEditorController var1 = new DefaultEditorController();
      var1.setPopupMenuRequestHandler(new PopupMenuRequestHandler() {
         @Override
         public boolean popupMenuRequested(Component var1, MouseEvent var2) {
            KongaPopupMenu var3 = new KongaPopupMenu();
            AbstractConsolePage.this.populatePopupMenu(var3);
            var3.show(AbstractConsolePage.this.getMainPanel(), var2.getX(), var2.getY());
            return false;
         }
      });
      return var1;
   }

   protected void populatePopupMenu(KongaPopupMenu var1) {
   }

   public final void setServerConnectionService(ServerConnectionService var1) {
      this.connectionService = Preconditions.checkNotNull(var1, "service");
      this.addResource(new ServerConnectionListenerResource(var1, new AbstractConsolePage.ConnectionChangeHandler()));
      synchronized (this.connectionLock) {
         this.currentServer = this.connectionService.getCurrentServerInfo();
         this.currentUser = this.connectionService.getCurrentUser();
      }
   }

   @Override
   public void addToWindow(ApplicationWindow var1) {
      super.addToWindow(var1);
      this.setSubTitle();
   }

   @Override
   public final EditorHistoryPoint getHistoryPoint() {
      return this.historyPoint;
   }

   public final void setHistoryPoint(EditorHistoryPoint var1) {
      this.historyPoint = var1;
   }

   private void setSubTitle() {
      String var1 = "";
      if (this.connectionService != null) {
         var1 = this.isConnected() ? this.connectionService.getCurrentServerName() : AdminViewResources.getString("AbstractConsolePage.Disconnected");
      }

      this.setSubTitle(var1);
   }

   protected final boolean isConnected() {
      return this.connectionService.getConnectionStatus() == ConnectionStatus.CONNECTED;
   }

   @Nullable
   protected final ServerInfo getCurrentServer() {
      return this.currentServer;
   }

   protected void serverConnectionChanged(ServerConnectionEvent var1) {
   }

   protected final boolean isLoggedInAsAdmin() {
      synchronized (this.connectionLock) {
         return this.currentUser != null && this.currentUser.isAdmin();
      }
   }

   private class ConnectionChangeHandler implements ServerConnectionListener {
      private ConnectionChangeHandler() {
      }

      @Override
      public void serverConnectionChanged(final ServerConnectionEvent var1) {
         synchronized (AbstractConsolePage.this.connectionLock) {
            AbstractConsolePage.this.currentServer = var1.getServerInfo();
            AbstractConsolePage.this.currentUser = var1.getUser();
         }

         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               AbstractConsolePage.this.setSubTitle(var1.getServerName());
               AbstractConsolePage.this.serverConnectionChanged(var1);
            }
         });
      }
   }
}
