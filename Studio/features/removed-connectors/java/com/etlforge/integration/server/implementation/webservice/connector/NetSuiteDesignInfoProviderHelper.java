package com.etlforge.integration.server.implementation.webservice.connector;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.zip.DataFormatException;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.integration.etlforgeconnector.UserCredentials;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNameValuePair;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteCustomFieldNew;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteCustomFieldsNew;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteDefinitionContents;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteObjectCustomFieldsNew;
import org.etlforge.integration.server.backend.webservice.netsuite.client.WsNetSuiteSavedSearch;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;
import org.etlforge.util.zip.ZipUtils;

public class NetSuiteDesignInfoProviderHelper {
   static NetSuiteDefinitionContents convert(WsNetSuiteDefinitionContents var0) {
      try {
         return convertImpl(var0);
      } catch (DataFormatException var2) {
         var2.printStackTrace();
         throw new RuntimeException(var2);
      }
   }

   static NetSuiteDefinitionContents convertImpl(WsNetSuiteDefinitionContents var0) throws DataFormatException {
      String var1 = ZipUtils.base64DecodeAndInflateString(var0.getConfigXml());
      NetSuiteDefinitionContents var2 = new NetSuiteDefinitionContents(var0.getNetsuiteWsdlKey(), var1);

      for (int var3 = 0; var3 < var0.getWsdlConnentsKey().length; var3++) {
         String var4 = var0.getWsdlConnentsKey()[var3];
         String var5 = ZipUtils.base64DecodeAndInflateString(var0.getWsdlConnents()[var3]);
         var2.setXsdSchema(var4, var5);
      }

      return var2;
   }

   static NetSuiteObjectCustomFields convert(WsNetSuiteObjectCustomFieldsNew var0) {
      String var1 = var0.getCustomObjectId();
      NetSuiteObjectCustomFields var2 = Strings.isNullOrEmpty(var1) ? new NetSuiteObjectCustomFields() : new NetSuiteObjectCustomFields(var1);
      int var3 = var0.getCustomFieldCategory().length;

      for (int var4 = 0; var4 < var3; var4++) {
         NetSuiteCustomFields var5 = convert(var0.getCustomFields()[var4]);
         var2.add(var0.getCustomFieldCategory()[var4], var5.getCustomFields());
      }

      return var2;
   }

   static NetSuiteCustomFields convert(WsNetSuiteCustomFieldsNew var0) {
      NetSuiteCustomFields var1 = new NetSuiteCustomFields();
      int var2 = var0.getCustomFieldList().length;

      for (int var3 = 0; var3 < var2; var3++) {
         WsNetSuiteCustomFieldNew var4 = var0.getCustomFieldList()[var3];
         NetSuiteCustomField var5 = new NetSuiteCustomField(var4.getCustomLabel(), var4.getInternalId(), var4.getScriptId(), var4.getFieldType());
         var1.add(var5);
      }

      return var1;
   }

   static Map<String, String> convertToSavedSearch(WsNetSuiteSavedSearch var0) {
      HashMap var1 = Maps.newHashMap();

      for (WsNameValuePair var5 : var0.getSavedSeaches()) {
         var1.put(var5.getName(), var5.getValue());
      }

      return var1;
   }

   static final WsEndpointParameters createWsEndpointParams(String var0, String var1, ConnectorEndpoint var2) {
      WsEndpointParameters var3 = new WsEndpointParameters();
      var3.setEtlforgeUser(var0);
      var3.setEtlforgeMd5Pwd(var1);
      String var4 = var2.getConnectorTypeId();
      var3.setEndpointTypeName(var4);
      var3.setEndpointGUID(var2.getID().toString());
      EndpointParameters var5 = var2.getConnectionParameters();
      var3.setHost(var5.getServer().getAddress());
      UserCredentials var6 = var5.getUserCredentials();
      var3.setUser(var6.getUsername());
      Optional var7 = var6.getPassword();
      if (var7.isPresent()) {
         var3.setPassword(((Password)var7.get()).clearText());
      }

      var3.setIsPasswordEncrypted(false);
      ArrayList var8 = Lists.newArrayList();
      Properties var9 = var5.getAdditionalParams();

      for (String var11 : var9.stringPropertyNames()) {
         String var12 = var9.getProperty(var11);
         var8.add(new WsNameValuePair(var11, var12));
      }

      var3.setAdditionalParameters(Iterables.toArray(var8, WsNameValuePair.class));
      return var3;
   }
}
