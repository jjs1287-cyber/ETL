package com.etlforge.integration.client.ui.interchange.project.share;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.LocatableFolders;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.wait.WaitService;

public class EntityImporter {
   private final ShareProjectDataModel model;
   private WaitService waitService = WaitService.NONE;

   public EntityImporter(ShareProjectDataModel var1) {
      this.model = var1;
   }

   public void importItem(final IntegrationEntity var1, final IntegrationEntity var2) {
      Runnable var3 = new Runnable() {
         @Override
         public void run() {
            EntityImporter.this.importItemImpl(var1, var2);
         }
      };
      KongaSwingWorker.runWhileWaiting(var3, this.waitService);
   }

   private void importItemImpl(IntegrationEntity var1, IntegrationEntity var2) {
      try {
         IntegrationEntity var3 = this.translateParentIfNeeded(var1, var2);
         this.insertItemIntoTargetProject(var1, var3);
         this.saveTargetProject();
      } catch (Exception var4) {
         ErrorLog.attention(this.getClass(), "An unexpected error occurred", var4);
      }
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   private IntegrationEntity translateParentIfNeeded(IntegrationEntity var1, IntegrationEntity var2) {
      if (var2 instanceof IntegrationProject) {
         EntityType var5 = EntityUtils.getItemType(var1);
         return ((IntegrationProject)var2).getRootFolder(var5);
      }

      if (var1 instanceof Source || var1 instanceof Target) {
         RootFolder var3 = var2.getRootFolder();
         if (var3 != null) {
            LocatableFolders var4 = new LocatableFolders(var3);
            var2 = var4.findFolderForNewItem((Locatable)var1, (Folder)var2);
         }
      }

      return var2;
   }

   private void insertItemIntoTargetProject(IntegrationEntity var1, IntegrationEntity var2) {
      Preconditions.checkState(this.model.getSourceProject() != null, "No source project has been selected");
      if (this.isGreenLightedByUser(var1)) {
         com.etlforge.integration.client.project.share.ProjectItemImporter var3 = new com.etlforge.integration.client.project.share.ProjectItemImporter(
            this.model.getSourceProject(), this.model.getTargetProject()
         );
         EntityOverwriter var4 = new EntityOverwriter();
         ConflictResolverImpl var5 = new ConflictResolverImpl(this.model.getSourceProject(), var4, this.getNumberOfIdConflicts(var1));
         var5.setHistoryEntry(var3.getHistoryEntry());
         var3.setConflictResolver(var5);
         var3.importItem(var1, var2);
      }
   }

   private boolean isGreenLightedByUser(final IntegrationEntity var1) {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            DependenciesMessage var1x = new DependenciesMessage(EntityImporter.this.model.getSourceProject(), var1);
            return var1x.isOk();
         }
      });
   }

   private int getNumberOfIdConflicts(IntegrationEntity var1) {
      return new IdConflictCounter(this.model.getSourceProject(), this.model.getTargetProject()).count(var1);
   }

   private void saveTargetProject() {
      IntegrationProjectLock.acquire();

      try {
         this.model.getTargetProject().saveToDisk();
      } catch (InterchangeSavingException var5) {
         ErrorLog.attention(this.getClass(), "An error occurred when saving the changes made to the target project.", var5);
      } finally {
         IntegrationProjectLock.release();
      }
   }
}
