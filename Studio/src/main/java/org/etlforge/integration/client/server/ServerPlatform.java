package org.etlforge.integration.client.server;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public final class ServerPlatform implements Serializable {
   private static final long serialVersionUID = -6092097172014090641L;
   public static final ServerPlatform UNKNOWN = new ServerPlatform("[unknown]");
   private final String os;
   private final String systemInfo;

   public static ServerPlatform fromOS(String var0) {
      return StringUtils.isBlank(var0) ? UNKNOWN : new ServerPlatform(var0.trim());
   }

   public static ServerPlatform fromOSAndSystemInfo(String var0, String var1) {
      String var2 = StringUtils.isBlank(var0) ? UNKNOWN.os : var0.trim();
      return new ServerPlatform(var2, var1);
   }

   private ServerPlatform(String var1) {
      this(var1, "");
   }

   private ServerPlatform(String var1, String var2) {
      this.os = var1;
      this.systemInfo = var2;
   }

   public String getOS() {
      return this.os;
   }

   public String getSystemInfo() {
      return this.systemInfo;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ServerPlatform)) {
         return false;
      }

      ServerPlatform var2 = (ServerPlatform)var1;
      return this.os.equals(var2.os) && this.systemInfo.equals(var2.systemInfo);
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.os.hashCode();
      return 31 * var1 + this.systemInfo.hashCode();
   }

   @Override
   public String toString() {
      return "[OS = " + this.os + "]";
   }
}
