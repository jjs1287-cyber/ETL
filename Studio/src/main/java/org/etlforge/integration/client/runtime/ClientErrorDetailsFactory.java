package org.etlforge.integration.client.runtime;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.errorlog.ErrorDetailsGenerator;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerPlatform;
import org.etlforge.io.KongaIoUtils;

public final class ClientErrorDetailsFactory implements ErrorDetailsGenerator {
   private final ServerConnectionService connectionService;

   public ClientErrorDetailsFactory(ServerConnectionService var1) {
      this.connectionService = var1;
   }

   @Override
   public String getDetails(ErrorLevel var1, Throwable var2) {
      String var3 = this.getExceptionMessage(var1, var2);
      StringBuilder var4 = new StringBuilder(var3);
      this.appendVersionInfoToDetails(var4);
      this.appendClientPlatformInfoToDetails(var4);
      this.appendServerPlatormInfoToDetails(var4);
      return var4.toString();
   }

   private String getExceptionMessage(ErrorLevel var1, Throwable var2) {
      if (var2 instanceof OutOfMemoryError) {
         return this.getOutOfMemoryMessage();
      }

      String var3 = var2.getMessage();
      if (StringUtils.isBlank(var3)) {
         var3 = "An unexpected error occurred. [" + var2.getClass().getName() + "]";
      }

      return var3 + "\n\n" + this.getLevelSpecificAppendix(var1);
   }

   private String getOutOfMemoryMessage() {
      return "The client has run out of memory. Please restart the client to prevent data corruption. \n\nThis problem may be caused by editing several complex transformations at the same time, or by loading large data files into the \"Load Source Data\" or \"Test Operation\" functionality. If possible, try using smaller sample files.\n\n";
   }

   private String getLevelSpecificAppendix(ErrorLevel var1) {
      return var1 == ErrorLevel.INTERNAL ? "This is an internal error that should not affect the functionality of the application." : "";
   }

   private void appendVersionInfoToDetails(StringBuilder var1) {
      this.appendClientVersionToDetails(var1);
      this.appendServerVersionToDetails(var1);
   }

   private void appendClientVersionToDetails(StringBuilder var1) {
      var1.append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);
      var1.append("Client version: ");
      var1.append(ClientInfo.getCurrentVersion());
   }

   private void appendServerVersionToDetails(StringBuilder var1) {
      var1.append(KongaIoUtils.LINE_BREAK).append("Server version: ");
      ServerInfo var2 = this.connectionService.getCurrentServerInfo();
      if (var2 == null) {
         var1.append("[Not connected]");
      } else {
         var1.append(var2.getVersion());
      }
   }

   private void appendClientPlatformInfoToDetails(StringBuilder var1) {
      var1.append(KongaIoUtils.LINE_BREAK).append("Client platform: ");
      var1.append(SystemUtils.OS_NAME);
      var1.append(" - Java ").append(SystemUtils.JAVA_VERSION);
   }

   private void appendServerPlatormInfoToDetails(StringBuilder var1) {
      ServerInfo var2 = this.connectionService.getCurrentServerInfo();
      if (var2 != null) {
         var1.append(KongaIoUtils.LINE_BREAK).append("Server platform: ");
         ServerPlatform var3 = var2.getPlatform();
         var1.append(var3.getOS());
         var1.append(KongaIoUtils.LINE_BREAK).append(var3.getSystemInfo());
      }
   }
}
