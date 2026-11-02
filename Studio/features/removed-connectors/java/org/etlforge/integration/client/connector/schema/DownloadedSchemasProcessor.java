package org.etlforge.integration.client.connector.schema;

import com.google.common.base.Objects;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.connector.serverapi.MultipleSchemas;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.connector.serverapi.SingleSchemas;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public final class DownloadedSchemasProcessor {
   private final FileStore fileStore;
   private final FileUploadApi uploadApi;
   private final AbstractConnectorCall function;

   public DownloadedSchemasProcessor(AbstractConnectorCall var1, FileStore var2, FileUploadApi var3) {
      this.function = var1;
      this.fileStore = var2;
      this.uploadApi = var3;
   }

   public DownloadedSchemasProcessor.Result process(Schemas var1) throws Exception {
      if (var1 instanceof SingleSchemas) {
         return this.processSingleSchemas((SingleSchemas)var1);
      } else if (var1 instanceof MultipleSchemas) {
         return this.processMultipleSchemas((MultipleSchemas)var1);
      } else {
         throw new AssertionError();
      }
   }

   private DownloadedSchemasProcessor.Result processSingleSchemas(SingleSchemas var1) throws Exception {
      boolean var2 = false;
      SingleSchemasProcessor var3 = new SingleSchemasProcessor(this.function, this.uploadApi, this.fileStore, var2);
      return var3.process(var1);
   }

   private DownloadedSchemasProcessor.Result processMultipleSchemas(MultipleSchemas var1) throws Exception {
      MultipleSchemasProcessor var2 = new MultipleSchemasProcessor(this.function, var1, this.uploadApi, this.fileStore);
      var2.process();
      return new DownloadedSchemasProcessor.Result(true);
   }

   public static final class Result {
      private final boolean fileStoreWasUpdated;

      Result(boolean var1) {
         this.fileStoreWasUpdated = var1;
      }

      public boolean wasFileStoreUpdated() {
         return this.fileStoreWasUpdated;
      }

      @Override
      public String toString() {
         return Objects.toStringHelper(this).add("fileStoreWasUpdated", this.fileStoreWasUpdated).toString();
      }
   }
}
