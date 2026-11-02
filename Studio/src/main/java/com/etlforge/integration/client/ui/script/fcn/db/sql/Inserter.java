package com.etlforge.integration.client.ui.script.fcn.db.sql;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGenerator;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.lang.KongaStringUtils;

final class Inserter {
   private final NameGenerator nameGenerator;
   private final String context;
   private final int position;
   private Template template = Template.SELECT;

   public Inserter(NameGenerator var1, String var2, int var3) {
      this(var1, var2, var3, var3);
   }

   public Inserter(NameGenerator var1, String var2, int var3, int var4) {
      this.nameGenerator = new QuoteEscapingNameGenerator(var1);
      this.context = removeSelection(var2, var3, var4);
      this.position = var3;
   }

   private static String removeSelection(String var0, int var1, int var2) {
      if (var1 == var2) {
         return var0;
      }

      if (var1 == 0) {
         return var0.substring(var2);
      }

      String var3 = var0.substring(0, var1);
      if (var2 >= var0.length()) {
         return var3;
      }

      String var4 = var0.substring(var2);
      return var3 + var4;
   }

   public void setTemplate(Template var1) {
      Preconditions.checkNotNull(var1, "template");
      this.template = var1;
   }

   public InsertionData getStringToInsertForTable(DatabaseObject var1) {
      String var2 = this.nameGenerator.getTableName(var1);
      return this.isEmptyExpression() ? this.template.getTableTemplate(var2) : new InsertionData(var2);
   }

   private boolean isEmptyExpression() {
      String var1 = removeQuotes(this.context);
      return StringUtils.isBlank(var1);
   }

   private static String removeQuotes(String var0) {
      return KongaStringUtils.removeStartAndEnd(var0, "\"");
   }

   public InsertionData getStringToInsertForColumn(DatabaseColumn var1) {
      return this.getStringToInsertForColumns(Collections.singletonList(var1));
   }

   public InsertionData getStringToInsertForColumns(List<DatabaseColumn> var1) {
      if (var1.isEmpty()) {
         return new InsertionData("");
      }

      if (this.isEmptyExpression()) {
         return this.template.getColumnTemplate(this.nameGenerator, var1);
      }

      String var2 = Utils.concatenateColumnNames(this.nameGenerator, var1);
      return new InsertionData(this.addLeadingAndTrailingCommasAsNeeded(var2));
   }

   private String addLeadingAndTrailingCommasAsNeeded(String var1) {
      String var2 = var1;
      if (this.isCommaNeededBeforeColumnName()) {
         var2 = ", " + var2;
      }

      if (this.isCommaNeededAfterColumnName()) {
         var2 = var2 + ", ";
      }

      return var2;
   }

   private boolean isCommaNeededBeforeColumnName() {
      if (this.position == 0) {
         return false;
      } else {
         char var1 = this.context.charAt(this.position - 1);
         if (Character.isLetterOrDigit(var1)) {
            return true;
         } else if (var1 == '"' && this.position - 2 > 0) {
            char var2 = this.context.charAt(this.position - 2);
            return Character.isLetterOrDigit(var2);
         } else {
            return false;
         }
      }
   }

   private boolean isCommaNeededAfterColumnName() {
      if (this.position >= this.context.length()) {
         return false;
      } else {
         char var1 = this.context.charAt(this.position);
         if (Character.isLetter(var1)) {
            return true;
         } else if (var1 == '"' && this.position < this.context.length() - 1) {
            char var2 = this.context.charAt(this.position + 1);
            return Character.isLetter(var2);
         } else {
            return false;
         }
      }
   }

   public InsertionData getStringToInsertForDataElement(String var1) {
      String var2 = "SqlEscape(" + var1 + ")";
      if (StringDetector.isInString(this.context, this.position)) {
         String var4 = "\" + " + var2;
         if (this.isBetweenSingleQuoteAndDoubleQuote()) {
            var4 = var4 + " + \"'";
         } else if (this.isInMiddleOfString()) {
            var4 = var4 + " + \"";
         }

         return new InsertionData(var4);
      } else {
         String var3 = var2;
         if (this.hasNonWhitespaceBeforeInsertPosition()) {
            var3 = " + " + var3;
         }

         if (this.isAfterSingleQuoteAndDoubleQuote()) {
            var3 = var3 + " + \"'\"";
         }

         return new InsertionData(var3);
      }
   }

   private boolean hasNonWhitespaceBeforeInsertPosition() {
      return KongaStringUtils.positionOfLastNoneWhitespaceBefore(this.context, this.position) >= 0;
   }

   private boolean isBetweenSingleQuoteAndDoubleQuote() {
      assert StringDetector.isInString(this.context, this.position);
      return this.position != 0 && this.position < this.context.length()
         ? this.context.charAt(this.position) == '"' && this.context.charAt(this.position - 1) == '\''
         : false;
   }

   private boolean isInMiddleOfString() {
      assert StringDetector.isInString(this.context, this.position);
      return this.position != 0 && this.position < this.context.length() - 1 ? this.context.charAt(this.position) != '"' : false;
   }

   private boolean isAfterSingleQuoteAndDoubleQuote() {
      assert !StringDetector.isInString(this.context, this.position);
      int var1 = KongaStringUtils.positionOfLastNoneWhitespaceBefore(this.context, this.position);
      return var1 < 1 ? false : this.context.charAt(var1) == '"' && this.context.charAt(var1 - 1) == '\'';
   }

   public InsertionData getStringToInsertForReservedWord(String var1) {
      return StringDetector.isInString(this.context, this.position)
         ? this.getReservedWordInsideString(var1)
         : new InsertionData(this.getReservedWordOutsideString(var1));
   }

   private InsertionData getReservedWordInsideString(String var1) {
      String var2 = var1;
      char var3 = this.context.charAt(this.position - 1);
      if (!Character.isWhitespace(var3)) {
         var2 = " " + var2;
      }

      if (this.position < this.context.length()) {
         char var4 = this.context.charAt(this.position);
         if (!Character.isWhitespace(var4)) {
            var2 = var2 + " ";
         }
      }

      return new InsertionData(var2);
   }

   private String getReservedWordOutsideString(String var1) {
      return " + \" " + var1 + " \"";
   }
}
