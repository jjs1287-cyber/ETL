package org.etlforge.integration.client.plugin.pipeline;

import com.google.common.collect.ImmutableSet;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.util.version.Version;

public abstract class PipelinePluginSpecification {
   private static final PipelinePluginSpecification DEFAULT = new PipelinePluginSpecification.DefaultImpl();

   public static PipelinePluginSpecification getLatest() {
      return DEFAULT;
   }

   public static PipelinePluginSpecification getSpecification(Version var0) {
      return DEFAULT;
   }

   public abstract Version getVersion();

   public abstract boolean isEntitySupported(IntegrationEntity var1);

   private static class DefaultImpl extends PipelinePluginSpecification {
      private final Version version;
      private final ImmutableSet<EntityType> supportedTypes = ImmutableSet.of(EntityType.Source, EntityType.Target, EntityType.WebServiceCall);

      public DefaultImpl() {
         this.version = VersionFactory.newVersion(1, 0, 0);
      }

      @Override
      public boolean isEntitySupported(IntegrationEntity var1) {
         return this.supportedTypes.contains(var1.getEntityType());
      }

      @Override
      public Version getVersion() {
         return this.version;
      }
   }
}
