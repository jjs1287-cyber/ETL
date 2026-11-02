package org.etlforge.integration.data.structure.edi;

import com.google.common.base.Preconditions;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public final class EdiPlugin {
   public static PipelinePosition forSource() {
      return getPipelinePosition(EntityType.Source);
   }

   public static PipelinePosition getPipelinePosition(EntityType var0) {
      Preconditions.checkArgument(var0 == EntityType.Source || var0 == EntityType.Target, "type must be one of Source or Target, was " + var0);
      PluginIdentifier var1 = getPluginId();
      RelativePosition var2 = var0 == EntityType.Source ? RelativePosition.POST : RelativePosition.PRE;
      return new PipelinePosition(var1, var0, var2, 50, true);
   }

   public static PluginIdentifier getPluginId() {
      try {
         String var0 = ApplicationConfiguration.getString("EdiPlugin.Name", "http://www.etlforge.invalid/plugins/pipeline/user/EDIReader");
         Version var1 = ApplicationConfiguration.getVersion("EdiPlugin.Version", VersionFactory.fromString("1.0.0-beta"));
         return new PluginIdentifier(var0, var1);
      } catch (IllegalVersionException var2) {
         throw new RuntimeException(var2);
      }
   }

   private EdiPlugin() {
   }
}
