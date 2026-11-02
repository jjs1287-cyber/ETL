package org.etlforge.integration.server.implementation.webservice.impl.common.folders;

import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;

public final class FolderItem {
   public static final String PARENT_FOLDER_ID = "parentfolder_id";
   public static final String ENTITY_TYPE_ID = "entitytype_id";
   public static final String ITEM_ID = "item_id";
   private static final String[] PROPERTIES = new String[]{"parentfolder_id", "entitytype_id", "item_id"};

   private FolderItem() {
   }

   public static String[] getPropertyKeysStatic() {
      return (String[])PROPERTIES.clone();
   }

   public static String[] getPropertyValues(Folder var0, IntegrationEntity var1) {
      String var2 = !(var0 instanceof RootFolder) && var0 != null ? var0.getID().toString() : "0";
      return new String[]{var2, Integer.toString(var1.getEntityType().getId()), var1.getID().toString()};
   }
}
