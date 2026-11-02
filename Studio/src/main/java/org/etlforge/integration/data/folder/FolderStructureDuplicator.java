package org.etlforge.integration.data.folder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public final class FolderStructureDuplicator {
   private final IntegrationProject project;
   private final EntityType from;
   private final Set<Folder> newFolders;

   public FolderStructureDuplicator(IntegrationProject var1, EntityType var2) {
      Preconditions.checkNotNull(var1, "project");
      Preconditions.checkNotNull(var2, "from");
      checkAllowedType(var2);
      this.project = var1;
      this.from = var2;
      this.newFolders = Sets.newHashSet();
   }

   private static void checkAllowedType(EntityType var0) {
      Preconditions.checkArgument(var0 != EntityType.Folder, "Unsupported type: " + var0);
      Preconditions.checkArgument(var0 != EntityType.RootFolder, "Unsupported type: " + var0);
      Preconditions.checkArgument(var0 != EntityType.Project, "Unsupported type: " + var0);
   }

   public void duplicateTo(Collection<EntityType> var1) {
      for (EntityType var3 : var1) {
         this.sanityCheck(var3);
      }

      for (EntityType var5 : var1) {
         this.duplicateToImpl(var5);
      }
   }

   private void sanityCheck(EntityType var1) {
      Preconditions.checkNotNull(var1, "target");
      checkAllowedType(var1);
      Preconditions.checkArgument(var1 != this.from, "target cannot be the same as the source type");
   }

   private void duplicateToImpl(EntityType var1) {
      this.sanityCheck(var1);
      RootFolder var2 = this.project.getRootFolder(this.from);
      RootFolder var3 = this.project.getRootFolder(var1);

      for (Folder var5 : var2.getAllSubFolders()) {
         if (!var5.isEtlforgeConnectFolder()) {
            this.processFolder(var5, var3);
         }
      }
   }

   private void processFolder(Folder var1, Folder var2) {
      Folder var3 = var2.findSubFolder(var1.getName());
      if (var3 == null) {
         var3 = new Folder(var1.getName(), var2.getItemType());
         this.project.insertEntity(var3, var2);
         this.newFolders.add(var3);
      }

      for (Folder var5 : var1.getAllSubFolders()) {
         this.processFolder(var5, var3);
      }
   }

   public void save(ProjectPersistor var1) throws InterchangeSavingException {
      if (!this.newFolders.isEmpty()) {
         for (Folder var3 : this.newFolders) {
            var1.save(var3);
         }

         var1.saveProjectRelations();
         this.newFolders.clear();
      }
   }

   public int getNumberOfNewFolders() {
      return this.newFolders.size();
   }
}
