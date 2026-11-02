package org.etlforge.integration.design.netsuite.cf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class NetSuiteCustomFieldsRequest {
   private final NetSuiteCustomFieldImplementedDefinition def;
   final Map<String, List<String>> requestObjects = Maps.newHashMap();
   final Set<String> customizationTypes = Sets.newHashSet();

   public NetSuiteCustomFieldsRequest(NetSuiteCustomFieldImplementedDefinition var1) {
      this.def = var1;
   }

   public void addObject(String var1) {
      this.addObjectImpl(true, true, var1, var1);
   }

   public void addObject(boolean var1, String var2, String var3) {
      this.addObjectImpl(var1, false, var2, var3);
   }

   public boolean containsKey(String var1) {
      return this.requestObjects.containsKey(var1);
   }

   private void addObjectImpl(boolean var1, boolean var2, String var3, String var4) {
      NetSuiteCustomFieldImplementedObject var5 = this.def.getNetSuiteCustomFieldObject(var4);
      if (var5 != null) {
         this.addObjectImpl(var3, var5.getObjectTypeName());
         if (var1 && var5.hasColumnCustomField()) {
            if (var2) {
               this.addObjectImpl(var5.getObjectTypeColumnName(), var5.getObjectTypeColumnName());
            } else {
               this.addObjectImpl(var3, var5.getObjectTypeColumnName());
            }
         }
      }
   }

   private void addObjectImpl(String var1, String var2) {
      List var3 = this.requestObjects.get(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.requestObjects.put(var1, var3);
      }

      var3.add(var2);
   }

   Map<String, List<String>> getRequestObjects() {
      return this.requestObjects;
   }

   int size() {
      return this.requestObjects.size();
   }

   public Set<String> getKeySet() {
      return this.requestObjects.keySet();
   }

   public void updateInfo() {
      for (Entry var2 : this.requestObjects.entrySet()) {
         for (Object __etlItem70 : (List)var2.getValue()) {
            String var4 = (String) __etlItem70;
            if (this.def.hasCustomFieldImplementation(var4)) {
               String var5 = this.def.getAppliedObjects().getCustomizationType(var4);
               this.customizationTypes.add(var5);
            }
         }
      }
   }

   public Set<String> getCustomizationTypes() {
      return this.customizationTypes;
   }

   @Override
   public String toString() {
      return this.requestObjects.toString();
   }
}
