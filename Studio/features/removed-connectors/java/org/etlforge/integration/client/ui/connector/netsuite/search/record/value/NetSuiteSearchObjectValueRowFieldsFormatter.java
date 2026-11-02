package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasicRow;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchRowPropertyies;

public class NetSuiteSearchObjectValueRowFieldsFormatter {
   private final Logger log = Logger.getLogger("NetSuiteSearchObjectValueRowFieldsFormatter");
   private final NetSuiteSearchNamespacePrefixes util;
   private final NetSuiteSearchCustomFieldId customFieldId;
   private final NetSuiteSearchBasicRow searchObjectFields;
   private final NetSuiteSearchObjectValueRow searchObjectValueFields;
   private SortedMap<Integer, String> customFieldFiltersIndex = null;
   private final NetSuiteSearchRowPropertyies searchRowProperties;
   private final String searchRowTypeName;

   public NetSuiteSearchObjectValueRowFieldsFormatter(NetSuiteSearchObjectValueRow var1, NetSuiteSearchRowPropertyies var2) {
      this.searchRowTypeName = var1.getNetSuiteSearchBasicRow().getSearchObjectLocalName();
      this.searchRowProperties = var2;
      this.searchObjectValueFields = var1;
      this.searchObjectFields = var1.getNetSuiteSearchBasicRow();
      this.util = this.searchObjectFields.getNamespacePrefix();
      this.customFieldId = new NetSuiteSearchCustomFieldId(this.util.getNetSuiteWsdlVersion().usingScriptIdForCustomField());
   }

   private void putSearchRowField(String var1) {
      if (this.searchRowProperties != null) {
         this.searchRowProperties.putField(this.searchRowTypeName, var1);
      }
   }

   private void putSearchRowCustomField(NetSuiteSearchCustomField var1) {
      String var2 = NetSuiteSearchCustomField.getNameWithoutPrefix(var1.getSearchFieldName());
      String var3 = var1.getScriptID();
      String var4 = var1.getSearchColumnCustomFieldType();
      this.putSearchRowCustomField(var2, var3, var4);
   }

   private void putSearchRowCustomField(String var1, String var2, String var3) {
      if (this.searchRowProperties != null) {
         this.searchRowProperties.putCustomField(this.searchRowTypeName, var1, var2, var3);
      }
   }

   void toXmlAllFields(StringBuilder var1) {
      this.customFieldFiltersIndex = Maps.newTreeMap();

      for (Entry var3 : this.searchObjectValueFields.getFiltersIndexSet()) {
         String var4 = (String)var3.getValue();
         if (this.searchObjectFields.isCustomField(var4)) {
            this.customFieldFiltersIndex.put((Integer)var3.getKey(), (String)var3.getValue());
         } else {
            String var5 = this.util.getQName(this.searchObjectFields.getSearchObjectNamespace(), var4);
            XmlUtils.ShortElement(var1, var5);
            this.putSearchRowField(var4);
         }
      }

      this.toXmlCustomFields(var1);
   }

   private void toXmlCustomFields(StringBuilder var1) {
      if (this.customFieldFiltersIndex.size() != 0) {
         String var2 = this.util.getQName(this.searchObjectFields.getSearchObjectNamespace(), "customFieldList");
         XmlUtils.startElementTag(var1, var2);

         for (Entry var4 : this.customFieldFiltersIndex.entrySet()) {
            String var5 = (String)var4.getValue();
            NetSuiteSearchCustomField var6 = this.searchObjectFields.getNetSuiteSearchCustomFieldByFieldName(var5);
            if (var6 == null) {
               String var7 = "Meta data may be outdated : No definition for field: " + var5;
               this.log.warning(var7);
            } else {
               String var8 = "customField";
               this.toXmlCustomField(var1, var8, var6);
               this.putSearchRowCustomField(var6);
            }
         }

         XmlUtils.endElementTag(var1, var2);
      }
   }

   private void toXmlCustomField(StringBuilder var1, String var2, NetSuiteSearchCustomField var3) {
      HashMap var4 = Maps.newHashMap();
      if (var3 != null) {
         this.customFieldId.put(var4, var3);
         var4.put("xsi:type", var3.getSearchColumnCustomFieldType());
      }

      XmlUtils.startElementTagAttributes(var1, var2, var4);
      XmlUtils.endElementTag(var1, var2);
   }
}
