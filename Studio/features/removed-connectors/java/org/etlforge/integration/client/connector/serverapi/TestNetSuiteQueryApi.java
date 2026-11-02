package org.etlforge.integration.client.connector.serverapi;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchResponse;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;

public interface TestNetSuiteQueryApi extends ServerCall {
   void testQuery(NetSuiteEndpoint var1, ConnectorObject var2, String var3, TestNetSuiteQueryCallback var4);

   final class Result {
      private final int status;
      private final String errorMessage;
      @Nullable
      private final NetSuiteSearchResponse searchResponse;

      public static TestNetSuiteQueryApi.Result success(NetSuiteSearchResponse var0) {
         if (var0.getStatus()) {
            return new TestNetSuiteQueryApi.Result(0, null, var0);
         }

         String var1 = String.format("%s [%s]", var0.getStatusMessage(), var0.getStatusCode());
         return failure(-1, var1);
      }

      public static TestNetSuiteQueryApi.Result failure(int var0, String var1) {
         Preconditions.checkArgument(var0 != 0, "status cannot be 0 (zero) for a failure.");
         Preconditions.checkNotNull(var1);
         return new TestNetSuiteQueryApi.Result(var0, var1, null);
      }

      private Result(int var1, String var2, @Nullable NetSuiteSearchResponse var3) {
         this.status = var1;
         this.errorMessage = var2;
         this.searchResponse = var3;
      }

      public boolean isSuccess() {
         return this.status == 0;
      }

      public NetSuiteSearchResponse getResponse() {
         Preconditions.checkState(this.searchResponse != null, "This method should not be called unless isSucess() returns true.");
         return this.searchResponse;
      }

      public int getStatus() {
         return this.status;
      }

      public String getErrorMessage() {
         return this.errorMessage;
      }

      @Override
      public String toString() {
         return this.searchResponse != null ? this.searchResponse.toString() : String.format("%s [%d]", this.errorMessage, this.status);
      }
   }
}
