package org.etlforge.integration.client.database.ui.sqlbuilder;

import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

public final class NameGenerators {
   public static final NameGenerator SIMPLE = new NameGenerator() {
      @Override
      public String getTableName(DatabaseObject var1) {
         return var1.getName();
      }

      @Override
      public String getColumnName(DatabaseColumn var1) {
         return var1.getName();
      }
   };

   public static NameGenerator forDatabase(DriverDescriptor var0, BeginEndQuote var1) {
      return new NameGenerators.Impl(var0, var1);
   }

   private NameGenerators() {
   }

   private static class Impl implements NameGenerator {
      private final DriverDescriptor driver;
      private final BeginEndQuote beginEndQuote;

      public Impl(DriverDescriptor var1, BeginEndQuote var2) {
         this.driver = var1;
         this.beginEndQuote = var2;
      }

      @Override
      public String getTableName(DatabaseObject var1) {
         String var2 = var1.getName();
         return this.encloseInQuotesIfNeeded(var2);
      }

      private String encloseInQuotesIfNeeded(String var1) {
         return this.needsQuotes(var1) ? this.beginEndQuote.escapeName(var1) : var1;
      }

      private boolean needsQuotes(String var1) {
         if (var1.contains(" ") || var1.contains(".") || var1.contains(",")) {
            return true;
         } else {
            return this.driver.isProbablyPostgreSQL() ? !var1.equals(var1.toLowerCase()) : false;
         }
      }

      @Override
      public String getColumnName(DatabaseColumn var1) {
         String var2 = var1.getName();
         return this.encloseInQuotesIfNeeded(var2);
      }
   }
}
