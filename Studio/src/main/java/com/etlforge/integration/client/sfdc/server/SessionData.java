package com.etlforge.integration.client.sfdc.server;

import org.etlforge.system.ArgCheck;

public final class SessionData {
   private final String sessionId;
   private final String userId;
   private final String endpoint;
   private final int sessionTimeoutInSeconds;

   public SessionData(String var1, String var2, String var3, int var4) {
      this.sessionId = ArgCheck.checkNotBlank(var1, "sessionId");
      this.userId = ArgCheck.checkNotBlank(var2, "userId");
      this.endpoint = ArgCheck.checkNotBlank(var3, "endpoint");
      this.sessionTimeoutInSeconds = var4;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public String getUserId() {
      return this.userId;
   }

   public String getEndpoint() {
      return this.endpoint;
   }

   public int getSessionTimeoutInSeconds() {
      return this.sessionTimeoutInSeconds;
   }
}
