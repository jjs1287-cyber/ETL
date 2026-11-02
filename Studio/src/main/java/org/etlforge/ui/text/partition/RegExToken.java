package org.etlforge.ui.text.partition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExToken implements Token {
   private final Pattern pattern;
   private Matcher matcher;
   private String lexeme;
   private int offset;

   public RegExToken(String var1) {
      this.pattern = Pattern.compile(var1);
      this.offset = -1;
   }

   @Override
   public String lexeme() {
      return this.lexeme;
   }

   @Override
   public boolean isWhitespace() {
      return false;
   }

   @Override
   public boolean matches(String var1, int var2) {
      String var3 = var1.substring(var2, var1.length());
      this.matcher = this.pattern.matcher(var3);
      if (this.matcher.find() && this.matcher.start() == 0) {
         this.offset = var2;
         this.lexeme = var3.substring(0, this.matcher.end());
         return true;
      } else {
         this.offset = -1;
         this.lexeme = null;
         return false;
      }
   }

   @Override
   public int offset() {
      return this.offset;
   }

   @Override
   public String toString() {
      return this.lexeme + " @" + this.offset;
   }
}
