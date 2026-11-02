package org.etlforge.integration.etlforgeconnector;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.net.Port;

public final class ServerParams {
   public static ServerParams dummyServer = new ServerParams("ETLForgeDummyHost");
   private final String host;
   private final Port port;

   public static ServerParams fromAddress(String var0) {
      ArgCheck.checkNotBlank(var0);
      String[] var1 = var0.split(":");
      switch (var1.length) {
         case 1:
            return new ServerParams(var1[0], Port.DEFAULT);
         case 2:
            try {
               int var2 = Integer.parseInt(var1[1]);
               Port var3 = Port.valueOf(var2);
               if (var3 != Port.INVALID) {
                  return new ServerParams(var1[0], var3);
               }
            } catch (NumberFormatException var4) {
            }

            throw new IllegalArgumentException("Invalid port number: " + var1[1]);
         default:
            throw new IllegalArgumentException("Unexpected format: Multiple ':' encountered.");
      }
   }

   public ServerParams(String var1) {
      this(var1, Port.DEFAULT);
   }

   public ServerParams(String var1, int var2) {
      this(var1, Port.valueOf(var2));
   }

   public ServerParams(String var1, Port var2) {
      this.host = ArgCheck.checkNotBlank(var1);
      this.port = Preconditions.checkNotNull(var2);
      Preconditions.checkArgument(var2 != Port.INVALID, "Invalid port number.");
   }

   public String getHost() {
      return this.host;
   }

   public Port getPort() {
      return this.port;
   }

   public String getAddress() {
      return this.port == Port.DEFAULT ? this.host : this.host + ":" + this.port.getPort();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.host, this.port);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ServerParams)) {
         return false;
      }

      ServerParams var2 = (ServerParams)var1;
      return this.host.equals(var2.host) && this.port.equals(var2.port);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).addValue(this.getAddress()).toString();
   }
}
