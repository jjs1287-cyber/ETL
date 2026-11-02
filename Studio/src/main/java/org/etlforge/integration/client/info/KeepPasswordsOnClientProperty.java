package org.etlforge.integration.client.info;

import org.etlforge.application.config.ApplicationConfiguration;

public final class KeepPasswordsOnClientProperty {
   private static final String PROPERTY_NAME = "KeepPasswordsOnClient";
   private static volatile boolean value = ApplicationConfiguration.getBoolean("KeepPasswordsOnClient", false);

   private KeepPasswordsOnClientProperty() {
   }

   public static void set(boolean var0) {
      value = var0;
   }

   public static boolean getValue() {
      return value;
   }
}
