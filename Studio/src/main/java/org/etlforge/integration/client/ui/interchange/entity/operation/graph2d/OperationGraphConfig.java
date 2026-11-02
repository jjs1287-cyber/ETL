package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.Icon;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;

public final class OperationGraphConfig {
   private final ImmutableMap<PipelineActivityType, ActivityTypeConfig> typeConfigs;

   public static final OperationGraphConfig standardConfig() {
      return new OperationGraphConfig(standardTypes(), Collections.emptyList());
   }

   public OperationGraphConfig(Iterable<? extends ActivityTypeConfig> var1) {
      this(standardTypes(), var1);
   }

   private static Iterable<? extends ActivityTypeConfig> standardTypes() {
      return Arrays.asList(
         new SourceActivityTypeConfig(),
         new TargetActivityTypeConfig(),
         new TransformationActivityTypeConfig(),
         new ScriptActivityTypeConfig(),
         new WebServiceActivityTypeConfig(),
         new BulkActivityTypeConfig()
      );
   }

   public OperationGraphConfig(Iterable<? extends ActivityTypeConfig> var1, Iterable<? extends ActivityTypeConfig> var2) {
      Builder var3 = ImmutableMap.builder();

      for (ActivityTypeConfig var5 : var1) {
         var3.put(var5.getType(), var5);
      }

      for (ActivityTypeConfig var7 : var2) {
         var3.put(var7.getType(), var7);
      }

      this.typeConfigs = var3.build();
   }

   public ActivityTypeConfig getTypeConfig(PipelineActivityType var1) {
      ActivityTypeConfig var2 = this.typeConfigs.get(var1);
      Preconditions.checkArgument(var2 != null, "No ActivityTypeConfig available for type " + var1);
      return var2;
   }

   public Icon getSmallIcon(PipelineActivityType var1) {
      ActivityTypeConfig var2 = this.getTypeConfig(var1);
      return var2.getSmallIcon();
   }
}
