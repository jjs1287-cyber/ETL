package org.etlforge.integration.client.server;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Date;

public final class KnownServer {
   private final ServerInfo serverInfo;
   private final Date dateOfFirstLogin;
   private final Date dateOfLatestLogin;
   private final String latestUser;
   private final String password;
   private final boolean useHttps;

   public KnownServer(ServerInfo var1, Date var2, Date var3, String var4, String var5, boolean var6) {
      this.serverInfo = Preconditions.checkNotNull(var1);
      this.dateOfFirstLogin = var2;
      this.dateOfLatestLogin = var3;
      this.latestUser = var4;
      this.password = var5;
      this.useHttps = var6;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   public Date getDateOfFirstLogin() {
      return this.dateOfFirstLogin;
   }

   public Date getDateOfLatestLogin() {
      return this.dateOfLatestLogin;
   }

   public String getLatestUser() {
      return this.latestUser;
   }

   public String getPassword() {
      return this.password;
   }

   public boolean isUsingHttps() {
      return this.useHttps;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("name", this.serverInfo.getName())
         .add("version", this.serverInfo.getVersion())
         .add("guid", this.serverInfo.getGuid())
         .toString();
   }
}
