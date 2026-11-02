package org.etlforge.integration.data.project;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.path.ProjectPathQuery;

public final class ProjectPathFilter {
   private final String filterString;

   public ProjectPathFilter(String var1) {
      this.filterString = var1;
   }

   public Set<IntegrationEntity> apply(IntegrationProject var1) {
      try {
         ProjectPathQuery var2 = new ProjectPathQuery(var1);
         String var3 = getPathFromFilterString(this.filterString);
         return var2.findItems(var3);
      } catch (Exception var4) {
         return Sets.newHashSet();
      }
   }

   private static String getPathFromFilterString(String var0) {
      boolean var1 = Character.isWhitespace(var0.charAt(var0.length() - 1));
      String var2 = var0.trim();
      return var1 ? var2 : var2 + "*";
   }
}
