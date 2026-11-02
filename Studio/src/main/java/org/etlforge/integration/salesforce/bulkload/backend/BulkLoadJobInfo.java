package org.etlforge.integration.salesforce.bulkload.backend;

import java.nio.charset.Charset;

public class BulkLoadJobInfo {
   private final BulkLoadJobInfo.BulkLoadJobInfoEnum statusEnum;
   private String JobId;
   private String sourcefilename;
   private Charset sourcefilecharencode;
   private int operationid;
   private int bulkloadid;
   private String header;
   private String salesforceAction;

   private void init(String var1, String var2, Charset var3) {
      this.JobId = var1;
      this.sourcefilename = var2;
      this.sourcefilecharencode = var3;
   }

   public BulkLoadJobInfo(String var1, String var2) {
      this.init(var1, null, null);
      this.statusEnum = BulkLoadJobInfo.BulkLoadJobInfoEnum.ConvertToEnum(var2);
   }

   public BulkLoadJobInfo(String var1, int var2, String var3, Charset var4) {
      this.init(var1, var3, var4);
      this.statusEnum = BulkLoadJobInfo.BulkLoadJobInfoEnum.ConvertToEnum(var2);
   }

   public String getSourceFilename() {
      return this.sourcefilename;
   }

   public Charset getSourceFileCharEncode() {
      return this.sourcefilecharencode;
   }

   public boolean isDone() {
      return BulkLoadJobInfo.BulkLoadJobInfoEnum.isDone(this.statusEnum);
   }

   public boolean isOpen() {
      return this.statusEnum == BulkLoadJobInfo.BulkLoadJobInfoEnum.OPEN;
   }

   public boolean isClosed() {
      return this.statusEnum == BulkLoadJobInfo.BulkLoadJobInfoEnum.CLOSED;
   }

   public String getJobId() {
      return this.JobId;
   }

   public void setOperationid(int var1) {
      this.operationid = var1;
   }

   public int getOperationid() {
      return this.operationid;
   }

   public void setHeader(String var1) {
      this.header = var1;
   }

   public String getHeader() {
      return this.header;
   }

   public void setSalesforceAction(String var1) {
      this.salesforceAction = var1;
   }

   public String getSalesforceAction() {
      return this.salesforceAction;
   }

   public int getBulkloadid() {
      return this.bulkloadid;
   }

   public void setBulkloadid(int var1) {
      this.bulkloadid = var1;
   }

   public enum BulkLoadJobInfoEnum {
      OPEN,
      CLOSED,
      COMPLETED,
      FAILED,
      ABORTED;

      public static int ConvertToInt(BulkLoadJobInfo.BulkLoadJobInfoEnum var0) {
         return var0.ordinal();
      }

      public static BulkLoadJobInfo.BulkLoadJobInfoEnum ConvertToEnum(int var0) {
         return values()[var0];
      }

      public static BulkLoadJobInfo.BulkLoadJobInfoEnum ConvertToEnum(String var0) {
         if (var0.equalsIgnoreCase("Closed")) {
            return CLOSED;
         } else if (var0.equalsIgnoreCase("Completed")) {
            return COMPLETED;
         } else if (var0.equalsIgnoreCase("Failed")) {
            return FAILED;
         } else if (var0.equalsIgnoreCase("Aborted")) {
            return ABORTED;
         } else {
            return var0.equalsIgnoreCase("Open") ? OPEN : OPEN;
         }
      }

      public static boolean isDone(BulkLoadJobInfo.BulkLoadJobInfoEnum var0) {
         return var0 != OPEN && var0 != CLOSED;
      }
   }
}
