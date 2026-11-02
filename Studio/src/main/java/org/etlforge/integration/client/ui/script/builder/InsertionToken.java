package org.etlforge.integration.client.ui.script.builder;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.system.ArgCheck;

public interface InsertionToken {
   final class DataElement implements InsertionToken {
      private final String name;

      public DataElement(String var1) {
         this.name = ArgCheck.checkNotBlank(var1);
      }

      public String getName() {
         return this.name;
      }

      @Override
      public String toString() {
         return "Data Element: " + this.name;
      }
   }

   final class Function implements InsertionToken {
      private final org.etlforge.integration.data.script.Function function;

      public Function(org.etlforge.integration.data.script.Function var1) {
         this.function = Preconditions.checkNotNull(var1);
      }

      public org.etlforge.integration.data.script.Function getFunction() {
         return this.function;
      }

      @Override
      public String toString() {
         return "Function: " + this.function.getDisplayName();
      }
   }

   final class Plugin implements InsertionToken {
      private final PipelinePluginManifest manifest;

      public Plugin(PipelinePluginManifest var1) {
         this.manifest = Preconditions.checkNotNull(var1);
      }

      public PipelinePluginManifest getManifest() {
         return this.manifest;
      }

      @Override
      public String toString() {
         return "Plugin: " + this.manifest.getDisplayName();
      }
   }

   final class ProjectItem implements InsertionToken {
      private final IntegrationEntity item;

      public ProjectItem(IntegrationEntity var1) {
         this.item = Preconditions.checkNotNull(var1);
      }

      public IntegrationEntity getItem() {
         return this.item;
      }

      @Override
      public String toString() {
         return "Project Item: " + this.item;
      }
   }
}
