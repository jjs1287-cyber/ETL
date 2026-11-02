package org.etlforge.integration.salesforce.bulkload.backend;

public enum BatchStatus {
   QUEUED,
   INPROGRESS,
   COMPLETED,
   FAILED,
   NOTPROCESSED;

   public static int ConvertToInt(BatchStatus var0) {
      return var0.ordinal();
   }

   public static BatchStatus ConvertToEnum(int var0) {
      return values()[var0];
   }

   public static BatchStatus ConvertToEnum(String var0) {
      if (var0.equalsIgnoreCase("Queued")) {
         return QUEUED;
      } else if (var0.equalsIgnoreCase("InProgress")) {
         return INPROGRESS;
      } else if (var0.equalsIgnoreCase("Completed")) {
         return COMPLETED;
      } else if (var0.equalsIgnoreCase("Failed")) {
         return FAILED;
      } else {
         return var0.equalsIgnoreCase("Not Processed") ? NOTPROCESSED : NOTPROCESSED;
      }
   }
}
