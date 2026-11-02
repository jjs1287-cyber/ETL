package org.etlforge.integration.design.netsuite.cf.meta;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class NetSuiteObjectCustomFields implements Serializable {
   private static final List<NetSuiteCustomField> none = Lists.newArrayList();
   private final Map<String, NetSuiteCustomFields> customFields = Maps.newHashMap();
   private final String customObjectId;

   public NetSuiteObjectCustomFields() {
      this.customObjectId = null;
   }

   public NetSuiteObjectCustomFields(String var1) {
      this.customObjectId = var1;
      this.customFields.put(var1, new NetSuiteCustomFields());
   }

   public static NetSuiteObjectCustomFields newCustomObjectCustomFields(String var0) {
      Preconditions.checkNotNull(var0, "Custom Object Id can not be empty");
      return new NetSuiteObjectCustomFields(var0);
   }

   public void add(NetSuiteObjectCustomFields var1) {
      for (Entry var3 : var1.customFields.entrySet()) {
         this.add((String)var3.getKey(), ((NetSuiteCustomFields)var3.getValue()).getCustomFields());
      }
   }

   public void add(String var1, List<NetSuiteCustomField> var2) {
      for (NetSuiteCustomField var4 : var2) {
         this.add(var1, var4);
      }
   }

   public void add(String var1, NetSuiteCustomField var2) {
      NetSuiteCustomFields var3 = this.customFields.get(var1);
      if (var3 == null) {
         var3 = new NetSuiteCustomFields();
         this.customFields.put(var1, var3);
      }

      var3.add(var2);
   }

   public List<NetSuiteCustomField> getNetSuiteCustomFieldsObject(String var1) {
      boolean var2 = Strings.isNullOrEmpty(this.customObjectId);
      NetSuiteCustomFields var3 = var2 ? this.customFields.get(var1) : this.customFields.get(this.customObjectId);
      return var3 == null ? none : var3.getCustomFields();
   }

   public String getCustomObjectId() {
      return this.customObjectId;
   }

   public Map<String, NetSuiteCustomFields> getNetSuiteObjectsCustomFields() {
      return this.customFields;
   }
}
