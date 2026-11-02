package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewerManager;
import org.etlforge.integration.client.ui.interchange.resources.InterchangeResources;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.functors.EntityFilters;

public final class ParentFolderLocator {
   private final CurrentProjectService currentProject;
   private final ProjectContentViewerManager contentViewers;

   public ParentFolderLocator(CurrentProjectService var1, ProjectContentViewerManager var2) {
      this.currentProject = Preconditions.checkNotNull(var1, "currentProject");
      this.contentViewers = Preconditions.checkNotNull(var2, "contentViewers");
   }

   public Folder getParent(EntityType var1) {
      ProjectContentViewer var2 = this.contentViewers.getViewer();
      List var3 = var2.getSelectedEntities();
      return var1 == EntityType.Folder ? this.findParentForFolder(var3) : this.findParentForNonFolder(var1, var3);
   }

   private Folder findParentForFolder(List<IntegrationEntity> var1) {
      if (var1.size() == 1) {
         IntegrationEntity var2 = (IntegrationEntity)var1.get(0);
         if (var2 instanceof Folder) {
            return (Folder)var2;
         } else {
            throw new RuntimeException("Folder cannot be created under a " + var2.getEntityType());
         }
      } else {
         throw new RuntimeException("Cannot find an appropriate owner for the new folder.");
      }
   }

   private Folder findParentForNonFolder(EntityType var1, List<IntegrationEntity> var2) {
      Folder var3 = this.findFolder(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         var3 = this.findRootFolder(var1);
         if (var3 != null) {
            return var3;
         } else {
            throw new RuntimeException(InterchangeResources.getString("DefaultUiFactory.Error.NoOwner"));
         }
      }
   }

   private Folder findFolder(EntityType var1, List<IntegrationEntity> var2) {
      List var3 = KongaListUtils.filter(var2, EntityFilters.foldersOfType(var1));
      return var3.size() == 1 ? (Folder)var3.get(0) : null;
   }

   private RootFolder findRootFolder(EntityType var1) {
      return this.currentProject.getProject().getRootFolder(var1);
   }
}
