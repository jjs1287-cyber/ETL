package org.etlforge.integration.activity.ui.queue;

import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueRequest;

public final class OperationQueueController {
   private final OperationQueueModel model;
   private final OperationQueueProvider queueApi;

   public OperationQueueController(OperationQueueModel var1, OperationQueueProvider var2) {
      this.model = var1;
      this.queueApi = var2;
   }

   public void downloadQueue() {
      this.model.setTalkingToServer(true);
      OperationQueueRequest var1 = this.createRequest();
      GetQueueJob var2 = new GetQueueJob(this.queueApi, this.model, var1);
      var2.submit();
   }

   private OperationQueueRequest createRequest() {
      OperationQueueRequest var1 = new OperationQueueRequest();
      var1.subjectId = this.model.getSubjectId();
      var1.nameFilter = this.model.getNameFilter();
      var1.maxNumberOfEntriesFilter = this.model.getMaxEntriesFilter();
      var1.statusFilter = this.model.getStatusFilter();
      return var1;
   }

   public void deleteSelectedEntries() {
      this.model.setTalkingToServer(true);
      DeleteQueueJob var1 = new DeleteQueueJob(this.queueApi, this.model, this);
      var1.submit();
   }
}
