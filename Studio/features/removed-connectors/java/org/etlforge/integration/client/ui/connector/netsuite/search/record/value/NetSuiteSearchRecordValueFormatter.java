package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchRowPropertyies;

public class NetSuiteSearchRecordValueFormatter {
   private static final String SEARCHELEMENTNAME = "search";
   private static final String SEARCHRECORDELEMENTNAME = "searchRecord";
   private final NetSuiteSearchRecordValue recordValue;
   private final NetSuiteSearchNamespacePrefixes namespacePrefixes;
   private final String searchRecordQName;
   private final String searchFieldTypeNamespace;

   NetSuiteSearchRecordValueFormatter(NetSuiteSearchRecordValue var1) {
      this.recordValue = var1;
      this.namespacePrefixes = var1.getNetSuiteSearchRecord().getNamespacePrefix();
      this.searchFieldTypeNamespace = this.namespacePrefixes.getCoreNamespace();
      this.searchRecordQName = this.namespacePrefixes.getQName(this.namespacePrefixes.getMessageNamespace(), "searchRecord");
   }

   public String toXml(NetSuiteSearchAdvancedProperties var1) {
      StringBuilder var2 = new StringBuilder();
      String var3 = this.namespacePrefixes.getQName(this.namespacePrefixes.getMessageNamespace(), "search");
      Map var4 = this.getPrefixNamespaceMap();
      XmlUtils.startElementTagPrefixNamespaceDef(var2, var3, var4);
      NetSuiteSearchCategory var5 = this.recordValue.getNetSuiteSearchTypeFromValue();
      switch (var5) {
         case NetSuiteSearchAdvancedSavedSearch:
            this.setAdvancedSavedSearchXml(var2);
            break;
         case NetSuiteSearchBasic:
            this.setBasicXml(var2);
            break;
         case NetSuiteSearchAdvanced:
            NetSuiteSearchRowPropertyies var6 = null;
            if (var1 != null) {
               NetSuiteWsdlVersion var7 = this.namespacePrefixes.getNetSuiteWsdlVersion();
               Object var8 = null;
               var6 = new NetSuiteSearchRowPropertyies(var7, (String)var8);
               var1.setNetSuiteSearchRowPropertyies(var6);
            }

            this.setAdvancedXml(var2, var6);
            break;
         default:
            this.setAdvancedXml(var2, null);
      }

      XmlUtils.endElementTag(var2, var3);
      return var2.toString();
   }

   private void setAdvancedSavedSearchXml(StringBuilder var1) {
      NetSuiteSearchRecord var2 = this.recordValue.getNetSuiteSearchRecord();
      String var3 = this.namespacePrefixes.getQName(var2.getSearchObjectNamespace(), var2.getSearchObjectLocalName());
      HashMap var4 = Maps.newHashMap();
      var4.put("xsi:type", var3);
      var4.put("savedSearchId", this.recordValue.getSavedSearchId());
      XmlUtils.startElementTagAttributes(var1, this.searchRecordQName, var4);
      XmlUtils.endElementTag(var1, this.searchRecordQName);
   }

   private void setAdvancedXml(StringBuilder var1, NetSuiteSearchRowPropertyies var2) {
      NetSuiteSearchRecord var3 = this.recordValue.getNetSuiteSearchRecord();
      String var4 = this.namespacePrefixes.getQName(var3.getSearchObjectNamespace(), var3.getSearchObjectLocalName());
      XmlUtils.startElementTagAttribute(var1, this.searchRecordQName, "xsi:type", var4);
      if (this.recordValue.hasCriteria()) {
         String var5 = this.namespacePrefixes.getQName(var3.getSearchObjectNamespace(), "criteria");
         XmlUtils.startElementTag(var1, var5);

         for (String var7 : var3.getNetSuiteSearchBasicKeys()) {
            NetSuiteSearchObjectValue var8 = this.recordValue.getNetSuiteSearchObjectValue(var7);
            if (var8 != null) {
               String var9 = this.namespacePrefixes.getQName(var3.getSearchObjectNamespace(), var7);
               XmlUtils.startElementTag(var1, var9);
               NetSuiteSearchRecordValueFieldsFormatter var10 = new NetSuiteSearchRecordValueFieldsFormatter(var8);
               var10.toXmlAllFields(var1);
               XmlUtils.endElementTag(var1, var9);
            }
         }

         XmlUtils.endElementTag(var1, var5);
      }

      if (this.recordValue.hasColumns()) {
         String var11 = this.namespacePrefixes.getQName(var3.getSearchObjectNamespace(), "columns");
         XmlUtils.startElementTag(var1, var11);

         for (String var13 : var3.getNetSuiteSearchBasicKeys()) {
            NetSuiteSearchObjectValueRow var14 = this.recordValue.getNetSuiteSearchObjectValueRow(var13);
            if (var14 != null) {
               String var15 = this.namespacePrefixes.getQName(var3.getSearchObjectNamespace(), var13);
               XmlUtils.startElementTag(var1, var15);
               NetSuiteSearchObjectValueRowFieldsFormatter var16 = new NetSuiteSearchObjectValueRowFieldsFormatter(var14, var2);
               var16.toXmlAllFields(var1);
               XmlUtils.endElementTag(var1, var15);
            }
         }

         XmlUtils.endElementTag(var1, var11);
      }

      XmlUtils.endElementTag(var1, this.searchRecordQName);
   }

   private String setBasicXml(StringBuilder var1) {
      NetSuiteSearchObjectValue var2 = this.recordValue.getNetSuiteSearchObjectValueBasic();
      if (var2 == null) {
         NetSuiteSearchBasic var3 = this.recordValue.getNetSuiteSearchRecord().getNetSuiteSearchBasic();
         var2 = new NetSuiteSearchObjectValue(var3);
      }

      String var5 = this.namespacePrefixes.getQName(var2.getSearchObjectNamespace(), var2.getSearchObjectLocalName());
      XmlUtils.startElementTagAttribute(var1, this.searchRecordQName, "xsi:type", var5);
      NetSuiteSearchRecordValueFieldsFormatter var4 = new NetSuiteSearchRecordValueFieldsFormatter(var2);
      var4.toXmlAllFields(var1);
      XmlUtils.endElementTag(var1, this.searchRecordQName);
      return var1.toString();
   }

   private Map<String, String> getPrefixNamespaceMap() {
      TreeMap var1 = Maps.newTreeMap();
      HashSet var2 = Sets.newHashSet();
      var2.add(this.namespacePrefixes.getMessageNamespace());
      var2.addAll(this.recordValue.getNamespaces());

      for (Object __etlItem138 : var2) {
         String var4 = (String) __etlItem138;
         var1.put(this.namespacePrefixes.getPrefix(var4), var4);
      }

      var1.put("", this.searchFieldTypeNamespace);
      var1.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
      return var1;
   }

   public static NetSuiteSearchRecordValue parse(NetSuiteSearchRecord var0, String var1) {
      return NetSuiteSearchObjectValueParser.parse(var0, var1);
   }
}
