package org.etlforge.integration.activity.client;

import org.etlforge.integration.data.entity.id.OperationId;

public abstract class AbstractActivityEntry implements ActivityEntry {
   private final long id;
   private final OperationId opId;
   private final String opName;

   public AbstractActivityEntry(long var1, OperationId var3, String var4) {
      this.id = var1;
      this.opId = var3;
      this.opName = var4;
   }

   @Override
   public final long getID() {
      return this.id;
   }

   public final OperationId getOperationId() {
      return this.opId;
   }

   public final String getOperationName() {
      return this.opName;
   }
}
