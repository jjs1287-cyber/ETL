package com.etlforge.integration.client.project.share.history;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.ProjectDescriptor;
import org.etlforge.util.persist.Persistor;

public class ImportHistoryEntry {
   private final ProjectDescriptor sourceProject;
   private final Date timeStamp;
   private final List<ImportedEntity> items;
   private ImportedEntity mainItem;

   static ImportHistoryEntry restore(Persistor var0) {
      ProjectDescriptor var1 = restoreSourceProject(var0);
      if (var1 == null) {
         return null;
      }

      Date var2 = restoreTimeStamp(var0);
      if (var2 == null) {
         return null;
      }

      ImportHistoryEntry var3 = new ImportHistoryEntry(var1, var2);
      restoreEntities(var3, var0);
      return var3;
   }

   private static ProjectDescriptor restoreSourceProject(Persistor var0) {
      Persistor var1 = var0.getFirstChild("SourceProject");
      return var1 != null ? ProjectDescriptor.restore(var1) : null;
   }

   private static Date restoreTimeStamp(Persistor var0) {
      Long var1 = var0.getLong("when");
      return var1 != null ? new Date(var1) : null;
   }

   private static void restoreEntities(ImportHistoryEntry var0, Persistor var1) {
      for (Persistor var3 : var1.getChildren("Item")) {
         ImportedEntity var4 = ImportedEntity.restore(var3);
         if (var4 != null) {
            var0.addEntity(var4);
            Boolean var5 = var3.getBoolean("main");
            if (var5 != null && var5) {
               var0.mainItem = var4;
            }
         }
      }
   }

   public ImportHistoryEntry(ProjectDescriptor var1, Date var2) {
      this.sourceProject = Preconditions.checkNotNull(var1, "sourceProject");
      this.timeStamp = new Date(var2.getTime());
      this.items = Lists.newArrayList();
   }

   public ProjectDescriptor getSourceProject() {
      return this.sourceProject;
   }

   public Date getTimeStamp() {
      return new Date(this.timeStamp.getTime());
   }

   public ImportedEntity addEntity(IntegrationEntity var1, IntegrationEntity var2) {
      return this.addEntity(new ImportedEntity(var1, var2));
   }

   public ImportedEntity addEntity(ImportedEntity var1) {
      this.items.add(Preconditions.checkNotNull(var1));
      return var1;
   }

   public List<ImportedEntity> getImportedEntities() {
      return Lists.newArrayList(this.items);
   }

   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   public void setMainEntity(IntegrationEntity var1) {
      this.mainItem = null;

      for (ImportedEntity var3 : this.items) {
         if (var1.getID().equals(var3.getOriginalEntity().getGuid())) {
            this.mainItem = var3;
            return;
         }
      }
   }

   public boolean isMainEntity(ImportedEntity var1) {
      return var1 == this.mainItem;
   }

   void writeTo(Persistor var1) {
      this.sourceProject.writeTo(var1.createChild("SourceProject"));
      var1.putLong("when", this.timeStamp.getTime());

      for (ImportedEntity var3 : this.items) {
         Persistor var4 = var1.createChild("Item");
         if (var3 == this.mainItem) {
            var4.putBoolean("main", true);
         }

         var3.writeTo(var4);
      }
   }
}
