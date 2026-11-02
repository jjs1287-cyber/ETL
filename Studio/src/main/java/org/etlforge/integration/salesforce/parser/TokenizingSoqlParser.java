package org.etlforge.integration.salesforce.parser;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.dataelement.DataElementResolver;

public final class TokenizingSoqlParser implements SoqlParser {
   @Override
   public ParsedQuery parse(String var1) {
      SoqlTokenScanner var2 = this.createScanner(var1);
      SoqlTokenScanner.Result var3 = var2.doScan();
      if (var3.isValid()) {
         String var4 = var3.getObjectName();
         List var5 = var3.getFieldNameList();
         ImmutableList var6 = var3.getNestedQueries();
         return new ParsedQuery(var4, var5, var6);
      } else {
         return ParsedQuery.invalid(var3.getErrorMessage());
      }
   }

   private SoqlTokenScanner createScanner(String var1) {
      String var2 = DataElementResolver.resolveDefaultValues(var1);
      return new SoqlTokenScanner(var2);
   }

   @Override
   public String extractObjectName(String var1) {
      SoqlTokenScanner var2 = this.createScanner(var1);
      SoqlTokenScanner.Result var3 = var2.doScan();
      return var3.isValid() ? var3.getObjectName() : null;
   }
}
