package com.etlforge.integration.server.engine.bulkload.batch;

import java.net.HttpURLConnection;

final class DebugRequestHeaders {
   private final int sleepTime;

   public DebugRequestHeaders(int var1) {
      this.sleepTime = var1;
   }

   public void addToRequest(HttpURLConnection var1, int var2, int var3) {
      String var4 = String.format("%02d_%04d", var2, ++var3);
      var1.addRequestProperty("X-batchid", var4);
      if (this.sleepTime > 0) {
         var1.addRequestProperty("X-sleep", String.valueOf(this.sleepTime));
      }
   }
}
