package org.etlforge.integration.data.project.path;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.lang.KongaStringUtils;

public final class ProjectPathCompleter {
   private final IntegrationProject project;

   public ProjectPathCompleter(IntegrationProject var1) {
      this.project = var1;
   }

   public String complete(String var1) {
      ProjectPath var2 = new ProjectPath(var1);
      if (var2.hasWildcards()) {
         return var1;
      }

      if (var2.isPathToFolder()) {
         return this.addWildcardAndTryCompleteAgain(var1);
      }

      Optional var3 = var2.search(this.project);
      return var3.isPresent() ? ProjectPath.toPathString((IntegrationEntity)var3.get()) : this.addWildcardAndTryCompleteAgain(var1);
   }

   private String addWildcardAndTryCompleteAgain(String var1) {
      ProjectPath var2 = new ProjectPath(var1 + "*");
      Set var3 = var2.searchForItems(this.project);
      if (var3.isEmpty()) {
         return var1;
      }

      if (var3.size() == 1) {
         return ProjectPath.toPathString((IntegrationEntity)Iterables.get(var3, 0));
      }

      ArrayList var4 = Lists.newArrayList();

      for (Object __etlItem46 : var3) {
         IntegrationEntity var6 = (IntegrationEntity) __etlItem46;
         var4.add(ProjectPath.toPathString(var6));
      }

      return KongaStringUtils.findLongestCommonStartFragment(var4, true);
   }
}
