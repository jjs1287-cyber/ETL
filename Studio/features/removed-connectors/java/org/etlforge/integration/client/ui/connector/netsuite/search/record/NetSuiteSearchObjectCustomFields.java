package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchCustomObject;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;

public class NetSuiteSearchObjectCustomFields {
   private final NetSuiteSearchObjectSearchFields searchFields;
   private final NetSuiteSearchCustomObject customObject;
   private final Map<String, NetSuiteSearchCustomField> customFieldsScriptId = Maps.newHashMap();
   private final Map<String, NetSuiteSearchCustomField> customFieldsFieldName = Maps.newHashMap();
   private static final int maxRegularFieldsPerObject = 1000000;

   public NetSuiteSearchObjectCustomFields(String var1, NetSuiteSearchCustomObject var2) {
      this.customObject = var2;
      this.searchFields = new NetSuiteSearchObjectSearchFields(var1);
   }

   public NetSuiteSearchCustomObject getCustomObject() {
      return this.customObject;
   }

   public void addCustomField(NetSuiteSearchCustomField var1, SearchField var2) {
      this.searchFields.addField(var1.getSearchFieldName(), var2);
      this.customFieldsScriptId.put(var1.getScriptID(), var1);
      this.customFieldsFieldName.put(var1.getSearchFieldName(), var1);
   }

   public NetSuiteSearchCustomField getNetSuiteSearchCustomFieldByScriptID(String var1) {
      return this.customFieldsScriptId.get(var1);
   }

   public NetSuiteSearchCustomField getNetSuiteSearchCustomFieldByFieldName(String var1) {
      return this.customFieldsFieldName.get(var1);
   }

   List<String> getFieldNames() {
      return this.searchFields.getFieldNames(null);
   }

   public SearchField getSearchField(String var1) {
      return this.searchFields.getSearchField(var1);
   }

   public Integer getFieldIndex(String var1) {
      return 1000000 + this.searchFields.getFieldIndex(var1);
   }
}
