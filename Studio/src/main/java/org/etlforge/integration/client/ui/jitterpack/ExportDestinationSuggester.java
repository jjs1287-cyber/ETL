package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import org.etlforge.integration.client.project.jitterpack.JitterPackConstants;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.SystemUtils;

final class ExportDestinationSuggester {
   private static final ExportDestinationSuggester INSTANCE = new ExportDestinationSuggester();
   private final Map<IntegrationProjectId, String> suggestedNames = Maps.newHashMap();

   private ExportDestinationSuggester() {
   }

   public static ExportDestinationSuggester instance() {
      return INSTANCE;
   }

   public void addDestination(IntegrationProject var1, File var2) {
      if (JitterPackConstants.getSuggestedJitterpakName(var1).equals(var2.getName())) {
         this.suggestedNames.remove(var1.getID());
      } else {
         this.suggestedNames.put(var1.getID(), var2.getName());
      }
   }

   public File getSuggestedDestination(IntegrationProject var1, File var2) {
      String var3 = this.getSuggestedJitterPackName(var1);
      if (var2 == null) {
         File var4 = SystemUtils.USER_HOME;
         return new File(var4, var3);
      } else {
         return var2.isDirectory() ? new File(var2, var3) : new File(var2.getParent(), var3);
      }
   }

   private String getSuggestedJitterPackName(IntegrationProject var1) {
      String var2 = this.suggestedNames.get(var1.getID());
      if (var2 == null) {
         var2 = JitterPackConstants.getSuggestedJitterpakName(var1);
      }

      return var2;
   }
}

