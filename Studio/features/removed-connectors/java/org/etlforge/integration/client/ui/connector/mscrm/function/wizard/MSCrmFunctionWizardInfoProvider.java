package org.etlforge.integration.client.ui.connector.mscrm.function.wizard;

import com.google.common.base.Preconditions;
import java.io.File;
import java.util.List;
import org.etlforge.integration.client.connector.mscrm.serverapi.MSCrmDesignInfoProvider;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.client.connector.serverapi.GetSchemasCallback;
import org.etlforge.integration.client.connector.serverapi.ListObjectsCallback;
import org.etlforge.integration.client.connector.serverapi.SingleSchemas;
import org.etlforge.integration.client.ui.connector.mscrm.ConnectorCallInfoProviderAdapter;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectCache;
import org.etlforge.integration.client.ui.connector.mscrm.MSCrmObjectLookupFactory;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.integration.mscrm.metadata.MSCrmSchemaBuilder;

public final class MSCrmFunctionWizardInfoProvider implements ConnectorCallInfoProvider {
   private final MSCrmDesignInfoProvider infoProvider;
   private final MSCrmObjectLookupFactory objectLookupFactory;

   public MSCrmFunctionWizardInfoProvider(MSCrmDesignInfoProvider var1, MSCrmObjectCache var2) {
      this.infoProvider = var1;
      this.objectLookupFactory = new MSCrmObjectLookupFactory(var2, var1);
   }

   public MSCrmObjectLookupFactory getObjectLookupFactory() {
      return this.objectLookupFactory;
   }

   @Override
   public void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4) {
      this.infoProvider.uploadFile(var1, var2, var3, var4);
   }

   @Override
   public void getObjectList(ConnectorEndpoint var1, List<ConnectorObject> var2, ListObjectsCallback var3) {
      ConnectorCallInfoProviderAdapter var4 = new ConnectorCallInfoProviderAdapter(this.infoProvider);
      var4.getObjectList(var1, var2, var3);
   }

   @Override
   public void getSingleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, GetSchemasCallback var3) {
      try {
         Preconditions.checkArgument(
            var2.size() == 2 && var2.get(0) instanceof FunctionType, "Expected two objects in the path: The FunctionType and object name."
         );
         FunctionType var4 = (FunctionType)var2.get(0);
         String var5 = ((ConnectorObject)var2.get(1)).getName();
         MSCrmObjectLookup var6 = this.objectLookupFactory.newLookup((MSCrmEndpoint)var1);
         MSCrmObject var7 = var6.describeObject(var5);
         MSCrmSchemaBuilder var8 = new MSCrmSchemaBuilder(var4, var7);
         SchemaData var9 = var8.buildRequestSchema();
         SchemaData var10 = var8.buildResponseSchema();
         var3.handleSchemas(new SingleSchemas(var9, var10));
      } catch (Exception var11) {
         var3.caught(var11);
      }
   }

   @Override
   public void getMultipleSchemas(ConnectorEndpoint var1, List<ConnectorObject> var2, boolean var3, GetSchemasCallback var4) {
      throw new UnsupportedOperationException();
   }
}
