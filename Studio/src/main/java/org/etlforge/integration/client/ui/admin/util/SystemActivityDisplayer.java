package org.etlforge.integration.client.ui.admin.util;

import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.ui.EntireSystemSubject;
import org.etlforge.integration.activity.ui.component.page.ActivityPage;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public abstract class SystemActivityDisplayer {
   public final void display(final ApplicationWindow var1, final ServerConnectionService var2) {
      (new BusyWorker(var1) {
         @Override
         protected void doWork() {
            ActivityPage var1x = SystemActivityDisplayer.this.createPage(var2);
            var1x.setSubjectIcon(ClientIcons.SERVER_16);
            var1.getEditorService().openEditor(var1x, "Administration");
            var1x.retrieveRecords();
         }
      }).run();
   }

   protected abstract ActivityPage createPage(ActivitySubject var1);

   private ActivityPage createPage(ServerConnectionService var1) {
      EntireSystemSubject var2 = new EntireSystemSubject();
      var2.setServerConnectionService(var1);
      return this.createPage(var2);
   }
}
