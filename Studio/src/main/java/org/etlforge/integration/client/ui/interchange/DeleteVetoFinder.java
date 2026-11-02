package org.etlforge.integration.client.ui.interchange;

import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

final class DeleteVetoFinder {
   public IntegrationEntity findVeto(List<IntegrationEntity> var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      if (var2 == null) {
         return null;
      }

      Set var3 = this.getNonWizardItems(var1);
      return var3.isEmpty() ? null : this.checkItems(var2, var3);
   }

   private Set<IntegrationEntity> getNonWizardItems(List<IntegrationEntity> var1) {
      List var2 = KongaListUtils.filter(var1, Predicates.not(EntityFilters.WIZARDS));
      return this.collectFolderItems(var2);
   }

   private Set<IntegrationEntity> collectFolderItems(List<IntegrationEntity> var1) {
      HashSet var2 = Sets.newHashSet();

      for (IntegrationEntity var4 : var1) {
         if (var4 instanceof Folder) {
            var2.addAll(((Folder)var4).getAllItemsRecursively());
         } else {
            var2.add(var4);
         }
      }

      return var2;
   }

   private IntegrationEntity checkItems(IntegrationProject var1, Set<IntegrationEntity> var2) {
      DependencyStore var3 = var1.getCurrentDependencies();

      try {
         for (IntegrationEntity var5 : var2) {
            for (IntegrationEntity var7 : var3.getDependingObjects(var5)) {
               if (!var2.contains(var7)) {
                  return var5;
               }
            }
         }
      } finally {
         var3.dispose();
      }

      return null;
   }
}
