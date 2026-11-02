package org.etlforge.integration.data.structure.database;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.StringTokenizer;
import org.etlforge.integration.database.BeginEndQuote;

public final class TableUsage {
   private final Multiset<String> usage = HashMultiset.create();
   private boolean schemaIncluded;

   public TableUsage(DbTables var1, BeginEndQuote var2) {
      this.parse(var1, var2);
   }

   public int getUsageCount(String var1) {
      return this.usage.count(var1);
   }

   public boolean isSchemaIncluded() {
      return this.schemaIncluded;
   }

   private void parse(DbTables var1, BeginEndQuote var2) {
      String var3 = var1.getTables();
      String[] var4 = var3.split(",");

      for (String var8 : var4) {
         TableUsage.Parser var9 = new TableUsage.Parser(var8, var2);
         String var10 = var9.parse();
         if (var9.isSchemaIncluded()) {
            this.schemaIncluded = true;
         }

         this.usage.add(var10);
      }
   }

   private static class Parser {
      private final String toParse;
      private final BeginEndQuote quotes;
      private final boolean isWrappedInBeginEndQuote;
      private final boolean wasSpaceEscaped;
      private String currentEntity;
      private String tableName;
      private String schemaName;
      private boolean insideQuotes;
      private boolean done;

      public Parser(String var1, BeginEndQuote var2) {
         this.quotes = var2;
         this.toParse = removePaddingQuotes(var1);
         this.isWrappedInBeginEndQuote = var2.begin.length() > 0 && this.toParse.startsWith(var2.begin);
         this.wasSpaceEscaped = !this.toParse.equals(var1);
      }

      public boolean isSchemaIncluded() {
         return this.schemaName != null;
      }

      public String parse() {
         this.reset();
         String var1 = this.getSeparators(this.quotes, this.isWrappedInBeginEndQuote);
         StringTokenizer var2 = new StringTokenizer(this.toParse, var1, true);

         while (!this.done && var2.hasMoreTokens()) {
            String var3 = var2.nextToken();
            if (this.isWrappedInBeginEndQuote && var3.equals(this.quotes.begin)) {
               this.handleBeginQuote();
            } else if (this.isWrappedInBeginEndQuote && var3.equals(this.quotes.end)) {
               this.handleEndQuote();
            } else if (var3.equals(" ")) {
               this.handleSpace();
            } else if (var3.equals(".")) {
               this.handleDot();
            } else {
               this.currentEntity = this.currentEntity + var3;
            }
         }

         if (this.tableName == null) {
            this.tableName = this.currentEntity;
         }

         return this.getParseResult();
      }

      private void handleBeginQuote() {
         if (this.insideQuotes) {
            if (this.quotes.begin.equals(this.quotes.end)) {
               this.handleEndQuote();
            } else {
               this.currentEntity = this.currentEntity + this.quotes.begin;
            }
         } else {
            this.insideQuotes = true;
         }
      }

      private void handleEndQuote() {
         this.insideQuotes = false;
         if (this.tableName == null) {
            this.tableName = this.currentEntity;
         } else {
            this.schemaName = this.tableName;
            this.tableName = null;
         }

         this.currentEntity = "";
      }

      private void handleSpace() {
         if (!this.insideQuotes && !this.wasSpaceEscaped) {
            this.done = true;
         } else {
            this.currentEntity = this.currentEntity + " ";
         }
      }

      private void handleDot() {
         if (this.insideQuotes || !this.isWrappedInBeginEndQuote) {
            this.currentEntity = this.currentEntity + ".";
         } else if (this.isWrappedInBeginEndQuote) {
            this.schemaName = this.tableName;
            this.tableName = null;
            this.currentEntity = "";
         }
      }

      private String getParseResult() {
         return this.schemaName != null
            ? this.quotes.escapeName(this.schemaName) + "." + this.quotes.escapeName(this.tableName)
            : this.quotes.escapeNameIfNeeded(this.tableName);
      }

      private String getSeparators(BeginEndQuote var1, boolean var2) {
         String var3 = " .";
         if (var2) {
            var3 = var3 + var1.begin;
            if (!var1.end.equals(var1.begin)) {
               var3 = var3 + var1.end;
            }
         }

         return var3;
      }

      private void reset() {
         this.schemaName = null;
         this.tableName = null;
         this.currentEntity = "";
         this.done = false;
         this.insideQuotes = false;
      }

      private static String removePaddingQuotes(String var0) {
         String var1 = DatabaseStructureUtils.unescape(var0);
         String var2 = var1.split("\" \"")[0];
         if (var2.contains("\".\"") && !var2.startsWith("\"")) {
            var2 = "\"" + var2 + "\"";
         }

         return var2;
      }
   }
}
