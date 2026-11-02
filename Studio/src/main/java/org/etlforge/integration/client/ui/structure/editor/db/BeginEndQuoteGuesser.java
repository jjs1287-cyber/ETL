package org.etlforge.integration.client.ui.structure.editor.db;

import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

final class BeginEndQuoteGuesser {
   public static BeginEndQuote guess(DatabaseObject[] var0) {
      for (DatabaseObject var4 : var0) {
         BeginEndQuote var5 = guess(var4);
         if (var5 != null) {
            return var5;
         }
      }

      return BeginEndQuote.BLANK;
   }

   static BeginEndQuote guess(DatabaseObject var0) {
      String var1 = var0.getSchema();
      if (StringUtils.isEmpty(var1)) {
         return null;
      }

      String var2 = var0.getName();
      int var3 = var2.indexOf(var1);
      if (var3 == -1) {
         return null;
      }

      int var4 = var3 + var1.length();
      int var5 = var2.indexOf(46, var4);
      return var5 == var4 + 1 ? createQuotes(var2, var4, var5) : null;
   }

   private static BeginEndQuote createQuotes(String var0, int var1, int var2) {
      String var3 = String.valueOf(var0.charAt(0));
      String var4 = var0.substring(var1, var2);
      return BeginEndQuote.fromStrings(var3, var4);
   }

   private BeginEndQuoteGuesser() {
   }
}
