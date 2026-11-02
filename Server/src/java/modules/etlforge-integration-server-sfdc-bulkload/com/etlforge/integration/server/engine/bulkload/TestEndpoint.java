package com.etlforge.integration.server.engine.bulkload;

public final class TestEndpoint implements BulkLoadEndpoint {
   private static final String LOCALHOST = "localhost:46908";
   private final String server;

   public TestEndpoint() {
      this("localhost:46908");
   }

   public TestEndpoint(String var1) {
      this.server = "http://" + (var1.endsWith("/") ? var1 : var1 + "/");
   }

   @Override
   public String getCreateJobUrl() {
      return this.server + "create_job";
   }

   @Override
   public String getSendBatchUrl(String var1) {
      return this.server + "bulk_load_batch";
   }

   @Override
   public String getCloseJobUrl(String var1) {
      return this.server + "close_job";
   }

   @Override
   public String getSessionId() {
      return "abc-123";
   }
}
