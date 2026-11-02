package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;
import org.etlforge.integration.salesforce.SalesforceVersion;
import org.etlforge.system.ArgCheck;

public final class DefaultBulkLoadEndpoint implements BulkLoadEndpoint {
   private static final int HTTPPREFIXLENGTH = 8;
   private final String serverUrl;
   private final SalesforceVersion version;
   private final String baseUrl;
   private final String sessionId;

   public DefaultBulkLoadEndpoint(String var1, String var2) {
      this(var1, var2, SalesforceVersion.DEFAULT_VERSION);
   }

   public DefaultBulkLoadEndpoint(String var1, String var2, SalesforceVersion var3) {
      this.serverUrl = ArgCheck.checkNotEmpty(var1, "serverUrl");
      this.sessionId = ArgCheck.checkNotEmpty(var2, "sessionId");
      this.version = Preconditions.checkNotNull(var3);
      int var4 = var1.indexOf(47, 8);
      if (var4 < 0) {
         var4 = var1.indexOf(92, 8);
      }

      String var5 = var4 > 0 ? var1.substring(0, var4) : var1;
      this.baseUrl = var5 + "/services/async/" + var3.getId() + "/job";
   }

   @Override
   public String getSessionId() {
      return this.sessionId;
   }

   @Override
   public String getCreateJobUrl() {
      return this.baseUrl;
   }

   @Override
   public String getSendBatchUrl(String var1) {
      return this.baseUrl + "/" + var1 + "/batch";
   }

   @Override
   public String getCloseJobUrl(String var1) {
      return this.baseUrl + "/" + var1;
   }

   public DefaultBulkLoadEndpoint forVersion(SalesforceVersion var1) {
      return var1 == this.version ? this : new DefaultBulkLoadEndpoint(this.serverUrl, this.sessionId, var1);
   }
}
