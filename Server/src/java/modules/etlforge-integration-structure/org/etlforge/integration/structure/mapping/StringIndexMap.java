package org.etlforge.integration.structure.mapping;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;

public final class StringIndexMap {
   public static final String ROOT = "Strings";
   public static final String ENTRY = "String";
   public static final String INDEX = "idx";
   public static final String VALUE = "val";
   private final Map<String, Integer> stringToIndex = Maps.newHashMap();
   private final Map<Integer, String> indexToString = Maps.newTreeMap();
   private int counter;

   public String store(String var1) {
      if (StringUtils.isEmpty(var1)) {
         return var1;
      }

      Integer var2 = this.stringToIndex.get(var1);
      if (var2 == null) {
         var2 = ++this.counter;
         this.stringToIndex.put(var1, var2);
         this.indexToString.put(var2, var1);
      }

      return var2.toString();
   }

   public void writeXml(StringBuilder var1) {
      var1.append("<").append("Strings").append(">\n");

      for (Entry var3 : this.indexToString.entrySet()) {
         var1.append("<").append("String").append(" ");
         var1.append("idx").append("=\"").append(var3.getKey()).append("\" ");
         var1.append("val").append("=\"").append((String)var3.getValue()).append("\"/>\n");
      }

      var1.append("</").append("Strings").append(">\n");
   }

   public void writeXml(Writer var1) throws IOException {
      StringBuilder var2 = new StringBuilder();
      var2.append("<").append("Strings").append(">\n");

      for (Entry var4 : this.indexToString.entrySet()) {
         var2.append("<").append("String").append(" ");
         var2.append("idx").append("=\"").append(var4.getKey()).append("\" ");
         JtrXmlFormatUtils.addAttribute(var2, "val", (String)var4.getValue());
         var2.append("/>\n");
      }

      var2.append("</").append("Strings").append(">\n");
      var1.write(var2.toString());
   }
}
