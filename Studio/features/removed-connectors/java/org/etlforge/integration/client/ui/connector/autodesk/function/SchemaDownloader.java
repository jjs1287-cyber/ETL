package org.etlforge.integration.client.ui.connector.autodesk.function;

import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.client.connector.serverapi.Schemas;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.data.connector.AutoDeskFunction;

@ParametersAreNonnullByDefault
public final class SchemaDownloader implements Callable<Schemas> {
   private final ConnectorCallInfoProvider infoProvider;
   private final SchemaRequestData requestData;

   public SchemaDownloader(ConnectorCallInfoProvider var1, AutoDeskFunction var2) {
      this(var1, new SchemaRequestData(var2));
   }

   public SchemaDownloader(ConnectorCallInfoProvider var1, SchemaRequestData var2) {
      this.infoProvider = Preconditions.checkNotNull(var1, "infoProvider");
      this.requestData = Preconditions.checkNotNull(var2, "requestData");
   }

   @Nullable
   public Schemas call() throws Exception {
      SchemaDownloader.Callback var1 = new SchemaDownloader.Callback();
      this.requestData.sendRequest(this.infoProvider, var1);
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
