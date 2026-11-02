package org.etlforge.integration.design.netsuite.cf;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;

public class ConnectorNetSuiteCustomFieldsFactory {
   private final List<ConnectorNetSuiteCustomField> customfields = Lists.newArrayList();

   private List<ConnectorNetSuiteCustomField> getCustomFields() {
      return this.customfields;
   }

   public static ConnectorNetSuiteCustomFields getNetSuiteCustomFields(NetSuiteCustomFieldsLookup var0) {
      NetSuiteObjectCustomFields var1 = var0.getNetSuiteObjectsCustomFields();
      return getNetSuiteCustomFields(var1);
   }

   public static ConnectorNetSuiteCustomFields getNetSuiteCustomFields(NetSuiteObjectCustomFields var0) {
      ConnectorNetSuiteCustomFields var1 = new ConnectorNetSuiteCustomFields(var0.getCustomObjectId());

      for (Entry var3 : var0.getNetSuiteObjectsCustomFields().entrySet()) {
         NetSuiteCustomFields var4 = (NetSuiteCustomFields)var3.getValue();
         List var5 = newNetSuiteCustomFields(var4.getCustomFields());
         var1.put((String)var3.getKey(), var5);
      }

      return var1;
   }

   private static List<ConnectorNetSuiteCustomField> newNetSuiteCustomFields(List<NetSuiteCustomField> var0) {
      ConnectorNetSuiteCustomFieldsFactory var1 = new ConnectorNetSuiteCustomFieldsFactory();
      var1.getNetSuiteCustomFields(var0);
      return var1.getCustomFields();
   }

   private void getNetSuiteCustomFields(List<NetSuiteCustomField> var1) {
      this.customfields.clear();

      for (NetSuiteCustomField var3 : var1) {
         this.customfields.add(new ConnectorNetSuiteCustomField(var3));
      }

      this.updateDuplicatesNameWithInternalId();
      this.makeUniqueTypeNameForCustomFields();
   }

   private void makeUniqueTypeNameForCustomFields() {
      HashSet var1 = Sets.newHashSet();

      for (ConnectorNetSuiteCustomField var3 : this.customfields) {
         String var4 = var3.getCustomFieldInternalIDTypeName();
         if (var1.contains(var4)) {
            var4 = this.getUniqueName(var1, var4);
            var3.setCustomFieldInternalIDTypeName(var4);
         }

         var1.add(var4);
      }
   }

   private String getUniqueName(Set<String> var1, String var2) {
      for (int var3 = 0; var1.contains(var2); var3++) {
         var2 = var2 + var3;
      }

      return var2;
   }

   private void updateDuplicatesNameWithInternalId() {
      HashSet var1 = Sets.newHashSet();
      HashSet var2 = Sets.newHashSet();

      for (ConnectorNetSuiteCustomField var4 : this.customfields) {
         if (var1.contains(var4.getCustomFieldXmlName())) {
            var2.add(var4.getCustomFieldXmlName());
         }

         var1.add(var4.getCustomFieldXmlName());
      }

      for (ConnectorNetSuiteCustomField var6 : this.customfields) {
         if (var2.contains(var6.getCustomFieldXmlName())) {
            var6.setCustomFieldXmlNameWithId();
         }
      }
   }
}
