package org.etlforge.integration.server.engine.json;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;

public final class JsonSessionCredentials {
   private final JsonSessionId sessionId;
   private final String userName;
   private final String md5Pwd;

   public JsonSessionCredentials(JsonSessionId var1, String var2, String var3) {
      Preconditions.checkNotNull(var1, "sessionId");
      ArgCheck.checkNotEmpty(var2, "userName");
      ArgCheck.checkNotEmpty(var3, "md5Pwd");
      this.sessionId = var1;
      this.userName = var2;
      this.md5Pwd = var3;
   }

   public JsonSessionId getSessionId() {
      return this.sessionId;
   }

   public String getUserName() {
      return this.userName;
   }

   public String getMd5Pwd() {
      return this.md5Pwd;
   }
}
