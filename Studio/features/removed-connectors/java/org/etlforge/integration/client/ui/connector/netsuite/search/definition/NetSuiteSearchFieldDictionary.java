package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldBoolean;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldDate;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldDouble;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldEnumMultiSelect;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldLong;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldMultiSelect;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldPostingPeriodDate;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldRecordRef;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldString;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldTextNumber;

public class NetSuiteSearchFieldDictionary implements Serializable {
   private static final NetSuiteSearchFieldDictionary.NetSuiteSearchCustomFieldMapping customFieldMapping = new NetSuiteSearchFieldDictionary.NetSuiteSearchCustomFieldMapping();
   private final Map<String, SearchField> searchFields = Maps.newHashMap();

   public SearchField getSearchField(String var1) {
      return this.searchFields.get(var1);
   }

   public void init(Map<String, List<String>> var1) {
      this.searchFields.clear();
      List var2 = getEnumTypes("SearchDate", var1);
      List var3 = getEnumTypes("RecordType", var1);
      this.searchFields.put("SearchDateField", new SearchFieldDate(var2));
      this.searchFields.put("SearchDoubleField", new SearchFieldDouble());
      this.searchFields.put("SearchEnumMultiSelectField", new SearchFieldEnumMultiSelect());
      this.searchFields.put("SearchLongField", new SearchFieldLong());
      this.searchFields.put("SearchMultiSelectField", new SearchFieldMultiSelect(var3));
      this.searchFields.put("SearchMultiSelectCustomField", new SearchFieldMultiSelect(true));
      this.searchFields.put("SearchStringField", new SearchFieldString());
      this.searchFields.put("SearchTextNumberField", new SearchFieldTextNumber());

      for (Entry var5 : this.searchFields.entrySet()) {
         String var6 = (String)var5.getKey() + "Operator";
         if (((String)var5.getKey()).equals("SearchMultiSelectCustomField")) {
            var6 = "SearchMultiSelectFieldOperator";
         }

         List var7 = getEnumTypes(var6, var1);
         SearchField var8 = (SearchField)var5.getValue();

         for (Object __etlItem49 : var7) {
            String var10 = (String) __etlItem49;
            var8.addOperator(var10);
         }
      }

      List var11 = getEnumTypes("PostingPeriodDate", var1);
      this.searchFields.put("PostingPeriodDate", new SearchFieldPostingPeriodDate(var11));
      this.searchFields.put("RecordRef", new SearchFieldRecordRef(var3));
      this.searchFields.put("SearchBooleanField", new SearchFieldBoolean());

      for (Entry var13 : customFieldMapping.customFieldMapping.entrySet()) {
         SearchField var14 = this.searchFields.get(var13.getValue());
         this.searchFields.put((String)var13.getKey(), var14);
      }
   }

   static List<String> getEnumTypes(String var0, Map<String, List<String>> var1) {
      List var2 = (List)var1.get(var0);
      if (var2 == null) {
         throw new ConnectorNetSuiteWsdlError(var0 + " not found in wsdl (platformCore).");
      } else {
         return var2;
      }
   }

   private static class NetSuiteSearchCustomFieldMapping implements Serializable {
      private final Map<String, String> customFieldMapping = Maps.newHashMap();

      NetSuiteSearchCustomFieldMapping() {
         this.init();
      }

      void init() {
         this.customFieldMapping.put("SearchBooleanCustomField", "SearchBooleanField");
         this.customFieldMapping.put("SearchLongCustomField", "SearchLongField");
         this.customFieldMapping.put("SearchDoubleCustomField", "SearchDoubleField");
         this.customFieldMapping.put("SearchDateCustomField", "SearchDateField");
         this.customFieldMapping.put("SearchStringCustomField", "SearchStringField");
         this.customFieldMapping.put("SearchEnumMultiSelectCustomField", "SearchEnumMultiSelectField");
      }
   }
}
