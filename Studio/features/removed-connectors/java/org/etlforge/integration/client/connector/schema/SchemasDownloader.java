package org.etlforge.integration.client.connector.schema;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.server.ResultProducingServerCallback;

public final class SchemasDownloader implements Callable<Schemas> {
   private final ConnectorCallInfoProvider infoProvider;
   private final SchemaRequestData requestData;

   public SchemasDownloader(ConnectorCallInfoProvider var1, SchemaRequestData var2) {
      this.infoProvider = Preconditions.checkNotNull(var1, "infoProvider");
      this.requestData = Preconditions.checkNotNull(var2, "requestData");
   }

   @Nullable
   public Schemas call() throws Exception {
      SchemasDownloader.Callback var1 = new SchemasDownloader.Callback();
      this.requestData.askServerForSchemas(this.infoProvider, var1);
      return var1.awaitResult().orNull();
   }

   private static class Callback extends ResultProducingServerCallback<Schemas> implements GetSchemasCallback {
      private Callback() {
      }

      @Override
      public void handleSchemas(Schemas var1) {
         this.set(var1);
      }
   }
}
