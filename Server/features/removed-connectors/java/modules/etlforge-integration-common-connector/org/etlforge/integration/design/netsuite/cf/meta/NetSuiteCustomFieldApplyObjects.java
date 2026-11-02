package org.etlforge.integration.design.netsuite.cf.meta;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NetSuiteCustomFieldApplyObjects implements Serializable {
   private final String customizationType;
   private final Map<String, List<String>> appliesObjects;

   public NetSuiteCustomFieldApplyObjects(String var1) {
      this.customizationType = var1;
      this.appliesObjects = Maps.newHashMap();
   }

   public void add(String var1, String var2) {
      List var3 = this.appliesObjects.get(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.appliesObjects.put(var1, var3);
      }

      var3.add(var2);
   }

   public String getCustomizationType() {
      return this.customizationType;
   }

   public boolean isApplytoKey(String var1) {
      return this.appliesObjects.containsKey(var1);
   }

   public List<String> getAppliedObjects(String var1) {
      return this.appliesObjects.get(var1);
   }
}
