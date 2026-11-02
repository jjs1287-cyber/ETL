package org.etlforge.integration.activity.ui.monitor;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.activity.serverapi.log.GetOperationLogCallback;
import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.serverapi.log.OperationLogRequest;
import org.etlforge.integration.activity.serverapi.progress.OperationProgressProvider;
import org.etlforge.integration.activity.serverapi.queue.DeleteQueueEntriesCallback;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;

public final class OperationMonitorApis {
   private final OperationLogProvider logApi;
   private final OperationQueueProvider queueApi;
   private final Optional<OperationProgressProvider> progressApi;

   public OperationMonitorApis(OperationLogProvider var1, OperationQueueProvider var2) {
      this(var1, var2, null);
   }

   public OperationMonitorApis(OperationLogProvider var1, OperationQueueProvider var2, OperationProgressProvider var3) {
      this.logApi = Preconditions.checkNotNull(var1);
      this.queueApi = Preconditions.checkNotNull(var2);
      this.progressApi = Optional.fromNullable(var3);
   }

   public void getLog(OperationLogRequest var1, GetOperationLogCallback var2) {
      this.logApi.getOperationLog(var1, var2);
   }

   public void cancelOperation(long var1, DeleteQueueEntriesCallback var3) {
      this.queueApi.deleteQueueEntries(new long[]{var1}, var3);
   }

   public boolean isProgressTrackingSupported() {
      return this.progressApi.isPresent();
   }

   public OperationProgressProvider getProgressProvider() {
      return this.isProgressTrackingSupported() ? this.progressApi.get() : null;
   }
}
