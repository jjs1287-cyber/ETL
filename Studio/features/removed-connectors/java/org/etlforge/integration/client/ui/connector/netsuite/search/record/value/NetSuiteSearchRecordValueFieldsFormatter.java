package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.collect.Maps;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.logging.Logger;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;

public class NetSuiteSearchRecordValueFieldsFormatter {
   private final Logger log = Logger.getLogger("NetSuiteSearchRecordValueFieldsXmlFormatter");
   private final NetSuiteSearchNamespacePrefixes util;
   private final NetSuiteSearchCustomFieldId customFieldId;
   private final NetSuiteSearchBasic searchObjectFields;
   private final NetSuiteSearchObjectValueFields searchObjectValueFields;
   private SortedMap<Integer, String> customFieldFiltersIndex = null;

   public NetSuiteSearchRecordValueFieldsFormatter(NetSuiteSearchObjectValue var1) {
      this.searchObjectValueFields = var1.getNetSuiteSearchObjectValueFields();
      this.searchObjectFields = this.searchObjectValueFields.getNetSuiteSearchObjectAllFields();
      this.util = this.searchObjectFields.getNamespacePrefix();
      this.customFieldId = new NetSuiteSearchCustomFieldId(this.util.getNetSuiteWsdlVersion().usingScriptIdForCustomField());
   }

   public String toFiltersString() {
      StringBuilder var1 = new StringBuilder();

      for (Entry var3 : this.searchObjectValueFields.getFilters().entrySet()) {
         var1.append((String)var3.getKey()).append(":").append(var3.toString());
      }

      return var1.toString();
   }

   void toXmlAllFields(StringBuilder var1) {
      this.customFieldFiltersIndex = Maps.newTreeMap();

      for (Entry var3 : this.searchObjectValueFields.getFiltersIndex().entrySet()) {
         String var4 = (String)var3.getValue();
         if (this.searchObjectFields.isCustomField(var4)) {
            this.customFieldFiltersIndex.put((Integer)var3.getKey(), (String)var3.getValue());
         } else {
            String var5 = this.util.getQName(this.searchObjectFields.getSearchObjectNamespace(), var4);
            SearchFieldValue var6 = this.searchObjectValueFields.get(var4);
            var6.toXml(var1, var5, null, null);
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
            NetSuiteSearchCustomField var6 = this.searchObjectFields.getNetSuiteSearchObjectCustomFields().getNetSuiteSearchCustomFieldByFieldName(var5);
            if (var6 == null) {
               String var7 = "Meta data may be outdated : No definition for field: " + var5;
               this.log.warning(var7);
            } else {
               String var9 = "customField";
               SearchFieldValue var8 = this.searchObjectValueFields.get(var5);
               var8.toXml(var1, var9, var6, this.customFieldId);
            }
         }

         XmlUtils.endElementTag(var1, var2);
      }
   }
}
