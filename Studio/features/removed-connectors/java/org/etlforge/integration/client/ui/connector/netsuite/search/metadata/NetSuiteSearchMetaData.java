package org.etlforge.integration.client.ui.connector.netsuite.search.metadata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionObjectAdvanced;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearchDefinition;

public class NetSuiteSearchMetaData {
   private final Map<String, String> targetNamespacePrefixes = Maps.newHashMap();
   private final Map<String, List<String>> enumTypeList = Maps.newHashMap();
   private final Map<String, NetSuiteSearchObjectMetaData> netSuiteSearchObjects = Maps.newHashMap();
   private final NetSuiteCustomSearchDefinition netsuiteCustomSearchObjects = new NetSuiteCustomSearchDefinition();
   private final Map<String, NetSuiteDefinitionObjectAdvanced> advanced = Maps.newHashMap();

   public Map<String, NetSuiteDefinitionObjectAdvanced> getNetSuiteDefinitionObjectAdvanced() {
      return this.advanced;
   }

   public NetSuiteCustomSearchDefinition getNetSuiteCustomSearchDefinition() {
      return this.netsuiteCustomSearchObjects;
   }

   public Map<String, String> getTargetNamespacePrefixes() {
      return this.targetNamespacePrefixes;
   }

   public Map<String, List<String>> getEnumTypeList() {
      return this.enumTypeList;
   }

   public Map<String, NetSuiteSearchObjectMetaData> getNetSuiteSearchObjects() {
      return this.netSuiteSearchObjects;
   }

   public void addNamespacePrefix(String var1, String var2) {
      this.targetNamespacePrefixes.put(var1, var2);
   }

   public void addEnumType(String var1, String var2) {
      List var3 = this.enumTypeList.get(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.enumTypeList.put(var1, var3);
      }

      var3.add(var2);
   }

   public void addNetSuiteSearchObjectMetaData(String var1, NetSuiteSearchObjectMetaData var2) {
      this.netSuiteSearchObjects.put(var1, var2);
   }

   public void addNetSuiteSearchCustomObjects(String var1, String var2, String var3) {
      this.netsuiteCustomSearchObjects.put(var2, var1, var3);
   }
}
