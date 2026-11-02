package org.etlforge.integration.activity.ui.log;

import java.util.Date;
import org.etlforge.integration.activity.client.filter.TimeWindowFilter;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.serverapi.log.OperationLogRequest;
import org.etlforge.util.SortOrder;

public final class OperationLogController {
   private final OperationLogModel model;
   private final OperationLogProvider logApi;

   public OperationLogController(OperationLogModel var1, OperationLogProvider var2) {
      this.model = var1;
      this.logApi = var2;
   }

   public void getLogEntries() {
      OperationLogRequest var1 = this.createRequest();
      this.startJob(var1);
   }

   private OperationLogRequest createRequest() {
      OperationLogRequest var1 = new OperationLogRequest();
      var1.subjectId = this.model.getSubjectId();
      var1.nameFilter = this.model.getNameFilter();
      var1.maxNumberOfEntriesFilter = this.model.getMaxEntriesFilter();
      var1.timeFilter = this.model.getTimeFilter();
      var1.statusFilter = this.model.getStatusFilter();
      var1.messageFilter = this.model.getMessageFilter();
      var1.sortOrder = SortOrder.DESCENDING;
      return var1;
   }

   private void startJob(OperationLogRequest var1) {
      GetLogJob var2 = new GetLogJob(this.model, this.logApi, var1);
      this.model.setTalkingToServer(true);
      var2.submit();
   }

   public void getNewLogEntries() {
      Date var1 = this.model.getTimeOfLastEntry();
      if (var1 == null) {
         this.getLogEntries();
      } else {
         OperationLogRequest var2 = this.createRequest();
         var2.timeFilter = new TimeWindowFilter(this.model.getTimeOfLastEntry(), null);
         this.startJob(var2);
      }
   }
}
