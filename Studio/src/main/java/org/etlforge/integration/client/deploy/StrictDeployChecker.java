package org.etlforge.integration.client.deploy;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.ProjectDependencies;

public final class StrictDeployChecker implements DeployChecker {
   private final ProjectDependencies dependencies;
   private Set<IntegrationEntity> itemsNotDeployed;
   private Set<IntegrationEntity> dirtyItems;

   public StrictDeployChecker(ProjectDependencies var1) {
      this.dependencies = var1;
   }

   @Override
   public boolean isUpToDate(Iterable<IntegrationEntity> var1) {
      this.itemsNotDeployed = null;
      this.dirtyItems = null;
      StrictDeployChecker.RecursionGuard var2 = new StrictDeployChecker.RecursionGuard();

      for (IntegrationEntity var4 : var1) {
         this.check(var4, var2);
      }

      return this.itemsNotDeployed == null && this.dirtyItems == null;
   }

   @Override
   public void promptDeploy(DeployPrompt var1, DeployPromptCallback var2) {
      if (var1.prompt(this.itemsNotDeployed, this.dirtyItems)) {
         ArrayList var3 = Lists.newArrayList();
         if (this.itemsNotDeployed != null) {
            var3.addAll(this.itemsNotDeployed);
         }

         var3.addAll(var1.getDirtyItemsToDeploy());
         if (var3.isEmpty()) {
            var2.deployDeclined();
         } else {
            var2.deployGreenLighted(var3);
         }
      } else {
         var2.cancelled();
      }
   }

   private void check(IntegrationEntity var1, StrictDeployChecker.RecursionGuard var2) {
      if (!var2.isCyclicDependency(var1)) {
         if (!var1.hasBeenDeployed()) {
            if (this.itemsNotDeployed == null) {
               this.itemsNotDeployed = Sets.newHashSet();
            }

            this.itemsNotDeployed.add(var1);
         } else if (var1.isDeployDirty()) {
            if (this.dirtyItems == null) {
               this.dirtyItems = Sets.newHashSet();
            }

            this.dirtyItems.add(var1);
         }

         for (IntegrationEntity var4 : this.dependencies.getRequiredObjects(var1)) {
            this.check(var4, var2);
         }
      }
   }

   private static class RecursionGuard {
      private final Set<IntegrationEntity> set = Sets.newHashSet();

      private RecursionGuard() {
      }

      public boolean isCyclicDependency(IntegrationEntity var1) {
         if (this.set.contains(var1)) {
            return true;
         }

         this.set.add(var1);
         return false;
      }
   }
}
