package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;

public class RootFolderId extends FolderId {
   public RootFolderId() {
   }

   public RootFolderId(String var1) {
      super(var1);
   }

   @Override
   public EntityType getEntityType() {
      return EntityType.RootFolder;
   }

   public static void main(String[] var0) {
      System.out.println(new RootFolderId());
   }
}
