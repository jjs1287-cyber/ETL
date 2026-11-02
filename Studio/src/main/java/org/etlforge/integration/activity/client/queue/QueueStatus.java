package org.etlforge.integration.activity.client.queue;

import org.etlforge.integration.activity.client.log.LogStatus;

public enum QueueStatus {
   PENDING("Pending"),
   RUNNING("Running");

   private final String displayName;

   QueueStatus(String var3) {
      this.displayName = var3;
   }

   @Override
   public String toString() {
      return this.displayName;
   }

   public static QueueStatus fromId(int var0) {
      switch (var0) {
         case 0:
            return PENDING;
         case 1:
            return RUNNING;
         default:
            throw new IllegalArgumentException("Invalid QueueStatus ID: " + var0);
      }
   }

   public LogStatus toLogStatus() {
      return this == PENDING ? LogStatus.PENDING : LogStatus.RUNNING;
   }
}
