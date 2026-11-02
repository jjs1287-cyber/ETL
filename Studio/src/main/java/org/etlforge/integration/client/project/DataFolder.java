package org.etlforge.integration.client.project;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.io.Folder;

public final class DataFolder {
   private final Folder path;

   public static DataFolder in(Folder var0) {
      return new DataFolder(var0.subFolder("Data"));
   }

   private DataFolder(Folder var1) {
      this.path = var1;
   }

   public Folder getPath() {
      return this.path;
   }

   public EntityDataDirectory getEntityTypeFolder(EntityType var1) {
      return new EntityDataDirectory(var1, this.path.subFolder(var1.name()));
   }

   public Iterable<EntityDataDirectory> getEntityTypeFolders() {
      ArrayList var1 = Lists.newArrayList();

      for (Folder var5 : this.path.listSubFolders()) {
         try {
            EntityType var6 = EntityType.valueOf(var5.getName());
            var1.add(new EntityDataDirectory(var6, var5));
         } catch (Exception var7) {
         }
      }

      return var1;
   }

   @Override
   public String toString() {
      return this.path.toString();
   }
}
