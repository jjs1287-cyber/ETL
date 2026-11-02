package com.etlforge.integration.client.ui.sfdc.wsdl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.etlforge.integration.salesforce.wsdl.KnownWsdl;
import com.etlforge.integration.salesforce.wsdl.KnownWsdlsContentsList;
import com.etlforge.integration.salesforce.wsdl.KnownWsdlsStore;
import com.etlforge.integration.salesforce.wsdl.WsdlWithCredentials;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.DummyFileStore;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.wsdl.WsdlUtils;
import org.etlforge.integration.client.wsdl.serverapi.WsdlProvider;
import org.etlforge.integration.client.wsdl.upload.WsdlUploader;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.io.Folder;
import org.etlforge.system.callback.CallbackResultAdapter;
import org.etlforge.util.KongaSerializationUtils;

class FileBasedKnownWsdlsStore implements KnownWsdlsStore {
   private final Folder storageDirectory;
   private KnownWsdlsContentsList toc;
   private final File tocFile;
   private final ReadWriteLock lock;

   public FileBasedKnownWsdlsStore(File var1) {
      this.storageDirectory = new Folder(var1);
      this.tocFile = this.storageDirectory.getFile(".contents");
      this.lock = new ReentrantReadWriteLock();
   }

   @Override
   public Iterable<KnownWsdl> getKnownWsdls() {
      ArrayList var1 = Lists.newArrayList();
      this.lock.readLock().lock();

      try {
         this.collectWsdlsFromTableOfContents(var1);
      } finally {
         this.lock.readLock().unlock();
      }

      return var1;
   }

   private void collectWsdlsFromTableOfContents(List<KnownWsdl> var1) {
      try {
         KnownWsdlsContentsList var2 = this.getTableOfContents();

         for (WsdlWithCredentials var4 : var2.getWsdlsWithCredentials()) {
            KnownWsdl var5 = this.createWsdl(var4);
            if (var5 != null) {
               var1.add(var5);
            }
         }
      } catch (IOException var6) {
         ErrorLog.log(FileBasedKnownWsdlsStore.class, "Failed to retrieve the list of known SFDC WSDLs.", var6);
      }
   }

   private KnownWsdlsContentsList getTableOfContents() throws IOException {
      if (this.toc == null) {
         this.toc = this.readTableOfContentsFromFile();
      }

      return this.toc;
   }

   private KnownWsdlsContentsList readTableOfContentsFromFile() throws IOException {
      if (this.tocFile.exists() && this.tocFile.canRead()) {
         try {
            return KongaSerializationUtils.deserializeFromFile(this.tocFile, KnownWsdlsContentsList.class);
         } catch (ClassNotFoundException var2) {
            ErrorLog.log(FileBasedKnownWsdlsStore.class, "Failed to retrieve the list of known SFDC WSDLs.", var2);
         }
      }

      return new KnownWsdlsContentsList();
   }

   private KnownWsdl createWsdl(WsdlWithCredentials var1) {
      String var2 = var1.wsdl;
      if (var2.isEmpty()) {
         return null;
      }

      if (this.isRemote(var2)) {
         return new FileBasedKnownWsdlsStore.RemoteWsdl(var2, var1.credentials);
      }

      File var3 = this.storageDirectory.getFile(var2);
      return var3.exists() && var3.canRead() ? new FileBasedKnownWsdlsStore.LocalWsdl(var3, var1.credentials) : null;
   }

   private boolean isRemote(String var1) {
      String var2 = var1.toLowerCase();
      return var2.startsWith("http://") || var2.startsWith("https://");
   }

