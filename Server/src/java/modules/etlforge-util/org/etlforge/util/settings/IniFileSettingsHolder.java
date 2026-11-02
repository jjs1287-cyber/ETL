package org.etlforge.util.settings;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;

public class IniFileSettingsHolder {
   private final Map<String, Map<String, String>> settings = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);

   public void addValue(String var1, String var2, String var3) {
      if (var1 != null && var2 != null) {
         Map var4 = this.settings.get(var1);
         if (var4 == null) {
            if (var3 == null) {
               return;
            }

            var4 = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
            this.settings.put(var1, var4);
         }

         if (var3 != null) {
            var4.put(var2, var3);
         } else {
            var4.remove(var2);
         }
      }
   }

   public String getValue(String var1, String var2) {
      if (var1 != null && var2 != null) {
         Map var3 = this.settings.get(var1);
         return var3 == null ? null : (String)var3.get(var2);
      } else {
         return null;
      }
   }

   public ImmutableMap<String, String> getSection(String var1) {
      Map var2 = this.settings.get(var1);
      return var2 == null ? ImmutableMap.of() : ImmutableMap.copyOf(var2);
   }

   public int numberOfSections() {
      return this.settings.size();
   }

   public boolean containsSection(String var1) {
      return this.settings.containsKey(var1);
   }
}
