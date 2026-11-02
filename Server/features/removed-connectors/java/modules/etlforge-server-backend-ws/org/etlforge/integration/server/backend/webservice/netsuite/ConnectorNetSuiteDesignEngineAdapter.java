package org.etlforge.integration.server.backend.webservice.netsuite;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.server.backend.webservice.connector.ConnectorEngineExecutionWrapperFactory;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.connector.WsNameValuePair;
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;
import org.etlforge.integration.server.engine.connector.ConnectorEngineExecutionWrapper;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;
import org.etlforge.util.zip.ZipUtils;

public class ConnectorNetSuiteDesignEngineAdapter {
   private static final WsStatus OKStatus = new WsStatus();
   private static final String SEARCHCUSTOMFIELDPREFIX = "(CF)";

   public static WsNetSuiteObjectCustomFieldsNew getNetSuiteCustomFieldsNew(WsEndpointParameters var0, boolean var1, String var2) {
      ConnectorEngineExecutionWrapper var3 = getConnectorEngineExecutionWrapper(var0);
      NetSuiteObjectCustomFields var4 = var3.getCustomFields(var1, var2);
      Map var5 = var4.getNetSuiteObjectsCustomFields();
      String[] var6 = new String[var5.size()];
      WsNetSuiteCustomFieldsNew[] var7 = new WsNetSuiteCustomFieldsNew[var5.size()];
      int var8 = 0;

      for (Object __etlItem31 : var5.entrySet()) {
         Entry var10 = (Entry) __etlItem31;
         var6[var8] = (String)var10.getKey();
         var7[var8] = convert(((NetSuiteCustomFields)var10.getValue()).getCustomFields());
         var8++;
      }

      return new WsNetSuiteObjectCustomFieldsNew(OKStatus, var4.getCustomObjectId(), var6, var7);
   }

   private static WsNetSuiteCustomFieldsNew convert(List<NetSuiteCustomField> var0) {
      WsNetSuiteCustomFieldNew[] var1 = new WsNetSuiteCustomFieldNew[var0.size()];
      int var2 = 0;

      for (NetSuiteCustomField var4 : var0) {
         var1[var2++] = new WsNetSuiteCustomFieldNew(var4.getCustomLabel(), var4.getInternalId(), var4.getFieldType(), var4.getScriptId());
      }

      return new WsNetSuiteCustomFieldsNew(var1);
   }

   public static WsNetSuiteDefinitionContents getNetSuiteDefinitionContents(WsEndpointParameters var0) throws IOException {
      ConnectorEngineExecutionWrapper var1 = getConnectorEngineExecutionWrapper(var0);
      return convert(var1.getNetSuiteDefinitionContents());
   }

   private static WsNetSuiteDefinitionContents convert(NetSuiteDefinitionContents var0) throws IOException {
      String var1 = ZipUtils.deflateAndBase64EncodeString(var0.getConfigXml());
      String var2 = var0.getNetSuiteWsdlContents().getNetsuiteWsdlKey();
      Set var3 = var0.getNetSuiteWsdlContents().getXsdSchemaKeys();
      String[] var4 = new String[var3.size()];
      String[] var5 = new String[var3.size()];
      int var6 = 0;

      for (Object __etlItem64 : var3) {
         String var8 = (String) __etlItem64;
         var4[var6] = var8;
         var5[var6] = ZipUtils.deflateAndBase64EncodeString(var0.getNetSuiteWsdlContents().getXsdSchema(var8));
         var6++;
      }

      return new WsNetSuiteDefinitionContents(OKStatus, var1, var2, var4, var5);
   }

   public static WsNetSuiteSavedSearch getSavedSearch(WsEndpointParameters var0, String var1) {
      ConnectorEngineExecutionWrapper var2 = getConnectorEngineExecutionWrapper(var0);
      Map var3 = var2.getSavedSearch(var1);
      WsNameValuePair[] var4 = new WsNameValuePair[var3.size()];
      int var5 = 0;

      for (Object __etlItem79 : var3.entrySet()) {
         Entry var7 = (Entry) __etlItem79;
         var4[var5++] = new WsNameValuePair((String)var7.getKey(), (String)var7.getValue());
      }

      return new WsNetSuiteSavedSearch(OKStatus, var4);
   }

   private static ConnectorEngineExecutionWrapper getConnectorEngineExecutionWrapper(WsEndpointParameters var0) {
      return ConnectorEngineExecutionWrapperFactory.obtainExecutionWrapper(true, var0, false);
   }
}
