package org.etlforge.integration.client.plugin.pipeline.xml;

import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifestLoader;
import org.etlforge.util.version.Version;

public abstract class PipelinePluginManifestLoaderFactory {
   private static final Set<PipelinePluginManifestLoaderFactory> FACTORIES = new HashSet<>();

   public abstract boolean canHandleSpecification(Version var1);

   public abstract PipelinePluginManifestLoader newLoader();

   public static PipelinePluginManifestLoaderFactory getFactory(Version var0) {
      for (PipelinePluginManifestLoaderFactory var2 : FACTORIES) {
         if (var2.canHandleSpecification(var0)) {
            return var2;
         }
      }

      return null;
   }

   private static void init() {
      FACTORIES.add(new PipelinePluginManifestLoaderFactory.V10_Factory());
   }

   static {
      init();
   }

   private static class V10_Factory extends PipelinePluginManifestLoaderFactory {
      private V10_Factory() {
      }

      @Override
      public boolean canHandleSpecification(Version var1) {
         return true;
      }

      @Override
      public PipelinePluginManifestLoader newLoader() {
         return new PipelinePluginManifestXmlLoader_V10();
      }
   }
}
