package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import com.google.common.collect.Sets;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.integration.client.project.ProjectFolders;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.loadsource.client.LatestFile;
import org.etlforge.integration.loadsource.client.LoadSourceFileStore;
import org.etlforge.io.Folder;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.util.zip.ZipUtils;

public final class LoadSourceFileStoreImpl implements LoadSourceFileStore {
   private final Folder root;
   private final LatestFile latestFile;

   public LoadSourceFileStoreImpl(Transformation var1) throws FileStoreException {
      this.root = this.createRootFolder(var1);
      this.latestFile = new LatestFile(this.root);
   }

   private Folder createRootFolder(Transformation var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      Folder var3 = ProjectFolders.getTestDataFolder(var2.getLocationOnDisk());
      Folder var4 = new Folder(var3, "source", var1.getID().toString());
      var4.createOnDisk();
      return var4;
   }

   @Override
   public Folder getRoot() {
      return this.root;
   }

   @Override
   public boolean contains(File var1) {
      return var1.exists() && this.root.contains(var1);
   }

   @Override
   public void update(File var1) {
      try {
         File var2;
         if (this.contains(var1)) {
            var2 = var1;
         } else {
            var2 = this.root.getFile(var1.getName());
            this.writeFileToFileStore(var1, var2);
         }

         this.latestFile.update(var2);
      } catch (IOException var3) {
         this.logStoreFileFailed(var3);
      }
   }

   private void writeFileToFileStore(File var1, File var2) throws IOException {
      if (ZipUtils.isLikelyZipped(var1)) {
         Files.copy(var1, var2);
      } else {
         byte[] var3 = ZipUtils.deflateFileContents(var1);
         Files.write(var3, var2);
      }
   }

   private void logStoreFileFailed(Exception var1) {
      ErrorLog.log(this.getClass(), "Failed to store the source file in the project's file store.", var1);
   }

   @Override
   public boolean removeFile(File var1) {
      if (this.root.contains(var1.getName())) {
         boolean var2 = var1.delete();
         if (var2) {
            this.latestFile.deleteFile(var1);
            return true;
         }
      }

      return false;
   }

   @Override
   public FileUi[] getFileList() {
      LinkedHashSet var1 = Sets.newLinkedHashSet();
      FileUi var2 = this.getLatestFile();
      if (var2 != null) {
         var1.add(var2);
      }

      for (File var6 : this.root.listNonFolders()) {
         if (!this.latestFile.isPropertyFile(var6)) {
            var1.add(new FileUi(var6, this.root.asJavaFile()));
         }
      }

      return (FileUi[]) var1.toArray(new FileUi[var1.size()]);
   }

   @Override
   public FileUi getLatestFile() {
      return this.latestFile.getLatest();
   }
}
