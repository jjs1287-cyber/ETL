package org.etlforge.integration.client.connector.schema;

import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.connector.serverapi.MultipleSchemas;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.XsdNamingStrategy;
import org.etlforge.integration.data.structure.CallStructureSchema;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdUploadManager;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;

final class MultipleSchemasProcessor {
   private final AbstractConnectorCall function;
   private final MultipleSchemas schemas;
   private final XsdNamingStrategy xsdNamer;
   private final FileUploadApi fileUploadApi;
   private final FileStore fileStore;
   private final MultipleSchemasProcessor.ProcessedFiles processedFiles = new MultipleSchemasProcessor.ProcessedFiles();
   private final File rootFolder = ApplicationTempFolder.newUniqueFolder(true);

   public MultipleSchemasProcessor(AbstractConnectorCall var1, MultipleSchemas var2, FileUploadApi var3, FileStore var4) {
      this.function = var1;
      this.schemas = var2;
      this.fileUploadApi = var3;
      this.fileStore = var4;
      this.xsdNamer = var1.getXsdNamingStrategy();
   }

   public void process() throws Exception {
      if (this.function.hasRequestTransformation()) {
         this.processRequest();
      }

      this.processResponse();
      this.updateFunction();
      this.uploadFiles();
   }

   private void processRequest() throws Exception {
      String var1 = this.schemas.getRequestFileName();
      String var2 = this.xsdNamer.getRequestName(this.function);
      this.processFile(var1, var2);
   }

   private void processResponse() throws Exception {
      String var1 = this.schemas.getResponseFileName();
      String var2 = this.xsdNamer.getResponseName(this.function);
      this.processFile(var1, var2);
   }

   private void processFile(String var1, String var2) throws Exception {
      if (!this.processedFiles.isProcessed(var1)) {
         this.processedFiles.add(var1, var2);
         CompressedAndBase64EncodedString var3 = this.schemas.getContent(var1);
         File var4 = new File(this.rootFolder, var2);
         var3.inflateTo(var4);
         SchemaRenameFileParser var5 = new SchemaRenameFileParser(this.function, this.xsdNamer, this.schemas, this.processedFiles);
         var5.process(var4);

         for (Entry var7 : var5.newReferences()) {
            this.processFile((String)var7.getKey(), (String)var7.getValue());
         }
      }
   }

   private void updateFunction() {
      String var1 = this.function.hasRequestTransformation() ? this.getRequestFileName() : null;
      String var2 = this.getResponseFileName();
      synchronized (this.function.getDataLock()) {
         if (this.function.hasRequestTransformation()) {
            this.function.setInputSchema(new CallStructureSchema(var1, this.schemas.getRequestRootName()));
         }

         this.function.setOutputSchema(new CallStructureSchema(var2, this.schemas.getResponseRootName()));
      }
   }

   private String getRequestFileName() {
      return this.processedFiles.getNewName(this.schemas.getRequestFileName());
   }

   private String getResponseFileName() {
      return this.processedFiles.getNewName(this.schemas.getResponseFileName());
   }

   private void uploadFiles() throws Exception {
      if (this.function.hasRequestTransformation()) {
         String var1 = this.getRequestFileName();
         String var2 = this.getResponseFileName();
         if (!var1.equals(var2)) {
            File var3 = new File(this.rootFolder, this.getRequestFileName());
            this.uploadFile(var3);
         }
      }

      File var4 = new File(this.rootFolder, this.getResponseFileName());
      this.uploadFile(var4);
   }

   private void uploadFile(File var1) throws Exception {
      XsdUploadManager var2 = new XsdUploadManager(this.fileUploadApi, new XsdFileStore(this.fileStore));
      var2.setRequiredFilesProvider(new ImportedFilesProvider());
      var2.upload(null, var1.getAbsolutePath());
      if (!var2.isSuccess()) {
         throw new Exception("Could not register the XSD files on the server.");
      }
   }

   static class ProcessedFiles {
      private final Map<String, String> newNames = Maps.newHashMap();

      public boolean isProcessed(String var1) {
         return this.newNames.containsKey(var1);
      }

      public void add(String var1, String var2) {
         this.newNames.put(var1, var2);
      }

      public String getNewName(String var1) {
         String var2 = this.newNames.get(var1);
         assert var2 != null;
         return var2;
      }
   }
}
