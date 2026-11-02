package org.etlforge.integration.client.plugin.pipeline;

import java.io.File;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.plugin.PluginSpecificationException;
import org.etlforge.util.version.Version;

public abstract class PipelinePluginSpecVersionExtractor {
   public abstract Version getVersion(String var1) throws PluginSpecificationException;

   public abstract Version getVersion(File var1) throws PluginSpecificationException;

   protected abstract boolean canHandlePluginSpec(Version var1);

   public static PipelinePluginSpecVersionExtractor getExtractor(Version var0) {
      return new PipelinePluginSpecVersionExtractor.DefaultImplementation();
   }

   private static class DefaultImplementation extends PipelinePluginSpecVersionExtractor {
      private DefaultImplementation() {
      }

      @Override
      protected boolean canHandlePluginSpec(Version var1) {
         return true;
      }

      @Override
      public Version getVersion(File var1) {
         return VersionFactory.newVersion(1, 0, 0);
      }

      @Override
      public Version getVersion(String var1) {
         return VersionFactory.newVersion(1, 0, 0);
      }
   }
}
