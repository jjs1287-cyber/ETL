package org.etlforge.integration.server.services;

import org.apache.commons.lang.SystemUtils;

public abstract class ServerRestarter {
   public static ServerRestarter getRestarter() {
      if (SystemUtils.IS_OS_WINDOWS) {
         return new WindowsServerRestarter();
      } else if (SystemUtils.IS_OS_LINUX) {
         return new LinuxServerRestarter();
      } else {
         throw new UnsupportedOperationException("Unsupported operating system: " + System.getProperty("os.name"));
      }
   }

   public abstract void restartServer() throws ServerServiceException;
}
