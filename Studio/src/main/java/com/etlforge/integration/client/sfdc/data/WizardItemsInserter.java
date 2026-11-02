package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;

public final class WizardItemsInserter {
   private final IntegrationProject project;
   private final Map<IntegrationEntity, Folder> createdItems;
   private final Map<Folder, Folder> existingFolders = Maps.newHashMap();

   public WizardItemsInserter(IntegrationProject var1, Collection<? extends ImplementingObject<?>> var2) {
      this.project = var1;
      this.createdItems = Maps.newHashMap();

      for (ImplementingObject var4 : var2) {
         if (!var4.isSystemEntity()) {
            this.createdItems.put(var4.getObject(), var4.getParent());
         }
      }
   }

   public WizardItemsInserter(IntegrationProject var1, Map<IntegrationEntity, Folder> var2) {
      this.project = var1;
      this.createdItems = Maps.newHashMap(var2);
   }

   public void insertAllItems() {
      this.removeEmptyFolders();

      while (!this.createdItems.isEmpty()) {
         Entry var1 = this.getNextItemToInsert();
         IntegrationEntity var2 = (IntegrationEntity)var1.getKey();
         this.createdItems.remove(var2);
         Folder var3 = (Folder)var1.getValue();
         if (this.existingFolders.containsKey(var3)) {
            var3 = this.existingFolders.get(var3);
         }

         if (var2 instanceof Folder) {
            String var4 = var2.getName();
            Folder var5 = var3.findSubFolder(var4);
            if (var5 != null) {
               this.existingFolders.put((Folder)var2, var5);
               continue;
            }
         }

         String var6 = var2.getName();
         String var7 = EntityUtils.getAvailableName(var2, var3, var6);
         var2.setName(var7);
         this.project.insertEntity(var2, var3);
      }
   }

   private void removeEmptyFolders() {
      for (Folder var3 : this.collectEmptyFolders()) {
         this.createdItems.remove(var3);
      }
   }

   private Set<Folder> collectEmptyFolders() {
      HashSet var1 = Sets.newHashSet();
      HashSet var2 = Sets.newHashSet();

      for (Entry var4 : this.createdItems.entrySet()) {
         Folder var5 = (Folder)var4.getValue();
         var2.add(var5);
         IntegrationEntity var6 = (IntegrationEntity)var4.getKey();
         if (var6 instanceof Folder) {
            var1.add((Folder)var6);
         }
      }

      var1.removeAll(var2);
      return var1;
   }

   private Entry<IntegrationEntity, Folder> getNextItemToInsert() {
      assert !this.createdItems.isEmpty();

      for (Entry var2 : this.createdItems.entrySet()) {
         if (!this.createdItems.containsKey(var2.getValue())) {
            return var2;
         }
      }

      throw new RuntimeException("Could not find the next item to insert. This is a bug");
   }
}
