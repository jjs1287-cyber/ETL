package org.etlforge.integration.server.backend.webservice.mscrm;

import org.etlforge.integration.etlforgeconnector.SchemaData;
import org.etlforge.integration.mscrm.metadata.MSCrmMetaDataException;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.MSCrmObjectLookup;
import org.etlforge.integration.mscrm.query.ResponseSchemaBuilder;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEngineMsCrm;
import org.etlforge.system.ArgCheck;

final class GetQueryResponseSchemaTask extends DesignTask<WsQueryResponseSchema> {
   private final String fetchXml;

   public GetQueryResponseSchemaTask(String var1, String var2, WsEndpointParameters var3, String var4) {
      super("Get Query Response Schema", var1, var2, var3);
      this.fetchXml = ArgCheck.checkNotEmpty(var4, "fetchXml");
   }

   protected WsQueryResponseSchema runTask(ConnectorEngineMsCrm var1) throws Exception {
      GetQueryResponseSchemaTask.ObjectLookupImpl var2 = new GetQueryResponseSchemaTask.ObjectLookupImpl(var1);
      SchemaData var3 = ResponseSchemaBuilder.buildSchema(this.fetchXml, var2);
      return new WsQueryResponseSchema(successStatus(), new WsSchema(var3.getContents().getCompressedRepresentation(), var3.getRootName()));
   }

   private static class ObjectLookupImpl implements MSCrmObjectLookup {
      private final ConnectorEngineMsCrm engine;

      public ObjectLookupImpl(ConnectorEngineMsCrm var1) {
         this.engine = var1;
      }

      @Override
      public MSCrmObject describeObject(String var1) throws MSCrmMetaDataException {
         return this.engine.getDetailedMetaData(var1);
      }
   }
}
