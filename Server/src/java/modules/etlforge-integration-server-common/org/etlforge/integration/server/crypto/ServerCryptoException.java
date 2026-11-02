package org.etlforge.integration.server.crypto;

import org.etlforge.integration.server.exception.ServerRuntimeException;

public class ServerCryptoException extends ServerRuntimeException {
   public ServerCryptoException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public ServerCryptoException(String var1) {
      super(var1);
   }

   public ServerCryptoException(Throwable var1) {
      super(var1);
   }
}
