package org.etlforge.integration.server.netsuite.config;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedDefinition;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldImplementedObject;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldType;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldTypeConverter;
import org.etlforge.integration.design.netsuite.config.NetSuiteConfig;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomFieldObject;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;

public class NetSuiteConfigFactory {
   public static NetSuiteConfig create(String var0) {
      NetSuiteConfigSetting var1 = NetSuiteConfigParser.parseString(var0);
      return create(var1);
   }

   public static NetSuiteConfig create(NetSuiteConfigSetting var0) {
      String var1 = var0.getMissingErrorMessage();
      if (var1 != null) {
         throw new ConnectorNetSuiteDefinitionError(var1);
      }

      NetSuiteCustomSearchDefinition var2 = buildNetSuiteCustomSearch(var0);
      NetSuiteCustomFieldImplementedDefinition var3 = buildCustomFieldImplemented(var0);
      NetSuiteCustomFieldTypeConverter var4 = buildNSCustomFieldTypeConverter(var0);
      return new NetSuiteConfig(var0.getExecutionCacheSize(), var0.getDesignCacheSize(), var3, var2, var4);
   }

   private static NetSuiteCustomSearchDefinition buildNetSuiteCustomSearch(NetSuiteConfigSetting var0) {
      NetSuiteCustomSearchDefinition var1 = new NetSuiteCustomSearchDefinition();

      for (NetSuiteCustomSearch var3 : NetSuiteCustomSearch.getConfigs(var0)) {
         var1.put(var3.getSearchNetSuiteObject(), var3.getSearchObject(), var3.getSearchNetSuiteSubtype(), var3.getSearchResponseObjectNames());
      }

      return var1;
   }

   private static NetSuiteCustomFieldImplementedDefinition buildCustomFieldImplemented(NetSuiteConfigSetting var0) {
      NetSuiteCustomFieldImplementedDefinition var1 = new NetSuiteCustomFieldImplementedDefinition();

      for (NetSuiteCustomFieldObject var3 : var0.getCustomFieldObjects()) {
         NetSuiteCustomFieldImplementedObject var4 = new NetSuiteCustomFieldImplementedObject(var3.getObjectName(), var3.getObjectColumnName());
         var1.putCustomFieldObjects(var3.getObjectName(), var4);
         var1.putCustomFieldApplyObjects(
            var3.getObjectName(), var3.getGetCustomizationType(), var3.getAppliesToObject(), var3.getAppliesToObjectColumn(), var3.getObjectColumnName()
         );
      }

      return var1;
   }

   static NetSuiteCustomFieldTypeConverter buildNSCustomFieldTypeConverter(NetSuiteConfigSetting var0) {
      NetSuiteCustomFieldTypeConverter var1 = new NetSuiteCustomFieldTypeConverter();

      for (NetSuiteCustomFieldType var3 : var0.getCustomFieldTypes()) {
         var1.put(var3.getFieldType(), var3);
      }

      return var1;
   }
}