   @Override
   public void addLocalWsdl(String var1, byte[] var2, LoginCredentials var3) {
      Preconditions.checkNotNull(var1, "name");
      Preconditions.checkNotNull(var2, "contents");
      Preconditions.checkNotNull(var3, "credentials");
      this.lock.writeLock().lock();

      try {
         this.ensureLocationExists();
         File var4 = this.storageDirectory.getFile(var1);
         Files.write(var2, var4);
         this.addToTableOfContents(var1, var3);
      } catch (IOException var8) {
         ErrorLog.log(FileBasedKnownWsdlsStore.class, "Failed to add known SFDC WSDL to the client's local store.", var8);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   private void addToTableOfContents(String var1, LoginCredentials var2) throws IOException {
      KnownWsdlsContentsList var3 = this.getTableOfContents();
      var3.addWsdl(var1, var2);
      this.writeTableOfContentsToFile();
   }

   private void writeTableOfContentsToFile() throws IOException {
      KongaSerializationUtils.serializeToFile(this.toc, this.tocFile);
   }

   @Override
   public void replaceLocalWsdl(String var1, File var2) {
      if (var2.canRead()) {
         this.lock.writeLock().lock();

         try {
            KnownWsdlsContentsList var3 = this.getTableOfContents();
            if (var3.contains(var1)) {
               File var4 = this.storageDirectory.getFile(var1);
               Files.copy(var2, var4);
            }
         } catch (IOException var8) {
            this.deleteFromTableOfContents(var1);
            this.deleteFile(var1);
         } finally {
            this.lock.writeLock().unlock();
         }
      }
   }

   private void deleteFromTableOfContents(String var1) {
      try {
         KnownWsdlsContentsList var2 = this.getTableOfContents();
         if (var2.remove(var1)) {
            this.writeTableOfContentsToFile();
         }
      } catch (IOException var3) {
         ErrorLog.internal(this.getClass(), null, new Exception("The local cache of known SFDC WSDLs is corrupted."));
      }
   }

   private void deleteFile(String var1) {
      File var2 = this.storageDirectory.getFile(var1);
      if (var2.exists()) {
         boolean var3 = var2.delete();
         if (!var3) {
            ErrorLog.internal(this.getClass(), null, new Exception("The local cache of known SFDC WSDLs is corrupted."));
         }
      }
   }

   @Override
   public void addRemoteWsdl(String var1, LoginCredentials var2) {
      Preconditions.checkNotNull(var1, "url");
      Preconditions.checkNotNull(var2, "credentials");
      this.lock.writeLock().lock();

      try {
         this.ensureLocationExists();
         this.addToTableOfContents(var1, var2);
      } catch (IOException var7) {
         ErrorLog.log(FileBasedKnownWsdlsStore.class, "Failed to add known SFDC WSDL URL to the client's local store.", var7);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   private void ensureLocationExists() throws IOException {
      boolean var1 = this.storageDirectory.createOnDisk();
      if (!var1) {
         throw new IOException("Failed to create the folder \"" + this.storageDirectory.getAbsolutePath() + "\".");
      }
   }

   @Override
   public void updateCredentials(String var1, LoginCredentials var2) {
      this.lock.writeLock().lock();

      try {
         KnownWsdlsContentsList var3 = this.getTableOfContents();
         if (var3.contains(var1)) {
            this.addToTableOfContents(var1, var2);
         }
      } catch (IOException var7) {
         ErrorLog.log(this.getClass(), "Failed to update the credentials for the WSDL " + var1, var7);
      } finally {
         this.lock.writeLock().unlock();
      }
   }

   private abstract static class AbstractKnownWsdl implements KnownWsdl {
      private final String name;
      private final List<LoginCredentials> knownCredentials;

      public AbstractKnownWsdl(String var1, List<LoginCredentials> var2) {
         this.name = var1;
         this.knownCredentials = var2;
      }

      @Override
      public final String getName() {
         return this.name;
      }

      @Override
      public final List<LoginCredentials> getListOfCredentials() {
         return Lists.newArrayList(this.knownCredentials);
      }

      @Override
      public final String toString() {
         return this.name;
      }
   }

   private static final class LocalWsdl extends FileBasedKnownWsdlsStore.AbstractKnownWsdl {
      private final File file;

      public LocalWsdl(File var1, List<LoginCredentials> var2) {
         super(var1.getName(), var2);
         this.file = var1;
      }

      @Override
      public void addToProjectFiles(FileStore var1) throws FileStoreException {
         var1.addFile(this.file, this.file.getName(), FileCollisionResolver.REPLACE_EXISTING);
      }

      @Override
      public void uploadToServer() {
         IntegrationServer var1 = IntegrationServer.getInstance();
         WsdlProvider var2 = var1.getServerCall(WsdlProvider.class);
         DummyFileStore var3 = new DummyFileStore(new File(""));
         WsdlUploader var4 = new WsdlUploader(var2, var3);
         var4.upload(this.file, new CallbackResultAdapter() {});
      }
   }

   private static class RemoteWsdl extends FileBasedKnownWsdlsStore.AbstractKnownWsdl {
      public RemoteWsdl(String var1, List<LoginCredentials> var2) {
         super(var1, var2);
      }

      @Override
      public void addToProjectFiles(FileStore var1) {
      }

      @Override
      public void uploadToServer() {
         String var1 = this.getName();
         WsdlUtils.uploadRemoteWsdlSynchronously(var1, new UploadServerFileCallback() {
            @Override
            public void caught(Throwable var1) {
            }

            @Override
            public void cancelled() {
            }

            @Override
            public void fileWasUploaded(ServerFileUploadResult var1) {
            }
         });
      }
   }
}
