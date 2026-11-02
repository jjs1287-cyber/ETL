package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import com.google.common.base.Strings;
import java.util.Map;
import org.etlforge.integration.design.util.SchemaRenameStrategy;
import org.etlforge.util.file.InvalidCharactersReplacer;

public class NetSuiteSchemaRenameStrategy implements SchemaRenameStrategy {
   private final Map<String, String> locations;

   public NetSuiteSchemaRenameStrategy(Map<String, String> var1) {
      this.locations = var1;
   }

   @Override
   public String newName(String var1) {
      String var2 = this.newNameImpl(var1);
      String var3 = this.locations.get(var1);
      String var4 = nsPrefix(var3);
      if (var4 != null) {
         var2 = var4 + '_' + var2;
      }

      InvalidCharactersReplacer var5 = new InvalidCharactersReplacer("_");
      return var5.replace(var2);
   }

   private static String nsPrefix(String var0) {
      return Strings.isNullOrEmpty(var0) ? null : var0.replace("urn:", "").replace("webservices.netsuite.com", "").replace(".platform.", "");
   }

   private String newNameImpl(String var1) {
      int var2 = var1.lastIndexOf(92);
      int var3 = var1.lastIndexOf(47);
      if (var2 > var3) {
         var3 = var2;
      }

      if (var3 < 0) {
         return var1;
      }

      var3++;
      return var3 < var1.length() ? var1.substring(var3) : "";
   }

   @Override
   public void checkLocation(String var1, String var2) throws Exception {
   }
}
