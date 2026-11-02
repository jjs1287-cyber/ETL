package org.etlforge.integration.client.server.download;

import com.google.common.base.Strings;

public final class Credentials {
   public final String user;
   public final String password;

   public Credentials(String var1, String var2) {
      this.user = Strings.nullToEmpty(var1);
      this.password = Strings.nullToEmpty(var2);
   }
}
