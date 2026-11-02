package org.etlforge.integration.client.ui.admin.util;

import org.etlforge.integration.activity.ActivitySubject;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.ui.component.page.ActivityPage;
import org.etlforge.integration.activity.ui.queue.OperationQueuePage;

public final class SystemQueueDisplayer extends SystemActivityDisplayer {
   private final OperationQueueProvider queueApi;

   public SystemQueueDisplayer(OperationQueueProvider var1) {
      this.queueApi = var1;
   }

   @Override
   protected ActivityPage createPage(ActivitySubject var1) {
      return new OperationQueuePage(var1, this.queueApi);
   }
}
