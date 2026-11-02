package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractActiveObjectAction;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.entity.ParentFolderLocator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.factory.FolderFactory;
import org.etlforge.integration.data.entity.factory.IntegrationEntityFactoryException;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;

public final class NewFolderAction extends AbstractActiveObjectAction {
   public static final String ID = "New.Folder";
   private final ParentFolderLocator parentLocator;
   private final ProjectContentViewerManager contentViewers;
   private IntegrationEntity parent;
   private EntityType itemType;

   public NewFolderAction(ActiveObjectService var1, ParentFolderLocator var2, ProjectContentViewerManager var3) {
      super(var1, "New.Folder", InterchangeActionDictionary.DICTIONARY);
      this.parentLocator = var2;
      this.contentViewers = var3;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (NewFolderAction.this.isEnabled()) {
               NewFolderAction.this.createFolder();
            }
         }
      });
   }

   private void createFolder() {
      try {
         FolderFactory var1 = new FolderFactory();
         Folder var2 = this.parentLocator.getParent(EntityType.Folder);
         IntegrationEntity var3 = var1.createEntity(var2);
         IntegrationProject var4 = ProjectUtils.getProject(var2);
         if (IntegrationProjectLock.acquireWithin(3L, TimeUnit.SECONDS)) {
            try {
               this.insertFolderIntoProject(var3, var2, var4);
            } finally {
               IntegrationProjectLock.release();
            }
         } else {
            Alert.error("Failed to create the folder because a background process did not complete in time. Please try again.", "Timeout");
         }
      } catch (IntegrationEntityFactoryException var9) {
         ErrorLog.attention(NewFolderAction.class, "Failed to create the requested folder.", var9);
      }
   }

   private void insertFolderIntoProject(IntegrationEntity var1, Folder var2, IntegrationProject var3) {
      try {
         var3.insertEntity(var1, var2);
         ManagedProject var4 = ProjectUtils.getManagedProject(var3);
         var4.saveIntegrationEntity(var1);
         this.contentViewers.getViewer().renameEntity(var1);
      } catch (InterchangeSavingException var5) {
         ErrorLog.attention(NewFolderAction.class, "Failed to save the created folder to disk.", var5);
      }
   }

   private void setEnabled() {
      this.setEnabled(this.parent != null && this.itemType != null);
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof Folder;
   }

   @Override
   protected void updateEnabledState(List<? extends Object> var1) {
      this.updateState(var1);
   }

   private void updateState(List<? extends Object> var1) {
      this.itemType = null;
      this.parent = null;
      if (var1.size() == 1) {
         Object var2 = var1.get(0);
         if (var2 instanceof IntegrationEntity) {
            this.parent = (IntegrationEntity)var2;
            if (this.parent instanceof Folder) {
               this.itemType = ((Folder)this.parent).getItemType();
            }
         }
      }

      this.setEnabled();
   }
}
