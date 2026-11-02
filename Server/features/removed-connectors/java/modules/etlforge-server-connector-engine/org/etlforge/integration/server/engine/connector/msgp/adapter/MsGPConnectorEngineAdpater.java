package org.etlforge.integration.server.engine.connector.msgp.adapter;

import com.google.common.base.Strings;
import java.util.ArrayList;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.msgp.ConnectorEndpointMsGP;
import org.etlforge.io.FileUtils;

public class MsGPConnectorEngineAdpater {
   final MsGPConnectorEndpointAdapter endpointAdapter;

   public MsGPConnectorEngineAdpater(ConnectorEndpointMsGP var1) {
      this.endpointAdapter = var1.getMsGPEndpointAdapter();
   }

   public ConnectorObjectList getConnectorObjectList(String var1, String var2) {
      String var4 = "<modules><operation>" + var1 + "</operation></modules>";
      MsGPServiceHandler var5 = new MsGPServiceHandler("Modules", this.endpointAdapter.getWsBaseUrl(), this.endpointAdapter.getAuthInfo());
      String var6 = var5.callWs(var4, false);
      ArrayList var7 = MsGPModuleListParser.parseModuleListResponse(var6);
      ConnectorObjectList var8 = new ConnectorObjectList(true, "Module List");

      for (Object __etlItem25 : var7) {
         MsGPModuleListParser.MsGPModuleSchema var10 = (MsGPModuleListParser.MsGPModuleSchema) __etlItem25;
         String var11 = var10.internalname;
         String var12 = var10.displayname == null ? var10.internalname : var10.displayname;
         if (!Strings.isNullOrEmpty(var10.msdnurl)) {
            var12 = var12 + ". check " + var10.msdnurl;
         }

         var8.addConnectorObject(var11, var12);
      }

      return var8;
   }

   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(boolean var1, String var2) {
      String var4 = "<specificschema><internalname>" + var2 + "</internalname></specificschema>";
      MsGPServiceHandler var5 = new MsGPServiceHandler("SpecificSchema", this.endpointAdapter.getWsBaseUrl(), this.endpointAdapter.getAuthInfo());
      String var6 = var5.callWs(var4, false);
      MsGPXsdSchemas var7 = new MsGPXsdSchemas();

      try {
         return var7.obtainSchemas(var6);
      } catch (Exception var9) {
         throw new ConnectorResponseProcessRuntimeException(var9);
      }
   }

   public void executeConnectorFileUtf8(String var1, String var2, String var3) {
      String var4 = FileUtils.getPostfixFilenameXml(var2, "_wsres");
      MsGPServiceHandler var5 = new MsGPServiceHandler(var1, this.endpointAdapter.getWsBaseUrl(), this.endpointAdapter.getAuthInfo());
      var5.callWsFile(var2, var4);
      MsGPDocumentExtractor var6 = new MsGPDocumentExtractor();
      var6.extractDocumentFile(var4, var3);
   }
}
