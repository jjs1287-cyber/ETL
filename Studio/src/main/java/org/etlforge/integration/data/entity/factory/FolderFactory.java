package org.etlforge.integration.data.entity.factory;

import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.RootFolder;

public class FolderFactory implements IntegrationEntityFactory {
   public Folder createEntity(Folder var1) throws IntegrationEntityFactoryException {
      RootFolder var2 = var1.getRootFolder();
      if (var2 == null) {
         throw new IntegrationEntityFactoryException("Unknown root folder for the parent " + var1);
      }

      Folder var3 = new Folder("", var2.getItemType());
      var3.setName(EntityFactoryUtils.getNewEntityName(var3, var1));
      return var3;
   }
}
