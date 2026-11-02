package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Function;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.KongaListUtils;

final class ObjectAlias implements Function<String, String> {
   private final String alias;
   private final String object;

   public ObjectAlias(String var1, String var2) {
      this.alias = var1;
      this.object = var2;
   }

   public List<String> replaceInFieldNames(List<String> var1) {
      return KongaListUtils.transform(var1, this);
   }

   public String apply(String var1) {
      if (StringUtils.startsWithIgnoreCase(var1, this.alias + ".")) {
         String var2 = var1.substring(this.alias.length());
         return this.object + var2;
      } else {
         return var1;
      }
   }
}
