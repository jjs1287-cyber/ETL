package org.etlforge.integration.plugin.pipeline;

import java.util.Comparator;
import org.apache.commons.lang.StringUtils;

public final class ManifestComparators {
   public static final Comparator<PipelinePluginManifest> DISPLAY_NAME = new Comparator<PipelinePluginManifest>() {
      public int compare(PipelinePluginManifest var1, PipelinePluginManifest var2) {
         String var3 = this.getStringToCompare(var1);
         String var4 = this.getStringToCompare(var2);
         return var3.compareToIgnoreCase(var4);
      }

      private String getStringToCompare(PipelinePluginManifest var1) {
         String var2 = var1.getDisplayName();
         if (StringUtils.isEmpty(var2)) {
            var2 = var1.getName();
         }

         if (var2 == null) {
            var2 = "";
         }

         return var2;
      }
   };

   private ManifestComparators() {
   }
}
