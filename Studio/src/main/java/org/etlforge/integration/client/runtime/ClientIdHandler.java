package org.etlforge.integration.client.runtime;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.appdir.ApplicationDirectory;

class ClientIdHandler {
   private static final String fileName = "client_guid.dat";

   ClientId getId(ApplicationDirectory var1) throws ClientPlatformException {
      ClientId var2 = this.readId(var1);
      if (var2 == null) {
         var2 = this.createAndWriteNewClientId(var1);
      }

      return var2;
   }

   private ClientId readId(ApplicationDirectory var1) throws ClientPlatformException {
      File var2 = new File(var1.getLocation(), "client_guid.dat");
      return var2.exists() && var2.canRead() ? this.readIdFromFile(var2) : null;
   }

   private ClientId readIdFromFile(File var1) throws ClientPlatformException {
      try {
         String var2 = this.readIdString(var1);
         return ClientId.fromString(var2);
      } catch (RuntimeException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   private String readIdString(File var1) {
      try {
         return Files.readFirstLine(var1, Charsets.UTF_8);
      } catch (IOException var3) {
         throw new ClientPlatformException("Failed to read the client ID", var3);
      }
   }

   private ClientId createAndWriteNewClientId(ApplicationDirectory var1) throws ClientPlatformException {
      File var2 = new File(var1.getLocation(), "client_guid.dat");
      ClientId var3 = ClientId.newClientId();

      try {
         Files.write(var3.toString(), var2, Charsets.UTF_8);
         var2.setReadOnly();
         return var3;
      } catch (IOException var5) {
         throw new ClientPlatformException("Failed to store new client ID", var5);
      }
   }
}
