package org.etlforge.integration.design.netsuite.search.wsdl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XsdGlobalElementName;

public class NetSuiteSearchWsdl implements Serializable {
   private static String[] supportedEnumerationTypes = new String[]{"SearchDate", "RecordType", "PostingPeriodDate"};
   Map<XsdGlobalElementName, List<String>> enumTypeList = Maps.newHashMap();
   Map<XsdGlobalElementName, List<NetSuiteSearchField>> searchObjects = Maps.newHashMap();
   Map<String, XsdGlobalElementName> searchObjectsIndex = Maps.newHashMap();
   private final ConnectorNetSuiteNamespaces ns;

   public NetSuiteSearchWsdl(ConnectorNetSuiteNamespaces var1) {
      this.ns = var1;
   }

   public ConnectorNetSuiteNamespaces getConnectorNetSuiteNamespaces() {
      return this.ns;
   }

   public Map<String, String> getNamespacePrefix() {
      return this.ns.getNamespacePreferedPrefix().getNamespacePrefix();
   }

   public Map<XsdGlobalElementName, List<String>> getEnumTypeList() {
      return this.enumTypeList;
   }

   public Map<String, List<String>> asEnumTypeList() {
      HashMap var1 = Maps.newHashMap();

      for (Entry var3 : this.enumTypeList.entrySet()) {
         var1.put(((XsdGlobalElementName)var3.getKey()).getLocalName(), var3.getValue());
      }

      return var1;
   }

   public Map<XsdGlobalElementName, List<NetSuiteSearchField>> getSearchObjects() {
      return this.searchObjects;
   }

   public List<NetSuiteSearchField> getSearchObjectIndexExists(String var1) {
      XsdGlobalElementName var2 = this.searchObjectsIndex.get(var1);
      if (var2 == null) {
         throw new ConnectorNetSuiteDefinitionError("no search object for " + var1);
      } else {
         return this.searchObjects.get(var2);
      }
   }

   public static boolean isSupportedEnumerationType(String var0) {
      if (var0.endsWith("Operator")) {
         return true;
      }

      for (String var4 : supportedEnumerationTypes) {
         if (var4.equals(var0)) {
            return true;
         }
      }

      return false;
   }

   public void addEnumType(XsdGlobalElementName var1, String var2) {
      List var3 = this.enumTypeList.get(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.enumTypeList.put(var1, var3);
      }

      var3.add(var2);
   }

   public void addSearchField(XsdGlobalElementName var1, NetSuiteSearchField var2) {
      XsdGlobalElementName var3 = this.searchObjectsIndex.get(var1.getLocalName());
      if (var3 == null) {
         this.searchObjectsIndex.put(var1.getLocalName(), var1);
      } else if (!var1.equals(var3)) {
         throw new ConnectorNetSuiteDefinitionError("Two search object with same name " + var3 + " and " + var1);
      }

      List var4 = this.searchObjects.get(var1);
      if (var4 == null) {
         var4 = Lists.newArrayList();
         this.searchObjects.put(var1, var4);
      }

      var4.add(var2);
   }

   public void validateUniqueName() {
      validateUniqueName(this.enumTypeList.keySet());
      validateUniqueName(this.searchObjects.keySet());
   }

   static void validateUniqueName(Set<XsdGlobalElementName> var0) {
      HashSet var1 = Sets.newHashSet();

      for (XsdGlobalElementName var3 : var0) {
         if (var1.contains(var3.getLocalName())) {
            throw new ConnectorNetSuiteDefinitionError("duplicate names defined for name " + var3.getLocalName());
         }

         var1.add(var3.getLocalName());
      }
   }
}
