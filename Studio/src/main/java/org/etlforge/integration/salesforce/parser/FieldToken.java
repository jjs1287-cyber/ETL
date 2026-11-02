package org.etlforge.integration.salesforce.parser;

import com.google.common.base.Optional;
import org.apache.commons.lang.StringUtils;

final class FieldToken implements SoqlToken {
   private String lexeme;
   private int offset;
   private String fieldName;

   @Override
   public SoqlTokenId getId() {
      return SoqlTokenId.FIELD;
   }

   @Override
   public boolean matches(String var1, int var2) {
      boolean var3 = this.lookForToLabelSyntax(var1, var2);
      if (var3) {
         return true;
      } else {
         Optional var4 = NameToken.matches(var1, var2);
         if (var4.isPresent()) {
            this.lexeme = (String)var4.get();
            this.fieldName = this.lexeme;
            this.offset = var2;
            return true;
         } else {
            this.lexeme = null;
            this.fieldName = null;
            this.offset = -1;
            return false;
         }
      }
   }

   private boolean lookForToLabelSyntax(String var1, int var2) {
      String var3 = var1.substring(var2);
      String var4 = "toLabel(";
      if (!StringUtils.startsWithIgnoreCase(var3, var4)) {
         return false;
      }

      int var5 = var3.indexOf(41, var4.length());
      if (var5 == -1) {
         return false;
      }

      this.fieldName = var3.substring(var4.length(), var5);
      this.offset = var2;
      this.lexeme = var1.substring(var2, var2 + var5 + 1);
      return true;
   }

   @Override
   public String lexeme() {
      return this.lexeme;
   }

   @Override
   public int offset() {
      return this.offset;
   }

   @Override
   public boolean isWhitespace() {
      return false;
   }

   public String getFieldName() {
      return this.fieldName;
   }
}
