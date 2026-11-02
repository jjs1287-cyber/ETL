package org.etlforge.integration.client.plugin.ui.pipeline;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.util.KeyValueToolTip;

public final class PipelinePluginToolTip {
   private final String tooltip;

   public PipelinePluginToolTip(PipelinePluginManifest var1) {
      this.tooltip = createToolTip(var1);
   }

   public String get() {
      return this.tooltip;
   }

   private static String createToolTip(PipelinePluginManifest var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      var1.put(Strings.get("PipelinePlugin.Label.Name"), var0.getName());
      var1.put(Strings.get("PipelinePlugin.Label.Version"), var0.getVersion());
      var1.put(Strings.get("PipelinePlugin.Label.Author"), var0.getAuthor());
      return KeyValueToolTip.create(var1);
   }
}
