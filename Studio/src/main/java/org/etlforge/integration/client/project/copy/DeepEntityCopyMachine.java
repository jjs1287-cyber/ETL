package org.etlforge.integration.client.project.copy;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.DefaultDeleteRequestHandler;
import org.etlforge.integration.client.project.ProjectAlteringProcessProcess;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;

public final class DeepEntityCopyMachine {
   private final EntityCopyMachine copier;
   private final EntityResources resources;
   private final DeepEntityCopyMachine.ServiceImpl serviceImpl;
   private Map<IntegrationEntity, IntegrationEntity> originalToClone;
   private DeleteRequestHandler deleteHandler;
   private Set<IntegrationEntity> itemsToExclude;

   public DeepEntityCopyMachine(EntityCopyMachine var1, EntityResources var2, IntegrationEntityLookup var3) {
      this.copier = var1;
      this.resources = var2;
      this.serviceImpl = new DeepEntityCopyMachine.ServiceImpl(var3);
      this.deleteHandler = new DefaultDeleteRequestHandler();
      this.itemsToExclude = Sets.newHashSet();
   }

   public void setItemsToExclude(Set<IntegrationEntity> var1) {
      this.itemsToExclude = Sets.newHashSet(var1);
   }

   public final void setDeleteHandler(DeleteRequestHandler var1) {
      this.deleteHandler = var1;
   }

   public DeepCopyResult cloneProjectWithoutSaving(IntegrationProject var1) {
      this.cloneAllProjectItems(var1);
      DeepCopyResult var2 = this.createResult(var1);
      var2.setSaveWhenModified(false);
      return var2;
   }

   private void cloneAllProjectItems(IntegrationProject var1) {
      this.originalToClone = Maps.newHashMap();
      List var2 = this.getAllProjectItemsToClone(var1);

      for (Object __etlItem59 : var2) {
         IntegrationEntity var4 = (IntegrationEntity) __etlItem59;
         if (!this.originalToClone.containsKey(var4)) {
            this.cloneImpl(var4);
         }
      }

      assert this.originalToClone.size() == var2.size() : "Not all items were cloned";
      this.flagCopiesAsNonTransient();
      this.copyOperationGraphLayouts();
   }

   private List<IntegrationEntity> getAllProjectItemsToClone(IntegrationProject var1) {
      List var2 = var1.getAllEntities();
      Iterables.removeIf(var2, EntityFilters.allOfType(EntityType.Folder));
      return var2;
   }

   public DeepCopyResult clone(final IntegrationEntity var1) {
      if (this.itemsToExclude.contains(var1)) {
         throw new IllegalStateException("item is in the set of objects to exclude");
      }

      IntegrationProject var2 = ProjectUtils.getProject(var1);
      return (new ProjectAlteringProcessProcess<DeepCopyResult>(var2) {
         protected DeepCopyResult doWork() {
            DeepEntityCopyMachine.this.originalToClone = Maps.newHashMap();
            DeepEntityCopyMachine.this.cloneImpl(var1);
            DeepEntityCopyMachine.this.flagCopiesAsNonTransient();
            DeepEntityCopyMachine.this.copyOperationGraphLayouts();
            return DeepEntityCopyMachine.this.createResult(var1);
         }
      }).run();
   }

   private IntegrationEntity cloneImpl(IntegrationEntity var1) {
      assert !this.itemsToExclude.contains(var1);
      assert !this.originalToClone.containsKey(var1) : "Asked to create a copy of an item that has already been copied.";
      IntegrationEntity var2 = this.copier.createAndInsertCopy(var1);
      this.originalToClone.put(var1, var2);
      var2.replaceReferences(this.serviceImpl);
      return var2;
   }

   private void flagCopiesAsNonTransient() {
      for (IntegrationEntity var2 : this.originalToClone.values()) {
         var2.setTransient(false);
      }
   }

   private DeepCopyResult createResult(IntegrationEntity var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      return new DeepCopyResult(var2, var1, this.originalToClone, this.deleteHandler);
   }

   private void copyOperationGraphLayouts() {
      for (IntegrationEntity var2 : this.originalToClone.keySet()) {
         if (var2 instanceof Operation) {
            OperationGraphLayoutCopier var3 = new OperationGraphLayoutCopier(this.resources, (Operation)var2, this.originalToClone);
            var3.copy();
         }
      }
   }

   private final class ServiceImpl implements ReferenceReplacementService {
      private final IntegrationEntityLookup itemLookup;

      public ServiceImpl(IntegrationEntityLookup var2) {
         this.itemLookup = var2;
      }

      @Override
      public <T extends IntegrationEntity> T getReplacement(IntegrationEntityId var1, Class<T> var2) {
         IntegrationEntity var3 = this.getReplacement(var1);
         return (T)(var3 == null ? null : var2.cast(var3));
      }

      @Override
      public IntegrationEntity getReplacement(IntegrationEntityId var1) {
         IntegrationEntity var2 = this.itemLookup.getEntity(var1);
         return var2 == null ? null : this.getCopy(var2);
      }

      private IntegrationEntity getCopy(IntegrationEntity var1) {
         if (this.shouldNotBeCopied(var1)) {
            return var1;
         }

         IntegrationEntity var2 = DeepEntityCopyMachine.this.originalToClone.get(var1);
         if (var2 == null) {
            var2 = DeepEntityCopyMachine.this.cloneImpl(var1);
         }

         return var2;
      }

      private boolean shouldNotBeCopied(IntegrationEntity var1) {
         return var1 instanceof BuiltInIntegrationEntity || DeepEntityCopyMachine.this.itemsToExclude.contains(var1);
      }

      @Override
      public IntegrationEntityLookup getItemLookup() {
         return this.itemLookup;
      }
   }
}
