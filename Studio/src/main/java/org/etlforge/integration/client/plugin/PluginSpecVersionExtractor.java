package org.etlforge.integration.client.plugin;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.plugin.PluginSpecificationException;
import org.etlforge.util.version.Version;

public abstract class PluginSpecVersionExtractor {
   private static final PluginSpecVersionExtractor IMPLEMENTATION = new PluginSpecVersionExtractor.Implementation();

   public static PluginSpecVersionExtractor getExtractor() {
      return IMPLEMENTATION;
   }

   public abstract Version getVersion(String var1) throws PluginSpecificationException;

   private static class Implementation extends PluginSpecVersionExtractor {
      private Implementation() {
      }

      @Override
      public Version getVersion(String var1) {
         return VersionFactory.newVersion(1, 0, 0, 0);
      }
   }
}
