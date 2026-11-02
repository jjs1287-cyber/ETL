package org.etlforge.integration.server.engine.connector.msax.adapter;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.server.engine.connector.framework.ConnectorObjectList;
import org.etlforge.integration.server.engine.connector.framework.ConnectorXsdSchemaFiles;
import org.etlforge.integration.server.engine.connector.msax.ConnectorEndpointMsAX;
import org.etlforge.integration.server.engine.connector.msax.rest.MsAXExecuteOperationResponseStatusCheck;
import org.etlforge.integration.server.engine.connector.msax.rest.MsAXOperationListResponseParser;
import org.etlforge.integration.server.engine.connector.msax.rest.MsAXOperationSchemaResponseParser;
import org.etlforge.integration.server.engine.connector.msax.rest.MsAXRequestBodyExecution;
import org.etlforge.integration.server.engine.connector.msax.rest.MsAxRequestBodyDesign;
import org.etlforge.integration.server.engine.connector.util.ContentExtractorByte;
import org.etlforge.io.FileUtils;
import org.etlforge.util.csv.CSVParams;
import org.etlforge.util.csv.CsvUtils;

public class ConnectorEngineMsAXAdapter {
   public static final String operations = "operations";
   public static final String operationschemas = "operationschemas";
   public static final String executeoperation = "executeoperation";
   public static final CSVParams params = new CSVParams(':', '"');
   final ConnectorEndpointMsAXAdapter adapter;

   public ConnectorEngineMsAXAdapter(ConnectorEndpointMsAX var1) {
      this.adapter = var1.getConnectorEndpointMsAXAdapter();
   }

   public static ImmutableList<String> parseServiceAndOperation(String var0) {
      return Strings.isNullOrEmpty(var0) ? ImmutableList.of() : CsvUtils.toValues(var0, params);
   }

   public ConnectorObjectList getConnectorObjectList(String var1, String var2) {
      List var3 = this.getConnectorObjectListImpl(var1, var2);
      ConnectorObjectList var4 = new ConnectorObjectList(true, "");

      for (Object __etlItem38 : var3) {
         MsAXOperation var6 = (MsAXOperation) __etlItem38;
         String[] var7 = new String[]{var6.getServiceName(), var6.getOperationName()};
         String var8 = CsvUtils.toRecord(var7, params);
         var4.addConnectorObject(var8, "");
      }

      return var4;
   }

   private List<MsAXOperation> getConnectorObjectListImpl(String var1, String var2) {
      String var3 = MsAxRequestBodyDesign.operationRequestBody(var1, var2);
      MsAXServiceHandler var4 = new MsAXServiceHandler(this.adapter.getServiceUrl(var1), this.adapter.getAuthInfo());
      String var5 = var4.callWs(var3);
      return MsAXOperationListResponseParser.parseOperationListResponse(var5);
   }

   public ConnectorXsdSchemaFiles getConnectorSchemaFiles(String var1, boolean var2, String var3, String var4, String var5) {
      String var6 = MsAxRequestBodyDesign.operationSchemaRequestBody(var1, var2, var3, var4, var5);
      MsAXServiceHandler var7 = new MsAXServiceHandler(this.adapter.getServiceUrl(var1), this.adapter.getAuthInfo());
      String var8 = var7.callWs(var6);
      return MsAXOperationSchemaResponseParser.parseOperationSchemaResponse(var8);
   }

   public void executeConnectorFileUtf8(String var1, String var2, String var3, String var4, String var5, String var6) {
      String var7 = FileUtils.getPostfixFilenameXml(var5, "_body");
      MsAXRequestBodyExecution.executeOperationRequestBody(var1, var2, var3, var4, var5, var7);
      String var8 = FileUtils.getPostfixFilenameXml(var6, "_axres");
      MsAXServiceHandler var9 = new MsAXServiceHandler(this.adapter.getServiceUrl(var1), this.adapter.getAuthInfo());
      var9.callWsFile(var7, var8);
      MsAXExecuteOperationResponseStatusCheck.checkExecuteOperationResponse(var8);
      ContentExtractorByte.extract(var8, var6);
   }
}
