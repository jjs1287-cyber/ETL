package org.etlforge.application.ui.view.shell;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.view.ViewManager;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.wait.WaitLock;

public class ViewSwitchAction extends AbstractAction {
   private final ApplicationWindow appWin;
   private final String viewId;

   protected ViewSwitchAction(ApplicationWindow var1, ViewShell var2) {
      super(var2.getName());
      this.appWin = var1;
      this.viewId = var2.getID();
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      final WaitLock var2 = this.appWin.startWait(200);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewSwitchAction.this.switchView(var2);
         }
      });
   }

   private void switchView(WaitLock var1) {
      try {
         ViewManager var2 = this.appWin.getViewManager();
         boolean var9 = !var2.hasView(this.viewId);
         var2.showView(this.viewId);
         this.onViewLoaded(var9);
      } catch (Exception var7) {
         String var3 = ApplicationResources.format("ViewSwitchAction.Error", this.viewId);
         ErrorLog.attention(this.getClass(), var3, var7);
      } finally {
         var1.release();
      }
   }

   protected final ApplicationWindow getWindow() {
      return this.appWin;
   }

   protected void onViewLoaded(boolean var1) {
   }
}
