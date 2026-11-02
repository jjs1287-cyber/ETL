package org.etlforge.crypto;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public final class Providers {
   private static final Object lock = new Object();
   private static boolean bouncyCastleIsRegistered;

   public static void registerDefaultProvider() {
      registerBouncyCastle();
   }

   public static void registerBouncyCastle() {
      synchronized (lock) {
         if (!bouncyCastleIsRegistered) {
            Security.addProvider(new BouncyCastleProvider());
            bouncyCastleIsRegistered = true;
         }
      }
   }

   private Providers() {
   }
}
