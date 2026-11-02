package org.etlforge.integration.client.plugin.ui.pipeline.view;

import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.util.version.Version;

final class AvailabilityProblem implements Comparable<AvailabilityProblem> {
   public final boolean fatal;
   public final String pluginName;
   public final String problem;

   public static AvailabilityProblem missing(String var0) {
      return new AvailabilityProblem(true, var0, PluginUiResources.getString("RequiredPluginsView.Problem.Missing"));
   }

   public static AvailabilityProblem versionTooLow(String var0, Version var1, Version var2) {
      return new AvailabilityProblem(false, var0, PluginUiResources.format("RequiredPluginsView.Problem.Version", var1, var2));
   }

   private AvailabilityProblem(boolean var1, String var2, String var3) {
      this.fatal = var1;
      this.pluginName = var2;
      this.problem = var3;
   }

   public int compareTo(AvailabilityProblem var1) {
      return this.getCompareString().compareToIgnoreCase(var1.getCompareString());
   }

   private String getCompareString() {
      return (this.fatal ? "1" : "0") + this.problem;
   }
}
