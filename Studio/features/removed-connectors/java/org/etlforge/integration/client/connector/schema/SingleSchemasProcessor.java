package org.etlforge.integration.client.connector.schema;

import java.util.Arrays;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileCollisionResolution;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.application.filestore.FileStoreMemento;
import org.etlforge.integration.client.connector.serverapi.SingleSchemas;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.XsdNamingStrategy;
import org.etlforge.integration.data.structure.CallStructureSchema;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdUploadManager;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

final class SingleSchemasProcessor {
   private final AbstractConnectorCall function;
   private final FileUploadApi uploadApi;
   private final FileStore fileStore;
   private final FileStoreMemento fileStoreMemento;
   private FileStoreFile requestXsd;
   private FileStoreFile responseXsd;

   public SingleSchemasProcessor(AbstractConnectorCall var1, FileUploadApi var2, FileStore var3, boolean var4) {
      this.function = var1;
      this.uploadApi = var2;
      this.fileStore = var3;
      this.fileStoreMemento = var4 ? this.createFileStoreMemento() : FileStoreMemento.empty();
   }

   private FileStoreMemento createFileStoreMemento() {
      try {
         String var1 = this.function.getOutputSchema().getSchemaName();
         if (this.function.hasRequestTransformation()) {
            String var2 = this.function.getInputSchema().getSchemaName();
            return new FileStoreMemento(this.fileStore, Arrays.asList(var2, var1));
         } else {
            return new FileStoreMemento(this.fileStore, Arrays.asList(var1));
         }
      } catch (FileStoreException var4) {
         ErrorLog.internal(this.getClass(), "Could not create the FileStoreMemento before updating function XSDs.", var4);
         return FileStoreMemento.empty();
      }
   }

   public DownloadedSchemasProcessor.Result process(SingleSchemas var1) throws Exception {
      boolean var2 = false;

      try {
         DownloadedSchemasProcessor.Result var3 = this.processImpl(var1);
         var2 = true;
         return var3;
      } finally {
         this.cleanup(var2);
      }
   }

   public DownloadedSchemasProcessor.Result processImpl(SingleSchemas var1) throws Exception {
      XsdNamingStrategy var2 = this.function.getXsdNamingStrategy();
      String var3 = var2.getRequestName(this.function);
      String var4 = var2.getResponseName(this.function);
      boolean var5 = this.saveInFileStore(var1, var3, var4);
      this.uploadToServer();
      this.updateFunction(var1, var3, var4);
      return new DownloadedSchemasProcessor.Result(var5);
   }

   private boolean saveInFileStore(SingleSchemas var1, String var2, String var3) throws Exception {
      SingleSchemasProcessor.FileStoreCollisionResolver var4 = new SingleSchemasProcessor.FileStoreCollisionResolver();
      boolean var5 = this.function.hasRequestTransformation() && this.fileStore.getFile(var2) == null;
      if (this.function.hasRequestTransformation()) {
         this.requestXsd = this.fileStore.addFile(var2, var1.getRequest(), var4);
      }

      boolean var6 = this.fileStore.getFile(var3) == null;
      this.responseXsd = this.fileStore.addFile(var3, var1.getResponse(), var4);
      return var5 || var6 || var4.fileWasReplaced();
   }

   private void uploadToServer() throws Exception {
      if (this.function.hasRequestTransformation()) {
         String var1 = this.requestXsd.getPhysicalRepresentation().getAbsolutePath();
         String var2 = this.responseXsd.getPhysicalRepresentation().getAbsolutePath();
         if (!var1.equals(var2)) {
            this.uploadXsdToServer(this.requestXsd);
         }
      }

      this.uploadXsdToServer(this.responseXsd);
   }

   private void uploadXsdToServer(FileStoreFile var1) throws Exception {
      XsdFileStore var2 = new XsdFileStore(this.fileStore);
      XsdUploadManager var3 = new XsdUploadManager(this.uploadApi, var2);
      var3.upload(null, var1.getPhysicalRepresentation().getAbsolutePath());
      if (!var3.isSuccess()) {
         throw new Exception("Could not register the XSD files on the server.");
      }
   }

   private void updateFunction(SingleSchemas var1, String var2, String var3) {
      synchronized (this.function.getDataLock()) {
         if (this.function.hasRequestTransformation()) {
            this.function.setInputSchema(new CallStructureSchema(var2, var1.getRequestRootName()));
         }

         this.function.setOutputSchema(new CallStructureSchema(var3, var1.getResponseRootName()));
      }
   }

   private void cleanup(boolean var1) {
      if (!var1) {
         this.restoreFileStore();
      }

      this.fileStoreMemento.forget();
   }

   private void restoreFileStore() {
      try {
         this.fileStoreMemento.restore();
      } catch (FileStoreException var2) {
         ErrorLog.internal(this.getClass(), "Could not restore the FileStoreMemento after failed attempt to update function XSDs.", var2);
      }
   }

   private static class FileStoreCollisionResolver implements FileCollisionResolver {
      private final FileCollisionResolver delegate = FileCollisionResolver.REPLACE_EXISTING;
      private boolean replacedFile;

      private FileStoreCollisionResolver() {
      }

      @Override
      public FileCollisionResolution resolve(FileStoreFile var1, String var2) {
         this.replacedFile = true;
         return this.delegate.resolve(var1, var2);
      }

      @Override
      public FileCollisionResolution resolve(FileStoreFile var1, byte[] var2) {
         this.replacedFile = true;
         return this.delegate.resolve(var1, var2);
      }

      public boolean fileWasReplaced() {
         return this.replacedFile;
      }
   }
}
