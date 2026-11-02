package org.etlforge.integration.client.server;

import com.google.common.base.Preconditions;

public class ServerAddressFactory {
   private final ServerPorts serverPorts;

   public static ServerAddressFactory usingPorts(final int var0, final int var1) {
      return new ServerAddressFactory(new ServerPorts() {
         @Override
         public int getDefaultPort() {
            return var0;
         }

         @Override
         public int getDefaultHttpsPort() {
            return var1;
         }
      });
   }

   public ServerAddressFactory(ServerPorts var1) {
      this.serverPorts = var1;
   }

   public ServerAddress fromString(String var1, boolean var2) throws IllegalServerAddressException {
      var1 = var1.trim();
      if (var1.endsWith(":")) {
         throw new IllegalServerAddressException(var1);
      }

      String var3 = null;
      int var4 = -1;
      String[] var5 = var1.split(":");
      switch (var5.length) {
         case 1:
            var3 = var1;
            var4 = this.getDefaultPort(var2);
            break;
         case 2:
            var3 = var5[0];

            try {
               var4 = Integer.parseInt(var5[1]);
               break;
            } catch (NumberFormatException var8) {
               throw new IllegalServerAddressException("Invalid port number: " + var1);
            }
         default:
            throw new IllegalServerAddressException(var1);
      }

      try {
         return new ServerAddressFactory.ServerAddressImpl(var3, var4);
      } catch (IllegalArgumentException var7) {
         throw new IllegalServerAddressException("Invalid server address: " + var1 + ". " + var7.getMessage());
      }
   }

   private int getDefaultPort(boolean var1) {
      return var1 ? this.serverPorts.getDefaultHttpsPort() : this.serverPorts.getDefaultPort();
   }

   private static final class ServerAddressImpl implements ServerAddress {
      private final String serverName;
      private final int portNumber;

      private ServerAddressImpl(String var1, int var2) {
         var1 = var1.trim();
         Preconditions.checkArgument(!var1.isEmpty(), "The server name cannot be empty.");
         Preconditions.checkArgument(var2 >= 1 && var2 <= 65536, "The port number must be in the range [1, 65636].");
         this.serverName = var1;
         this.portNumber = var2;
      }

      @Override
      public String getServerName() {
         return this.serverName;
      }

      @Override
      public int getPortNumber() {
         return this.portNumber;
      }

      @Override
      public String toString() {
         return this.serverName + ":" + this.portNumber;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof ServerAddressFactory.ServerAddressImpl)) {
            return false;
         }

         ServerAddressFactory.ServerAddressImpl var2 = (ServerAddressFactory.ServerAddressImpl)var1;
         return this.serverName.equalsIgnoreCase(var2.serverName) && this.portNumber == var2.portNumber;
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + this.serverName.toLowerCase().hashCode();
         return 31 * var1 + this.portNumber;
      }
   }
}
