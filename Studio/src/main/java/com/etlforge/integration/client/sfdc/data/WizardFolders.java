package com.etlforge.integration.client.sfdc.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.EntityPath;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.LocatableFolders;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.sfdc.SalesforceProjectFolders;
import org.etlforge.integration.data.project.IntegrationProject;

public final class WizardFolders {
   private final IntegrationProject project;
   private final Folder wizardParent;
   private final Map<EntityType, Folder> wizardsRootFolders = Maps.newHashMap();
   private final Map<EntityType, Folder> wizardTypeRootFolders = Maps.newHashMap();
   private final Map<EntityType, Folder> wizardSpecificFolders = Maps.newHashMap();
   private final SalesforceProjectFolders projectFolders;
   private final List<ImplementingObject<Folder>> createdByWizard = Lists.newArrayList();

   public WizardFolders(IntegrationProject var1, EntityType var2, Folder var3) {
      this.project = var1;
      this.wizardParent = var3;
      this.projectFolders = new SalesforceProjectFolders(var1, var2);
   }

   public void startOver() {
      for (ImplementingObject var2 : this.createdByWizard) {
         ((Folder)var2.getObject()).delete();
      }

      this.createdByWizard.clear();
   }

   public ImmutableList<ImplementingObject<Folder>> getFoldersCreatedByWizard() {
      return ImmutableList.copyOf(this.createdByWizard);
   }

   public Folder getWizardTypeRootFolder(EntityType var1) {
      Folder var2 = this.wizardTypeRootFolders.get(var1);
      if (var2 == null) {
         var2 = this.projectFolders.findWizardTypeRoot(var1);
         if (var2 == null) {
            var2 = this.projectFolders.createWizardTypeRoot(var1);
            Folder var3 = this.getWizardsRootFolder(var1);
            this.addNewFolder(var2, var3);
            this.wizardTypeRootFolders.put(var1, var2);
         }
      }

      return var2;
   }

   private Folder getWizardsRootFolder(EntityType var1) {
      Folder var2 = this.wizardsRootFolders.get(var1);
      if (var2 == null) {
         var2 = this.projectFolders.findWizardsRoot(var1);
         if (var2 == null) {
            var2 = this.projectFolders.createWizardsRoot(var1);
            this.addNewFolder(var2, this.project.getRootFolder(var1));
         }

         this.wizardsRootFolders.put(var1, var2);
      }

      return var2;
   }

   private final void addNewFolder(Folder var1, Folder var2) {
      var1.setTransient(true);
      ImplementingObject var3 = ImplementingObject.newObject(var2, var1);
      this.createdByWizard.add(var3);
   }

   public Folder getWizardSpecificFolder(EntityType var1) {
      Folder var2 = this.wizardSpecificFolders.get(var1);
      if (var2 == null) {
         var2 = this.createWizardSpecificFolder(var1);
         this.wizardSpecificFolders.put(var1, var2);
      }

      return var2;
   }

   private Folder createWizardSpecificFolder(EntityType var1) {
      Folder var2 = this.getWizardTypeRootFolder(var1);
      EntityPath var3 = new EntityPath(this.wizardParent, false);
      Folder var4 = null;
      Folder var5 = var2;

      for (IntegrationEntity var7 : var3) {
         if (!(var7 instanceof RootFolder)) {
            assert var7 instanceof Folder;
            var4 = var5.findSubFolder(var7.getName());
            if (var4 == null) {
               var4 = new Folder(var7.getName(), var1);
               this.addNewFolder(var4, var5);
            }

            var5 = var4;
         }
      }

      assert var5 != null;
      return var5;
   }

   public Folder getParentForNewProjectItem(IntegrationEntity var1) {
      RootFolder var2 = this.project.getRootFolder(var1.getEntityType());
      if (var1 instanceof Locatable) {
         LocatableFolders var3 = new LocatableFolders(var2);
         return var3.findFolderForNewItem((Locatable)var1, var2);
      } else {
         return var2;
      }
   }
}
