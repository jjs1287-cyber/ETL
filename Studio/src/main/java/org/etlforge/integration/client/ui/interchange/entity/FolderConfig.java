package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class FolderConfig extends AbstractEntityConfig {
   private final ApplicationAction newFolderAction;

   public FolderConfig(ApplicationAction var1) {
      super(EntityType.Folder);
      this.newFolderAction = var1;
   }

   @Override
   protected ApplicationAction createNewEntityAction() {
      return this.newFolderAction;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return null;
   }
}
