package org.etlforge.integration.client.ui.admin.core.action;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.proxy.serverapi.ProxySettingsApi;
import com.etlforge.integration.client.proxy.ui.ProxySettingsEditor;
import com.etlforge.integration.client.proxy.ui.ProxySettingsEditorController;
import com.etlforge.integration.client.proxy.ui.ProxySettingsEditorModel;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.component.proxy.ServerProxyConsolePage;

public class ShowServerProxyConsoleAction extends ShowConsoleAction {
   public ShowServerProxyConsoleAction(ApplicationWindow var1, ServerConnectionService var2, ProxySettingsApi var3) {
      super("admin.action.ServerProxyConsole", var1, new ShowServerProxyConsoleAction.EditorFactory(var2, var3));
      this.setConnectionService(var2);
   }

   private static class EditorFactory extends AbstractConsoleFactory {
      private final ServerConnectionService connectionService;
      private final ProxySettingsApi serverApi;

      public EditorFactory(ServerConnectionService var1, ProxySettingsApi var2) {
         this.connectionService = Preconditions.checkNotNull(var1, "connectionService");
         this.serverApi = Preconditions.checkNotNull(var2, "serverApi");
      }

      @Override
      protected Editor createPage() {
         ProxySettingsEditorModel var1 = new ProxySettingsEditorModel();
         ProxySettingsEditorController var2 = new ProxySettingsEditorController(var1, this.serverApi);
         ProxySettingsEditor var3 = new ProxySettingsEditor(var1, var2);
         return new ServerProxyConsolePage(var3);
      }

      @Override
      protected void configurePageWhenRestoredFromHistoryPoint(Editor var1) {
         ((ServerProxyConsolePage)var1).setServerConnectionService(this.connectionService);
      }
   }
}
