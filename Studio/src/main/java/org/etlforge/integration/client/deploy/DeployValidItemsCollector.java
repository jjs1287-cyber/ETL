package org.etlforge.integration.client.deploy;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RequiredObjectsCollector;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class DeployValidItemsCollector {
   private final IntegrationProject entityStore;
   private final DependencyStore dependencyStore;
   private final ValidationModel validationModel;

   public DeployValidItemsCollector(IntegrationProject var1, DependencyStore var2, ValidationModel var3) {
      this.entityStore = Preconditions.checkNotNull(var1);
      this.dependencyStore = Preconditions.checkNotNull(var2);
      this.validationModel = Preconditions.checkNotNull(var3);
   }

   public Set<IntegrationEntity> getItemsToDeploy() {
      Set var1 = this.getValidItems();
      HashSet var2 = new HashSet();
      RequiredObjectsCollector var3 = new RequiredObjectsCollector(this.dependencyStore);

      for (Object __etlItem31 : var1) {
         IntegrationEntity var5 = (IntegrationEntity) __etlItem31;
         if (this.canBeDeployed(var5, var3, var1)) {
            var2.add(var5);
         }
      }

      Set var6 = this.collectAllParentFolders(var2);
      var2.addAll(var6);
      return var2;
   }

   private Set<IntegrationEntity> getValidItems() {
      HashSet var1 = new HashSet();

      for (IntegrationEntity var3 : this.entityStore.getAllEntities()) {
         if (this.validationModel.getStatus(var3, false) != ValidationStatus.INVALID) {
            var1.add(var3);
         }
      }

      return var1;
   }

   private boolean canBeDeployed(IntegrationEntity var1, RequiredObjectsCollector var2, Set<IntegrationEntity> var3) {
      for (IntegrationEntity var5 : var2.collect(var1)) {
         if (!var3.contains(var5)) {
            return false;
         }
      }

      return true;
   }

   private Set<IntegrationEntity> collectAllParentFolders(Iterable<IntegrationEntity> var1) {
      HashSet var2 = new HashSet();

      for (IntegrationEntity var4 : var1) {
         for (IntegrationEntity var5 = var4.getParent(); !this.isParentStop(var5); var5 = var5.getParent()) {
            var2.add(var5);
         }
      }

      return var2;
   }

   private boolean isParentStop(@Nullable IntegrationEntity var1) {
      return var1 == null || var1 instanceof RootFolder || var1 instanceof IntegrationProject;
   }
}
