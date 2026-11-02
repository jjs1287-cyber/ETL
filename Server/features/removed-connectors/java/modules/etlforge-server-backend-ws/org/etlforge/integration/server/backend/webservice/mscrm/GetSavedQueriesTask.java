package org.etlforge.integration.server.backend.webservice.mscrm;

import java.io.IOException;
import java.util.List;
import org.etlforge.integration.mscrm.query.FetchXml;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEngineMsCrm;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.zip.ZipUtils;

final class GetSavedQueriesTask extends DesignTask<WsSavedQueries> {
   private final String objectName;

   public GetSavedQueriesTask(String var1, String var2, WsEndpointParameters var3, String var4) {
      super("Get Saved Queries", var1, var2, var3);
      this.objectName = ArgCheck.checkNotEmpty(var4, "objectName");
   }

   protected WsSavedQueries runTask(ConnectorEngineMsCrm var1) throws Exception {
      List var2 = var1.getSavedQueries(this.objectName);
      WsFetchXml[] var3 = this.serializeQueries(var2);
      return new WsSavedQueries(new WsStatus(0, ""), var3);
   }

   private WsFetchXml[] serializeQueries(List<FetchXml> var1) throws IOException {
      WsFetchXml[] var2 = new WsFetchXml[var1.size()];
      int var3 = 0;

      for (FetchXml var5 : var1) {
         String var6 = ZipUtils.deflateAndBase64EncodeString(var5.getXml());
         WsFetchXml var7 = new WsFetchXml(var5.getName(), var6);
         var2[var3] = var7;
         var3++;
      }

      return var2;
   }
}
