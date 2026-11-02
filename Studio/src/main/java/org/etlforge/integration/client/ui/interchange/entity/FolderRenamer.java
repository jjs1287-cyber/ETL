package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.application.ui.Alert;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.folder.FolderNameValidator;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.util.name.IllegalNameException;

final class FolderRenamer implements EntityRenamer {
   private final DefaultEntityRenamer impl;
   private final FolderNameValidator folderNameValidator = new FolderNameValidator();

   public FolderRenamer(EntityPersistor var1) {
      this.impl = new DefaultEntityRenamer(var1);
   }

   @Override
   public boolean setNewName(IntegrationEntity var1, String var2) {
      try {
         this.folderNameValidator.validateName(var2);
         return this.impl.setNewName(var1, var2);
      } catch (IllegalNameException var4) {
         Alert.error(var4.getMessage(), "Invalid Name");
         return false;
      }
   }
}
