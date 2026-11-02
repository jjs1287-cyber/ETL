package com.etlforge.integration.server.engine.bulkload.batch;

import com.etlforge.integration.server.engine.bulkload.ErrorLog;
import java.net.HttpURLConnection;
import org.apache.commons.lang.StringUtils;

public final class HttpBatchResponse implements BatchResponse {
   private final int responseCode;
   private final String response;
   private final String batchId;
   public final Exception readError;

   public HttpBatchResponse(HttpURLConnection var1) {
      int var2 = -1;
      String var3 = null;
      String var4 = null;
      Exception var5 = null;

      try {
         var2 = var1.getResponseCode();
         var3 = BatchResponseParser.getResponse(var1);
         var4 = BatchResponseParser.getId(var3);
      } catch (Exception var7) {
         var5 = var7;
      }

      this.responseCode = var2;
      this.response = var3;
      this.batchId = var4;
      this.readError = var5;
   }

   @Override
   public String getBatchId(ErrorLog var1) {
      if (this.batchId == null) {
         this.reportError(var1);
      }

      return this.batchId;
   }

   private void reportError(ErrorLog var1) {
      if (StringUtils.isNotEmpty(this.response)) {
         var1.writeError("Failed to extract the batch ID from the salesforce response. The response was:\n" + this.response, this.readError);
      } else {
         var1.writeError(
            "Failed to send a batch to salesforce. [Response code: " + (this.responseCode == -1 ? "unknown" : this.responseCode) + "]", this.readError
         );
      }
   }

   @Override
   public boolean isClientSideError() {
      return this.responseCode >= 400 && this.responseCode < 500;
   }
}
