package org.etlforge.integration.design.netsuite.cf.meta;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomFieldObject;

public class NetSuiteCustomFieldApplyObjectsTypes implements Serializable {
   private final Map<String, String> objectCustomizationType = Maps.newHashMap();
   private final Map<String, NetSuiteCustomFieldApplyObjects> typeApplyObjects = Maps.newHashMap();

   public String getCustomizationType(String var1) {
      return this.objectCustomizationType.get(var1);
   }

   public boolean hasCustomFieldImplementation(String var1) {
      return this.objectCustomizationType.containsKey(var1);
   }

   public Set<String> getHasCustomFieldsObjects() {
      return this.objectCustomizationType.keySet();
   }

   public NetSuiteCustomFieldApplyObjects getApplyObjectsTypes(String var1) {
      return this.typeApplyObjects.get(var1);
   }

   private void add(String var1, String var2, String var3) {
      this.objectCustomizationType.put(var3, var1);
      NetSuiteCustomFieldApplyObjects var4 = this.typeApplyObjects.get(var1);
      if (var4 == null) {
         var4 = new NetSuiteCustomFieldApplyObjects(var1);
         this.typeApplyObjects.put(var1, var4);
      }

      var4.add(var2, var3);
   }

   public void put(String var1, String var2, String var3, String var4, String var5) {
      this.add(var2, var3, var1);
      if (!Strings.isNullOrEmpty(var4)) {
         this.add(NetSuiteCustomFieldObject.getGetcustomizationtypecolumn(), var4, var5);
      }
   }
}
