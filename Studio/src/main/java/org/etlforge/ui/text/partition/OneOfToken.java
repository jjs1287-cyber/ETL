package org.etlforge.ui.text.partition;

import java.util.Collection;

public class OneOfToken implements Token {
   private final String[] values;
   private final boolean caseSensitive;
   private final boolean wholeWord;
   private String lexeme;
   private int offset;

   public OneOfToken(String[] var1, boolean var2) {
      this(var1, var2, false);
   }

   public OneOfToken(String[] var1, boolean var2, boolean var3) {
      this.values = var1;
      this.caseSensitive = var2;
      this.wholeWord = var3;
      this.offset = -1;
   }

   public OneOfToken(Collection<String> var1, boolean var2) {
      this(var1.toArray(new String[var1.size()]), var2);
   }

   public OneOfToken(Collection<String> var1, boolean var2, boolean var3) {
      this(var1.toArray(new String[var1.size()]), var2, var3);
   }

   @Override
   public String lexeme() {
      return this.lexeme;
   }

   @Override
   public final int offset() {
      return this.offset;
   }

   @Override
   public final boolean isWhitespace() {
      return false;
   }

   @Override
   public final boolean matches(String var1, int var2) {
      for (String var6 : this.values) {
         if (var1.length() - var2 >= var6.length()) {
            String var7 = var1.substring(var2, var2 + var6.length());
            boolean var8 = this.caseSensitive ? var7.equals(var6) : var7.equalsIgnoreCase(var6);
            if (var8 && (!this.wholeWord || this.checkWordBoundary(var1, var2, var6))) {
               this.lexeme = var6;
               this.offset = var2;
               return true;
            }
         }
      }

      this.lexeme = null;
      this.offset = -1;
      return false;
   }

   private boolean checkWordBoundary(String var1, int var2, String var3) {
      int var4 = var2 + var3.length();
      if (var4 == var1.length()) {
         return true;
      }

      char var5 = var1.charAt(var4);
      return !Character.isJavaIdentifierStart(var5) && !Character.isJavaIdentifierPart(var5);
   }

   @Override
   public final String toString() {
      return this.lexeme() + " @" + this.offset();
   }
}
