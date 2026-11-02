package org.etlforge.util.net;

import java.io.IOException;
import java.net.Socket;
import org.etlforge.lang.exception.ExceptionHandler;

public final class KongaNetUtils {
   public static void close(Socket var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }

   public static void close(Socket var0, ExceptionHandler<IOException> var1) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var3) {
            var1.caught(var3);
         }
      }
   }

   private KongaNetUtils() {
   }
}
