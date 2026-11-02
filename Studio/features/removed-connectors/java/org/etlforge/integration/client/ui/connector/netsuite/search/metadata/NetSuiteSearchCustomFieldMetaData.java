package org.etlforge.integration.client.ui.connector.netsuite.search.metadata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchCustomObject;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;

public class NetSuiteSearchCustomFieldMetaData {
   private final String selectedObject;
   private final NetSuiteSearchCustomObject customObject;
   private final Map<String, List<NetSuiteSearchCustomField>> customFields;

   public NetSuiteSearchCustomFieldMetaData(String var1, NetSuiteSearchCustomObject var2) {
      this.selectedObject = var1;
      this.customObject = var2;
      this.customFields = Maps.newHashMap();
   }

   public NetSuiteSearchCustomObject getCustomObject() {
      return this.customObject;
   }

   public void add(String var1, String var2, String var3, String var4, String var5) {
      NetSuiteSearchCustomField var6 = new NetSuiteSearchCustomField(var2, var3, var4, var5);
      this.add(var1, var6);
   }

   private void add(String var1, NetSuiteSearchCustomField var2) {
      List var3 = this.getCustomFields(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.customFields.put(var1, var3);
      }

      var3.add(var2);
   }

   public String getSelectedObject() {
      return this.selectedObject;
   }

   public List<NetSuiteSearchCustomField> getCustomFields(String var1) {
      return this.customFields.get(var1);
   }

   public Set<String> getAllCategories() {
      return this.customFields.keySet();
   }
}
