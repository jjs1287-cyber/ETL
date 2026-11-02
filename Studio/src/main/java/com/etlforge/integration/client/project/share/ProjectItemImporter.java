package com.etlforge.integration.client.project.share;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.project.share.history.ImportHistory;
import com.etlforge.integration.client.project.share.history.ImportHistoryEntry;
import java.util.Date;
import java.util.Set;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.EntityTypeDependencyOrder;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.RequiredObjectsCollector;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.ProjectDescriptor;

public final class ProjectItemImporter {
   private final ManagedProject sourceProject;
   private final ManagedProject targetProject;
   private ConflictResolver conflictResolver;
   private final Set<IntegrationEntity> importedItems;
   private final ImportHistoryEntry historyEntry;

   public ProjectItemImporter(ManagedProject var1, ManagedProject var2) {
      this.sourceProject = var1;
      this.targetProject = var2;
      this.conflictResolver = new DefaultConflictResolver(var1);
      this.importedItems = Sets.newHashSet();
      this.historyEntry = new ImportHistoryEntry(new ProjectDescriptor(var1.getProject()), new Date());
   }

   public ImportHistoryEntry getHistoryEntry() {
      return this.historyEntry;
   }

   public void setConflictResolver(ConflictResolver var1) {
      Preconditions.checkNotNull(var1, "resolver");
      this.conflictResolver = var1;
   }

   public void importItem(IntegrationEntity var1, IntegrationEntity var2) {
      this.checkNewParentInTargetProject(var2);
      this.insertRequiredObjects(var1, var2);
      if (var2 instanceof RootFolder) {
         var2 = this.insertAllAncestors(var1);
      }

      this.insertIntoNewProject(var1, var2);
      this.doPostImportHandling();
      this.updateDependencies();
      this.addToImportHistory(var1);
   }

   private void addToImportHistory(IntegrationEntity var1) {
      if (!this.historyEntry.isEmpty()) {
         this.historyEntry.setMainEntity(var1);
         ImportHistory var2 = new ImportHistory(this.targetProject);
         var2.addEntry(this.historyEntry);
      }
   }

   private void updateDependencies() {
      this.targetProject.rebuildDependencies();
   }

   private void doPostImportHandling() {
      IntegrationEntityLookup var1 = this.targetProject.getProject().getItemLookup();

      for (IntegrationEntity var3 : this.importedItems) {
         var3.doPostImportProcessing(var1);
      }
   }

   private void insertRequiredObjects(IntegrationEntity var1, IntegrationEntity var2) {
      for (IntegrationEntity var5 : this.getRequiredItems(var1)) {
         if (!(var5 instanceof BuiltInIntegrationEntity)) {
            IntegrationEntity var6 = var2;
            if (var5.getParent() != var1.getParent() || var2 instanceof RootFolder) {
               var6 = this.insertAllAncestors(var5);
            }

            this.insertIntoNewProject(var5, var6);
         }
      }
   }

   private IntegrationEntity insertAllAncestors(IntegrationEntity var1) {
      IntegrationEntity var2 = null;

      for (IntegrationEntity var5 : new EntityPath(var1, false)) {
         if (var5 == var1) {
            break;
         }

         var2 = this.insertIntoNewProject(var5, var2);
      }

      return var2;
   }

   private void checkNewParentInTargetProject(IntegrationEntity var1) {
      Preconditions.checkArgument(this.lookForExistingItem(null, var1) != null, "newParent does not belong to the target project");
   }

   private IntegrationEntity insertIntoNewProject(IntegrationEntity var1, IntegrationEntity var2) {
      IntegrationEntity var3 = this.lookForExistingItem(var2, var1);
      if (var3 != null) {
         if (!var1.getID().equals(var3.getID())) {
            if (var3 instanceof Folder) {
               return var3;
            }

            throw new RuntimeException("Unexpected condition: Found an existing non-folder item with a different ID than the original " + var1);
         }

         IdConflict var4 = new IdConflict(var1, var3);
         ConflictResolution var5 = var4.resolve(this.conflictResolver);
         if (var5.skipItem()) {
            this.importedItems.add(var3);
            return var3;
         }
      } else if (var1 instanceof Folder) {
         Folder var8 = (Folder)var1;
         Folder var10 = new Folder(var8.getName(), var8.getItemType());
         this.targetProject.getProject().insertEntity(var10, var2);
         return var10;
      }

      IntegrationEntity var7 = var1.clone();
      Conflict var9 = this.checkNameConflict(var7, var2);
      if (var9 != null) {
         ConflictResolution var6 = var9.resolve(this.conflictResolver);
         if (var6.skipItem()) {
            return var3;
         }
      }

      this.targetProject.getProject().insertEntity(var7, var2);
      this.historyEntry.addEntity(var1, var7);
      this.importedItems.add(var7);
      this.transferResources(var7);
      return var7;
   }

   private IntegrationEntity lookForExistingItem(IntegrationEntity var1, IntegrationEntity var2) {
      IntegrationEntityLookup var3 = this.targetProject.getProject().getItemLookup();
      IntegrationEntity var4 = var3.getEntity(var2.getID());
      if (var4 == null && var2 instanceof Folder && var1 != null) {
         assert var1 instanceof Folder;
         var4 = ((Folder)var1).findSubFolder(var2.getName());
      }

      return var4;
   }

   private void transferResources(IntegrationEntity var1) {
      EntityResourcesImporter var2 = this.getResourceImporter(var1);
      var2.transferResources(this.sourceProject, this.targetProject);
   }

   private EntityResourcesImporter getResourceImporter(IntegrationEntity var1) {
      EntityResourcesImporter var2 = var1.getExtensionObject(EntityResourcesImporter.class);
      return var2 != null ? var2 : new DefaultEntityResourcesImporter(var1);
   }

   private Conflict checkNameConflict(IntegrationEntity var1, IntegrationEntity var2) {
      IntegrationEntity var3 = this.findDuplicate(var1, var2);
      return var3 != null ? new NameConflict(var1, var3) : null;
   }

   private IntegrationEntity findDuplicate(IntegrationEntity var1, IntegrationEntity var2) {
      String var3 = var1.getName();

      for (IntegrationEntity var5 : var2.getAllChildren()) {
         if (var3.equals(var5.getName())) {
            return var5;
         }
      }

      return null;
   }

   private Iterable<IntegrationEntity> getRequiredItems(IntegrationEntity var1) {
      RequiredObjectsCollector var2 = new RequiredObjectsCollector(this.sourceProject.getDependencyStore());
      Set var3 = var2.collect(var1);
      var3.remove(var1);
      return Iterables2.sortedCopy(var3, new EntityTypeDependencyOrder());
   }
}
