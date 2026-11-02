package org.etlforge.integration.client.project;

import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.LocatableFolders;
import org.etlforge.integration.data.entity.SourceFolders;
import org.etlforge.integration.data.entity.TargetFolders;
import org.etlforge.integration.data.project.IntegrationProject;

final class SourceAndTargetFolders {
   private final IntegrationProject project;
   private boolean createIfNotThere;

   public static void prepareNewProject(IntegrationProject var0) {
      SourceAndTargetFolders var1 = new SourceAndTargetFolders(var0);
      var1.createIfNotThere = ClientInfo.isDataLoader();
      var1.prepare();
   }

   public static void prepareExistingProject(IntegrationProject var0) {
      SourceAndTargetFolders var1 = new SourceAndTargetFolders(var0);
      var1.prepare();
   }

   public static SourceAndTargetFolders forExistingProject(IntegrationProject var0) {
      return new SourceAndTargetFolders(var0);
   }

   private SourceAndTargetFolders(IntegrationProject var1) {
      this.project = var1;
   }

   private void prepare() {
      this.prepare(new SourceFolders(this.project.getRootFolder(EntityType.Source)));
      this.prepare(new TargetFolders(this.project.getRootFolder(EntityType.Target)));
   }

   private void prepare(LocatableFolders var1) {
      var1.prepare(this.createIfNotThere);
   }
}
