package org.etlforge.integration.client.ui.admin.util;

import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.ui.component.page.ActivityPage;
import org.etlforge.integration.activity.ui.log.OperationLogPage;

public final class SystemOperationLogDisplayer extends SystemActivityDisplayer {
   private final OperationLogProvider logApi;

   public SystemOperationLogDisplayer(OperationLogProvider var1) {
      this.logApi = var1;
   }

   @Override
   protected ActivityPage createPage(ActivitySubject var1) {
      return new OperationLogPage(var1, this.logApi);
   }
}
