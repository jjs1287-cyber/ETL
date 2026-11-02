package org.etlforge.integration.data.project;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.entity.RootFolder;

final class PartialProjectCreator {
   private final IntegrationProject originalProject;
   private IntegrationProject projectShell;
   private final Set<IntegrationEntity> items;
   private LinkedList<IntegrationEntity> remainingItems;
   private IntegrationEntityMapLookup entityLookup;

   public PartialProjectCreator(IntegrationProject var1, Collection<? extends IntegrationEntity> var2) {
      this.originalProject = var1;
      this.items = Sets.newHashSet(var2);
      if (!this.items.isEmpty()) for (org.etlforge.integration.data.entity.ProjectVariable variable : var1.getAllEntitiesOfClass(org.etlforge.integration.data.entity.ProjectVariable.class)) this.items.add(variable);
   }

   public IntegrationProject create(IntegrationProject var1) {
      this.projectShell = var1;
      this.entityLookup = IntegrationEntityMapLookup.build(var1);
      if (this.items.isEmpty()) {
         return var1;
      }

      this.remainingItems = new LinkedList<>(this.items);

      while (!this.remainingItems.isEmpty()) {
         IntegrationEntity var2 = this.remainingItems.removeFirst();
         if (this.isProcessingRequred(var2)) {
            new PartialProjectCreator.ParentChainBuilder(var2).rebuild();
         }
      }

      var1.setLocationOnDisk(this.originalProject.getLocationOnDisk());
      return var1;
   }

   private boolean isProcessingRequred(IntegrationEntity var1) {
      return var1 != this.originalProject && !(var1 instanceof RootFolder);
   }

   private class ParentChainBuilder {
      private IntegrationEntity currentEntity;
      private IntegrationEntity currentParent;

      public ParentChainBuilder(IntegrationEntity var2) {
         this.currentEntity = var2.clone();
         this.currentParent = var2.getParent();
      }

      public void rebuild() {
         while (this.currentParent != null) {
            PartialProjectCreator.this.entityLookup.addItem(this.currentEntity);
            if (!PartialProjectCreator.this.items.contains(this.currentParent)) {
               RootFolder var1 = this.getRootFolderForEntity(this.currentEntity);
               this.currentEntity.setParent(var1);
               return;
            }

            IntegrationEntity var2 = this.attachToParent();
            if (var2 == null) {
               return;
            }

            this.currentEntity = var2;
            this.currentParent = this.currentParent.getParent();
         }
      }

      private RootFolder getRootFolderForEntity(IntegrationEntity var1) {
         EntityType var2 = EntityUtils.getItemType(var1);
         return PartialProjectCreator.this.projectShell.getRootFolder(var2);
      }

      private IntegrationEntity attachToParent() {
         PartialProjectCreator.this.remainingItems.remove(this.currentParent);
         if (this.currentParent instanceof RootFolder) {
            EntityType var3 = EntityUtils.getItemType(this.currentEntity);
            RootFolder var2 = PartialProjectCreator.this.projectShell.getRootFolder(var3);
            this.currentEntity.setParent(var2);
            return null;
         } else {
            IntegrationEntity var1 = this.getParentClone(this.currentParent);
            this.currentEntity.setParent(var1);
            return var1;
         }
      }

      private IntegrationEntity getParentClone(IntegrationEntity var1) {
         IntegrationEntity var2 = PartialProjectCreator.this.entityLookup.getEntity(var1.getID());
         if (var2 == null) {
            var2 = var1.clone();
         }

         return var2;
      }
   }
}
