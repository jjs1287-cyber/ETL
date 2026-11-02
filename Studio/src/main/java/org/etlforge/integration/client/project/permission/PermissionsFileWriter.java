package org.etlforge.integration.client.project.permission;

import com.google.common.base.Charsets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.UserId;
import org.etlforge.io.Folder;

final class PermissionsFileWriter {
   private final Folder root;
   private final EntityType entityType;
   private BufferedWriter userWriter;
   private BufferedWriter groupWriter;

   public PermissionsFileWriter(Folder var1, EntityType var2) {
      this.root = var1;
      this.entityType = var2;
   }

   public void close() throws IOException {
      if (this.userWriter != null) {
         this.userWriter.close();
      }

      if (this.groupWriter != null) {
         this.groupWriter.close();
      }
   }

   public void writeUser(IntegrationEntityId var1, UserId var2, int var3, int var4) throws IOException {
      if (this.userWriter == null) {
         this.userWriter = this.openFile("_User.dat");
      }

      String var5 = getLineToWrite(var1, var2, var3, var4);
      this.userWriter.write(var5);
      this.userWriter.newLine();
   }

   private BufferedWriter openFile(String var1) throws IOException {
      this.createFolderIfNeeded();
      File var2 = this.root.getFile(this.entityType.toString() + var1);
      return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2), Charsets.UTF_8));
   }

   private void createFolderIfNeeded() {
      if (!this.root.createOnDisk()) {
      }
   }

   private static String getLineToWrite(IntegrationEntityId var0, Object var1, int var2, int var3) {
      StringBuilder var4 = new StringBuilder(var0.toString());
      var4.append(",");
      var4.append(var1.toString());
      var4.append(",");
      var4.append(String.valueOf(var2));
      var4.append(",");
      var4.append(String.valueOf(var3));
      return var4.toString();
   }

   public void writeGroup(IntegrationEntityId var1, GroupId var2, int var3, int var4) throws IOException {
      if (this.groupWriter == null) {
         this.groupWriter = this.openFile("_Group.dat");
      }

      String var5 = getLineToWrite(var1, var2, var3, var4);
      this.groupWriter.write(var5);
      this.groupWriter.newLine();
   }
}
