package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;

public final class BulkLoadSessionCredentials {
   public final BulkLoadSessionId sessionId;
   public final String userName;
   public final String md5Password;

   public BulkLoadSessionCredentials(BulkLoadSessionId var1, String var2, String var3) {
      this.sessionId = Preconditions.checkNotNull(var1, "sessionId");
      this.userName = Preconditions.checkNotNull(var2, "userName");
      this.md5Password = Preconditions.checkNotNull(var3, "md5Password");
   }
}
