package org.etlforge.integration.filetransfer.client.upload.xsd;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.client.upload.FileUploadClient;
import org.etlforge.util.crypto.DigestUtils;

final class XsdUploadClient implements FileUploadClient {
   private final ServerFileFolder folder;
   private ImmutableList<ServerFileFolder> filesOnServer = ImmutableList.of();
   private final XsdFileStore xsdFileStore;
   private ServerFile mainStructureFile;
   private volatile boolean aborted;

   public XsdUploadClient(ServerFileFolder var1, XsdFileStore var2) {
      this.folder = var1;
      this.xsdFileStore = var2;
   }

   @Override
   public boolean isAborted() {
      return this.aborted;
   }

   @Override
   public synchronized void fileWasUploaded(String var1, ServerFile var2, ServerFileUploadResult var3) {
      this.storeFileInFileStore(var1, var3.getServerFile().getFilePath(), var2);
      this.registerExistingDependencies(var3);
   }

   private void registerExistingDependencies(ServerFileUploadResult var1) {
      String var2 = var1.getServerFile().getName();

      for (ServerFile var4 : var1.getExistingDependencies()) {
         String var5 = var4.getName();
         this.xsdFileStore.addDependency(var2, var5);
      }
   }

   @Override
   public FileStore getFileStore() {
      return this.xsdFileStore.getBackingFileStore();
   }

   @Override
   public synchronized void handleError(Throwable var1) {
      ErrorLog.attention(XsdUploadClient.class, var1.getMessage(), var1);
   }

   @Override
   public synchronized boolean isFileClearedForUpload(File var1) {
      ServerFile var2 = this.lookupExistingFile(this.folder, var1.getName());
      return var2 != null && !this.isIdenticalContent(var2, var1) ? this.askPermissionToOverwriteExistingFile(var1) : true;
   }

   @Override
   public synchronized void uploadCancelled() {
   }

   @Override
   public synchronized void uploadComplete(ServerInfo var1, ServerFile var2, ServerFile[] var3) {
      this.mainStructureFile = var2;
      XsdCache.update(var1.getGuid(), var3);
   }

   public synchronized ServerFile getMainFile() {
      return this.mainStructureFile;
   }

   public synchronized void setExistingFiles(Collection<ServerFileFolder> var1) {
      this.filesOnServer = ImmutableList.copyOf(var1);
   }

   private ServerFile lookupExistingFile(ServerFileFolder var1, String var2) {
      for (ServerFileFolder var4 : this.filesOnServer) {
         if (var4.equals(var1)) {
            return var1.getFile(var2);
         }
      }

      return null;
   }

   private boolean isIdenticalContent(ServerFile var1, File var2) {
      try {
         String var3 = var1.getMD5();
         String var4 = DigestUtils.getMd5FileDigestString(var2);
         return var3.equals(var4);
      } catch (FileNotFoundException var5) {
         var5.printStackTrace();
      } catch (IOException var6) {
         var6.printStackTrace();
      }

      return true;
   }

   private boolean askPermissionToOverwriteExistingFile(File var1) {
      String var2 = String.format(
         "A file with the name %s but with different content already exists on the server. Do you want to overwrite the existing file?", var1.getName()
      );
      Answer var3 = Ask.yesNoOrCancel(null, var2, "Overwrite?", Alert.Option.YES, Alert.Type.WARNING);
      return var3.isYes();
   }

   private void storeFileInFileStore(String var1, String var2, ServerFile var3) {
      try {
         String var4 = var3 == null ? null : var3.getFilePath();
         this.xsdFileStore.addFile(var1, var2, var4, FileCollisionResolver.REPLACE_EXISTING);
      } catch (IOException var5) {
         this.onFileStorageFailed(var5);
      } catch (FileStoreException var6) {
         this.onFileStorageFailed(var6);
      }
   }

   private void onFileStorageFailed(Exception var1) {
      String var2 = "Failed to store the XSD/DTD file on the local disk. Please contact ETLForge support.";
      ErrorLog.attention(XsdUploadClient.class, var2, var1);
   }
}
