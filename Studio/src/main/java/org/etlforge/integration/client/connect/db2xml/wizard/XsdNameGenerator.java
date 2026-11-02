package org.etlforge.integration.client.connect.db2xml.wizard;

import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.SchemaAndTableNameSeparator;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.ManualSqlStatement;
import org.etlforge.util.file.WindowsFileNameValidator;

final class XsdNameGenerator {
   private final String dbName;
   private final BeginEndQuote quotes;
   private final Set<Character> invalidChars;
   private final Set<String> reservedWords;

   public XsdNameGenerator(String var1, BeginEndQuote var2) {
      this.dbName = var1;
      this.quotes = var2;
      this.invalidChars = Sets.newHashSet();

      for (char var6 : WindowsFileNameValidator.illegalChars()) {
         this.invalidChars.add(var6);
      }

      this.invalidChars.add(' ');
      this.reservedWords = Sets.newHashSet();

      for (String var10 : WindowsFileNameValidator.reservedWords()) {
         this.reservedWords.add(var10.toLowerCase());
      }
   }

   public String getXsdName(DatabaseObject var1) {
      if (var1 instanceof ManualSqlStatement) {
         String var8 = this.getNameFromSqlStatement((ManualSqlStatement)var1);
         return this.prependDatabaseName(var8);
      }

      StringBuilder var2 = new StringBuilder();
      String var3 = this.getNameToCheck(var1);

      for (char var7 : var3.toCharArray()) {
         if (this.invalidChars.contains(var7)) {
            var2.append("_");
         } else {
            var2.append(var7);
         }
      }

      if (this.reservedWords.contains(var2.toString().toLowerCase())) {
         var2.append("_");
      }

      var2.append(".xsd");
      return var2.toString();
   }

   private String prependDatabaseName(String var1) {
      return StringUtils.isEmpty(this.dbName) ? var1 : this.dbName + "." + var1;
   }

   private String getNameFromSqlStatement(ManualSqlStatement var1) {
      String var2 = var1.guessTableName();
      return StringUtils.isNotBlank(var2) ? var2 + ".xsd" : "";
   }

   private String getNameToCheck(DatabaseObject var1) {
      String var2 = var1.getName();
      SchemaAndTableNameSeparator var3 = new SchemaAndTableNameSeparator(var2, this.quotes);
      String var4 = var3.getSchema();
      String var5 = var3.getTable();
      String var6;
      if (StringUtils.isEmpty(var4)) {
         var6 = var5;
      } else {
         var6 = var4 + "." + var5;
      }

      return this.prependDatabaseName(var6);
   }
}
