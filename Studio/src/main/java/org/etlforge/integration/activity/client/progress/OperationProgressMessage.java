package org.etlforge.integration.activity.client.progress;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.system.ArgCheck;

public final class OperationProgressMessage {
   private final OperationId opGuid;
   private final long opInstanceId;
   private final ProgressSnapshot snapshot;

   public OperationProgressMessage(OperationId var1, long var2, ProgressSnapshot var4) {
      Preconditions.checkNotNull(var1, "opGuid");
      ArgCheck.checkPositive(var2, "opInstanceId");
      Preconditions.checkNotNull(var4, "snapshot");
      this.opGuid = var1;
      this.opInstanceId = var2;
      this.snapshot = var4;
   }

   public OperationId getOpGuid() {
      return this.opGuid;
   }

   public long getOpInstanceId() {
      return this.opInstanceId;
   }

   public ProgressSnapshot getSnapshot() {
      return this.snapshot;
   }
}
