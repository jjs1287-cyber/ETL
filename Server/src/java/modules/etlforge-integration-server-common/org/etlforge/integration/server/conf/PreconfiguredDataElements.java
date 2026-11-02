package org.etlforge.integration.server.conf;

import com.google.common.collect.ImmutableMap;
import org.etlforge.integration.dataelement.DataElementContext;
import org.etlforge.integration.dataelement.DataElementMapContext;

public final class PreconfiguredDataElements {
   private static final String PRECONFIGURED_DATA_ELEMENTS = "PredefinedServerGlobalDataElement";
   private static final Object lock = new Object();
   private static DataElementContext context;

   public static DataElementContext getContext() {
      synchronized (lock) {
         if (context == null) {
            context = loadContext();
         }

         return context;
      }
   }

   private static DataElementContext loadContext() {
      ImmutableMap var0 = ServerConfig.getSection("PredefinedServerGlobalDataElement");
      return new DataElementMapContext(var0);
   }

   static {
      ServerConfig.addConfigurationListener(new PreconfiguredDataElements.Updater());
   }

   private static class Updater implements ServerConfigListener {
      private Updater() {
      }

      @Override
      public void configReloaded() {
         synchronized (PreconfiguredDataElements.lock) {
            PreconfiguredDataElements.context = null;
         }
      }
   }
}
