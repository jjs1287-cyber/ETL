package org.etlforge.integration.database.driver;

import org.etlforge.integration.database.BeginEndQuote;

public final class DatabaseDriverQuoteHandlerFactory {
   private DatabaseDriverQuoteHandlerFactory() {
   }

   public static DatabaseDriverQuoteHandler newInstance(BeginEndQuote var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("quotes cannot be null.");
      } else {
         return new DatabaseDriverQuoteHandlerFactory.ManagerImpl(var0);
      }
   }

   private static class ManagerImpl implements DatabaseDriverQuoteHandler {
      private final String beginQuote;
      private final String endQuote;

      public ManagerImpl(BeginEndQuote var1) {
         this.beginQuote = var1.begin;
         this.endQuote = var1.end;
      }

      @Override
      public boolean isNameWrapped(String var1) {
         return this.startsWithBeginQuote(var1) || this.endsWithEndQuote(var1);
      }

      @Override
      public String wrapName(String var1) {
         StringBuilder var2 = new StringBuilder();
         if (!this.startsWithBeginQuote(var1)) {
            var2.append(this.beginQuote);
         }

         var2.append(var1);
         if (!this.endsWithEndQuote(var1)) {
            var2.append(this.endQuote);
         }

         return var2.toString();
      }

      @Override
      public String unwrapName(String var1) {
         StringBuilder var2 = new StringBuilder(var1);
         if (this.startsWithBeginQuote(var1)) {
            var2.delete(0, this.beginQuote.length());
         }

         if (this.endsWithEndQuote(var1)) {
            var2.delete(var2.length() - this.endQuote.length(), var2.length());
         }

         return var2.toString();
      }

      private boolean startsWithBeginQuote(String var1) {
         return this.beginQuote.length() > 0 && var1.startsWith(this.beginQuote);
      }

      private boolean endsWithEndQuote(String var1) {
         return this.endQuote.length() > 0 && var1.endsWith(this.endQuote);
      }
   }
}
