package org.etlforge.integration.client.ui.script.debug;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.debug.client.BreakLocation;

abstract class ConsoleLabelBuilder {
   private final BreakLocation loc;
   private final IntegrationEntity item;

   public static ConsoleLabelBuilder getBuilder(BreakLocation var0, IntegrationEntityLookup var1) {
      if (var0 != null) {
         IntegrationEntity var2 = var1.getEntity(var0.getIntegrationEntityId());
         if (var2 != null) {
            int var3 = var0.getLineNumber();
            if (var3 >= 0) {
               return StringUtils.isNotEmpty(var0.getTargetNodePath())
                  ? new ConsoleLabelBuilder.MappingLineBreakpointLabelBuilder(var0, var2)
                  : new ConsoleLabelBuilder.ScriptBreakpointLabelBuilder(var0, var2);
            }

            return new ConsoleLabelBuilder.EndOfMappingLabelBuilder(var0, var2);
         }
      }

      return new ConsoleLabelBuilder.UnknownLocationLabelBuilder();
   }

   protected ConsoleLabelBuilder(BreakLocation var1, IntegrationEntity var2) {
      this.loc = var1;
      this.item = var2;
   }

   public final String build() {
      StringBuilder var1 = new StringBuilder("paused");
      this.at(this.loc, var1);
      if (this.item != null) {
         var1.append(" in ").append(this.item.getName());
      }

      return var1.toString();
   }

   protected abstract void at(BreakLocation var1, StringBuilder var2);

   private static class EndOfMappingLabelBuilder extends ConsoleLabelBuilder {
      public EndOfMappingLabelBuilder(BreakLocation var1, IntegrationEntity var2) {
         super(var1, var2);
      }

      @Override
      protected void at(BreakLocation var1, StringBuilder var2) {
         var2.append(" @ end of mapping \"").append(var1.getTargetNodePath()).append("\"");
      }
   }

   private static class MappingLineBreakpointLabelBuilder extends ConsoleLabelBuilder {
      public MappingLineBreakpointLabelBuilder(BreakLocation var1, IntegrationEntity var2) {
         super(var1, var2);
      }

      @Override
      protected void at(BreakLocation var1, StringBuilder var2) {
         var2.append(" @ line ").append(var1.getLineNumber()).append(" of mapping \"").append(var1.getTargetNodePath()).append("\"");
      }
   }

   private static class ScriptBreakpointLabelBuilder extends ConsoleLabelBuilder {
      public ScriptBreakpointLabelBuilder(BreakLocation var1, IntegrationEntity var2) {
         super(var1, var2);
      }

      @Override
      protected void at(BreakLocation var1, StringBuilder var2) {
         var2.append(" @ line ").append(var1.getLineNumber());
      }
   }

   private static class UnknownLocationLabelBuilder extends ConsoleLabelBuilder {
      public UnknownLocationLabelBuilder() {
         super(null, null);
      }

      @Override
      protected void at(BreakLocation var1, StringBuilder var2) {
      }
   }
}
