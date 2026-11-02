package org.etlforge.integration.server.engine.jdbc;

import com.google.common.base.Preconditions;

public final class JdbcSessionCredentials {
   public final JdbcSessionId sessionId;
   public final String userName;
   public final String md5Password;

   public JdbcSessionCredentials(JdbcSessionId var1, String var2, String var3) {
      Preconditions.checkNotNull(var1, "sessionId");
      Preconditions.checkNotNull(var2, "userName");
      Preconditions.checkNotNull(var3, "md5Password");
      this.sessionId = var1;
      this.userName = var2;
      this.md5Password = var3;
   }
}
