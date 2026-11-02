package org.etlforge.integration.design.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class PrefixNamespace {
   Map<String, List<String>> prefixNamespaces = Maps.newHashMap();
   Map<String, String> currentPrefixNamespaces = Maps.newHashMap();

   public void startPrefixMappingExists(String var1, String var2) {
      if (!this.currentPrefixNamespaces.containsKey(var1)) {
         this.startPrefixMapping(var1, var2);
      }
   }

   public void startPrefixMapping(String var1, String var2) {
      this.currentPrefixNamespaces.put(var1, var2);
      List var3 = this.prefixNamespaces.get(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.prefixNamespaces.put(var1, var3);
      }

      var3.add(var2);
   }

   public void endPrefixMapping(String var1) {
      List var2 = this.prefixNamespaces.get(var1);
      if (var2 != null && var2.size() > 1) {
         var2.remove(var2.size() - 1);
      } else {
         this.prefixNamespaces.remove(var1);
      }
   }

   public void clearWithCurrentPrefixNamespaces() {
      if (!this.currentPrefixNamespaces.isEmpty()) {
         this.currentPrefixNamespaces.clear();
      }
   }

   public Map<String, String> getCurrentPrefixNamespaces() {
      return this.currentPrefixNamespaces;
   }

   public Map<String, String> getPrefixNamespaces() {
      HashMap var1 = Maps.newHashMap();

      for (Entry var3 : this.prefixNamespaces.entrySet()) {
         List var4 = (List)var3.getValue();
         if (var4 != null && var4.size() > 0) {
            var1.put(var3.getKey(), var4.get(var4.size() - 1));
         }
      }

      return var1;
   }

   public String getPrefix(String var1) {
      for (Entry var3 : this.prefixNamespaces.entrySet()) {
         List var4 = (List)var3.getValue();
         if (var4 != null && var4.size() > 0) {
            String var5 = (String)var4.get(var4.size() - 1);
            if (var1.equals(var5)) {
               return (String)var3.getKey();
            }
         }
      }

      return null;
   }

   public String getNamespace(String var1) {
      List var2 = this.prefixNamespaces.get(var1);
      return var2 != null && var2.size() > 0 ? (String)var2.get(var2.size() - 1) : null;
   }

   public String getNamespaceExists(String var1) {
      String var2 = this.getNamespace(var1);
      if (Strings.isNullOrEmpty(var2)) {
         throw new ConnectorNetSuiteDefinitionError("No namespace found for prefix " + var1);
      } else {
         return var2;
      }
   }
}
