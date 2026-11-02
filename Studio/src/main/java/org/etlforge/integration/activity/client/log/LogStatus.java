package org.etlforge.integration.activity.client.log;

import java.util.EnumSet;

public enum LogStatus {
   SUCCESS("Success", 3),
   SUCCESS_WITH_INFO("Success With Info", 4),
   SUCCESS_WITH_WARNING("Success With Warning", 5),
   ERROR("Error", 6),
   CANCELED("Canceled", 1),
   PENDING("Pending", 0),
   RUNNING("Running", 2);

   private final String displayName;
   private final int serverId;

   LogStatus(String var3, int var4) {
      this.displayName = var3;
      this.serverId = var4;
   }

   @Override
   public String toString() {
      return this.displayName;
   }

   public int getServerId() {
      return this.serverId;
   }

   public static LogStatus fromServerId(int var0) {
      for (LogStatus var4 : values()) {
         if (var4.serverId == var0) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Unknown LogStatus server ID: " + var0);
   }

   public static EnumSet<LogStatus> completed() {
      return EnumSet.complementOf(stillInQueue());
   }

   public static EnumSet<LogStatus> stillInQueue() {
      return EnumSet.of(PENDING, RUNNING);
   }

   public boolean isDone() {
      return this != PENDING && this != RUNNING;
   }

   public boolean isSuccess() {
      return this == SUCCESS || this == SUCCESS_WITH_INFO || this == SUCCESS_WITH_WARNING;
   }
}
