package org.etlforge.integration.common.server.license;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public final class OptionsParser {
   public static final String KEY_VALUE_PAIR_SEPARATOR = ";";
   public static final String KEY_VALUE_SEPARATOR = "=";
   private Map<String, String> keyValues;

   public Map<String, String> parse(String var1) {
      this.keyValues = Maps.newHashMap();
      this.doParse(var1);
      return this.keyValues;
   }

   private void doParse(String var1) {
      if (StringUtils.isNotEmpty(var1)) {
         for (String var5 : this.getKeyValues(var1)) {
            this.parseKeyValue(var5);
         }
      }
   }

   private String[] getKeyValues(String var1) {
      return var1.split(";");
   }

   private void parseKeyValue(String var1) {
      String[] var2 = var1.split("=");
      if (var2.length != 2) {
         throw new RuntimeException("Invalid key-value pair encountered in the license options string: " + var1);
      }

      String var3 = var2[0];
      if (StringUtils.isBlank(var3)) {
         throw new RuntimeException("Empty key encountered in the license options string: " + var1);
      }

      String var4 = var2[1];
      if ("edition".equalsIgnoreCase(var2[0])) {
         if ("enterprise-mx".equalsIgnoreCase(var4)) {
            var4 = "enterprise";
         } else if ("enterprise".equalsIgnoreCase(var4)) {
            var4 = "professional";
         }
      }

      this.keyValues.put(var2[0], var4);
   }
}
