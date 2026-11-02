package org.etlforge.integration.plugin.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.plugin.PluginIdentifier;

public class PipelinePosition {
   public static final int DEFAULT_EXECUTION_ORDER = 50;
   private final PluginIdentifier pluginId;
   private final EntityType entityType;
   private final RelativePosition relativePosition;
   private final int executionOrder;
   private final boolean optional;

   public PipelinePosition(PluginIdentifier var1, EntityType var2, RelativePosition var3, int var4, boolean var5) {
      if (var1 == null) {
         throw new IllegalArgumentException("pluginId cannot be null.");
      }

      if (var2 == null) {
         throw new IllegalArgumentException("type cannot be null.");
      }

      if (var3 == null) {
         throw new IllegalArgumentException("relative cannot be null.");
      }

      this.pluginId = var1;
      this.entityType = var2;
      this.relativePosition = var3;
      this.executionOrder = var4;
      this.optional = var5;
   }

   public PluginIdentifier getPluginId() {
      return this.pluginId;
   }

   public EntityType getEntityType() {
      return this.entityType;
   }

   public RelativePosition getRelativePosition() {
      return this.relativePosition;
   }

   public boolean isOptional() {
      return this.optional;
   }

   public int getExecutionOrder() {
      return this.executionOrder;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof PipelinePosition ? this.equals((PipelinePosition)var1) : super.equals(var1);
      }
   }

   private boolean equals(PipelinePosition var1) {
      return this.pluginId.equals(var1.getPluginId())
         && this.entityType == var1.getEntityType()
         && this.relativePosition.equals(var1.getRelativePosition())
         && this.optional == var1.isOptional();
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.pluginId.hashCode();
      var1 = 31 * var1 + this.entityType.hashCode();
      var1 = 31 * var1 + this.relativePosition.hashCode();
      return 31 * var1 + (this.optional ? 1231 : 1237);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("PipelinePosition:[");
      var1.append("EntityType=").append(this.entityType);
      var1.append(", Relative=").append(this.relativePosition);
      var1.append(", ExecOrder=").append(this.executionOrder);
      var1.append(", IsOptional=").append(this.optional);
      var1.append("]");
      return var1.toString();
   }
}
