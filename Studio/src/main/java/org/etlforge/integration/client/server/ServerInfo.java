package org.etlforge.integration.client.server;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.util.version.Version;

public final class ServerInfo implements Serializable {
   private static final long serialVersionUID = -2370442892472604882L;
   private final String serverName;
   private final ServerGuid serverGuid;
   private final Version version;
   private final ServerPlatform platform;

   public ServerInfo(ServerGuid var1, String var2) {
      this(var1, var2, null);
   }

   public ServerInfo(ServerGuid var1, String var2, Version var3) {
      this(var1, var2, var3, ServerPlatform.UNKNOWN);
   }

   public ServerInfo(ServerGuid var1, String var2, Version var3, ServerPlatform var4) {
      Preconditions.checkNotNull(var1, "guid");
      this.serverGuid = var1;
      this.serverName = var2;
      this.version = var3;
      this.platform = var4;
   }

   public String getName() {
      return this.serverName;
   }

   public ServerGuid getGuid() {
      return this.serverGuid;
   }

   public Version getVersion() {
      return this.version;
   }

   public ServerPlatform getPlatform() {
      return this.platform;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("[ServerInfo - GUID: ");
      var1.append(this.serverGuid);
      var1.append(", Name: ");
      var1.append(this.serverName);
      var1.append(", Version: ");
      var1.append(this.version == null ? "?" : this.version.toString());
      var1.append(", Platform: ").append(this.platform);
      String var2 = this.platform.getSystemInfo();
      if (!var2.isEmpty()) {
         var1.append(var2);
      }

      var1.append("]");
      return var1.toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ServerInfo ? this.equals((ServerInfo)var1) : false;
      }
   }

   private boolean equals(ServerInfo var1) {
      return this.serverGuid.equals(var1.serverGuid)
         && equals(this.serverName, var1.serverName)
         && equals(this.version, var1.version)
         && equals(this.platform, var1.platform);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.serverGuid.hashCode();
      var1 = 31 * var1 + (this.serverName != null ? this.serverName.hashCode() : 0);
      var1 = 31 * var1 + (this.version != null ? this.version.hashCode() : 0);
      return 31 * var1 + (this.platform != null ? this.platform.hashCode() : 0);
   }

   public static boolean representsTheSameServer(ServerInfo var0, ServerInfo var1) {
      return var0 == null ? var1 == null : var1 != null && var0.serverGuid.equals(var1.serverGuid);
   }

   private static boolean equals(Object var0, Object var1) {
      return var0 != null ? var0.equals(var1) : var1 == null;
   }
}
