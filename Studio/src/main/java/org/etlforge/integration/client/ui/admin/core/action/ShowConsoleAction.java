package org.etlforge.integration.client.ui.admin.core.action;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.ui.wait.WaitLock;

public class ShowConsoleAction extends AbstractApplicationAction {
   private final ApplicationWindow appWin;
   private String editorId;
   private final Supplier<Editor> editorFactory;
   private ServerConnectionService connectionService;

   public ShowConsoleAction(String var1, ApplicationWindow var2, Supplier<Editor> var3) {
      this(var1, var2, AdminActionDictionary.DICTIONARY, var3);
   }

   public ShowConsoleAction(String var1, ApplicationWindow var2, ActionDictionary var3, Supplier<Editor> var4) {
      super(var1, var3);
      this.appWin = var2;
      this.editorFactory = var4;
   }

   public final void setConnectionService(ServerConnectionService var1) {
      this.connectionService = var1;
   }

   @Override
   public final void execute(ActionEvent var1) {
      final WaitLock var2 = this.appWin.startWait();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ShowConsoleAction.this.showConsole(var2);
         }
      });
   }

   private void showConsole(WaitLock var1) {
      try {
         Editor var2 = this.findOpenEditor();
         if (var2 == null) {
            var2 = this.editorFactory.get();
            this.prepareEditor(var2);
            this.editorId = var2.getId();
         }

         this.appWin.getEditorService().openEditor(var2, "Administration");
      } finally {
         var1.release();
      }
   }

   private Editor findOpenEditor() {
      return this.editorId != null ? this.appWin.getEditorService().getEditor(this.editorId) : null;
   }

   private void prepareEditor(Editor var1) {
      if (var1 instanceof AbstractConsolePage && this.connectionService != null) {
         ((AbstractConsolePage)var1).setServerConnectionService(this.connectionService);
      }
   }
}
