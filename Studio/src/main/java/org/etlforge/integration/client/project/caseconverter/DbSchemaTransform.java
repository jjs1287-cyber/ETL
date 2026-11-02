package org.etlforge.integration.client.project.caseconverter;

import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Pair;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.util.string.Case;

public final class DbSchemaTransform {
   private final BeginEndQuote oldQuotes;
   private final String newSchema;
   private final BeginEndQuote newQuotes;
   private final Case newCase;

   private DbSchemaTransform(BeginEndQuote var1, String var2, BeginEndQuote var3, Case var4) {
      this.oldQuotes = var1;
      this.newSchema = var2;
      this.newQuotes = var3;
      this.newCase = var4;
   }

   public static DbSchemaTransform oracleToSqlServer() {
      return new DbSchemaTransform(BeginEndQuote.ORACLE, "", BeginEndQuote.SQL_SERVER, Case.LOWER);
   }

   public static DbSchemaTransform sqlServerToOracle(String var0) {
      return new DbSchemaTransform(BeginEndQuote.SQL_SERVER, var0, BeginEndQuote.ORACLE, Case.UPPER);
   }

   public static DbSchemaTransform oracleSchemaChange(String var0) {
      return new DbSchemaTransform(BeginEndQuote.ORACLE, var0, BeginEndQuote.ORACLE, Case.UPPER);
   }

   public String getNewSchemaName() {
      return this.newSchema;
   }

   public BeginEndQuote getOldQuotes() {
      return this.oldQuotes;
   }

   public BeginEndQuote getNewQuotes() {
      return this.newQuotes;
   }

   public Case getNewCase() {
      return this.newCase;
   }

   public String convertCase(String var1) {
      return this.newCase.convert(var1);
   }

   public String deriveOldSchemaName(String var1) {
      String var2 = StringUtils.substringBetween(var1, this.oldQuotes.begin, this.oldQuotes.end);
      if (StringUtils.isEmpty(var2)) {
         return null;
      }

      String var3 = this.oldQuotes.escapeName(var2);
      if (var3.length() < var1.length()) {
         char var4 = var1.charAt(var3.length());
         if (var4 == '.') {
            return var2;
         }
      }

      return null;
   }

   public Pair<String, String> deriveOldSchemaNameAndTableName(String var1) {
      String var2 = this.deriveOldSchemaName(var1);
      if (var2 == null) {
         return Pair.of(null, var1);
      }

      String var3 = var1.substring(this.oldQuotes.escapeName(var2).length() + 1);
      String var4 = this.oldQuotes.unescapeName(var3);
      return Pair.of(var2, var4);
   }

   public String transformTableName(String var1) {
      String var2;
      if (StringUtils.isEmpty(this.newSchema)) {
         var2 = var1;
      } else {
         var2 = this.newQuotes.getQualifiedName(this.newSchema, var1);
      }

      return this.newCase.convert(var2);
   }
}
