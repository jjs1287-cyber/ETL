package org.etlforge.integration.client.plugin.pipeline;

import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public final class PipelinePluginEntitySupport {
   public static PipelinePosition[] getApplicablePositions(PipelinePluginManifest var0, IntegrationEntity var1) {
      return getApplicablePositions(var0, var1.getEntityType());
   }

   public static PipelinePosition[] getApplicablePositions(PipelinePluginManifest var0, final EntityType var1) {
      PipelinePosition[] var2 = var0.getPipelinePositions();
      List var3 = KongaListUtils.filter(Arrays.asList(var2), new Predicate<PipelinePosition>() {
         public boolean apply(PipelinePosition var1x) {
            return var1x.getEntityType() == var1;
         }
      });
      return (PipelinePosition[]) var3.toArray(new PipelinePosition[var3.size()]);
   }

   private PipelinePluginEntitySupport() {
   }
}
