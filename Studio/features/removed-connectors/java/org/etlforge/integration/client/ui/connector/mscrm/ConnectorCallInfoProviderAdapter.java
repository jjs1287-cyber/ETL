package org.etlforge.integration.client.ui.connector.mscrm;

import com.google.common.collect.ImmutableList;
import java.io.File;
import java.util.List;
import org.etlforge.integration.client.connector.mscrm.serverapi.GetObjectDescriptionsCallback;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.client.connector.serverapi.ListObjectsCallback;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.mscrm.metadata.BasicEntityDetails;

public final class ConnectorCallInfoProviderAdapter implements ConnectorCallInfoProvider {
   private final MSCrmDesignInfoProvider impl;

   public ConnectorCallInfoProviderAdapter(MSCrmDesignInfoProvider var1) {
      this.impl = var1;
   }

   @Override
   public void getObjectList(ConnectorEndpoint var1, List<ConnectorObject> var2, ListObjectsCallback var3) {
      this.impl.getObjectDescriptions((MSCrmEndpoint)var1, new ConnectorCallInfoProviderAdapter.CallbackAdapter(var3));
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void getSingleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, GetSchemasCallback var3) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void getMultipleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, boolean var3, GetSchemasCallback var4) {
      throw new UnsupportedOperationException();
   }

   private static class CallbackAdapter implements GetObjectDescriptionsCallback {
      private final ListObjectsCallback receiver;

      public CallbackAdapter(ListObjectsCallback var1) {
         this.receiver = var1;
      }

      @Override
      public void gotDescriptions(ImmutableList<BasicEntityDetails> var1) {
         this.receiver.handleObjectList(var1);
      }

      @Override
      public void caught(Throwable var1) {
         this.receiver.caught(var1);
      }

      @Override
      public void cancelled() {
         this.receiver.cancelled();
      }
   }
}
