package org.etlforge.integration.client.ui.interchange.deploy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.integration.client.deploy.DeployChecker;
import org.etlforge.integration.client.deploy.DeployGate;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class DeployGateImpl implements DeployGate {
   private final InterchangeDeployFacade facade;
   private final DependencyStore dependencies;
   private DeployChecker checker;
   private ImmutableList<IntegrationEntity> itemsToCheck;
   private Runnable job;

   public DeployGateImpl(InterchangeDeployFacade var1, DependencyStore var2) {
      this.facade = var1;
      this.dependencies = var2;
      this.checker = var1.createDefaultDeployChecker();
   }

   @Override
   public void setItemsToCheck(IntegrationEntity... var1) {
      this.itemsToCheck = ImmutableList.copyOf(var1);
   }

   @Override
   public void setItemsToCheck(Iterable<? extends IntegrationEntity> var1) {
      this.itemsToCheck = ImmutableList.copyOf(var1);
   }

   @Override
   public void setJob(Runnable var1) {
      this.job = Preconditions.checkNotNull(var1);
   }

   @Override
   public void setDeployChecker(DeployChecker var1) {
      this.checker = Preconditions.checkNotNull(var1);
   }

   @Override
   public void start() {
      boolean var1 = this.saveAllDirtyDependencies();
      if (var1) {
         if (this.checker.isUpToDate(this.itemsToCheck)) {
            this.job.run();
         } else {
            this.facade.promptDeploy(this.checker, this.job);
         }
      }
   }

   private boolean saveAllDirtyDependencies() {
      for (IntegrationEntity var3 : this.getRequiredItems()) {
         boolean var4 = UiCompanion.of(var3).saveIfDirty();
         if (!var4) {
            return false;
         }
      }

      return true;
   }

   private Set<IntegrationEntity> getRequiredItems() {
      HashSet var1 = Sets.newHashSet();

      for (IntegrationEntity var3 : this.itemsToCheck) {
         Iterables.addAll(var1, this.dependencies.getRequiredObjectsRecursively(var3));
      }

      return var1;
   }
}
