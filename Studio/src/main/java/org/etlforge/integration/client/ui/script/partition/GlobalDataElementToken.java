package org.etlforge.integration.client.ui.script.partition;

import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.ui.text.partition.Token;

public class GlobalDataElementToken implements Token {
   private String lexeme;
   private int offset = -1;

   @Override
   public boolean isWhitespace() {
      return false;
   }

   @Override
   public String lexeme() {
      return this.lexeme;
   }

   @Override
   public boolean matches(String var1, int var2) {
      if (GlobalDE.isMarker(var1.charAt(var2))) {
         int var3 = this.getEndOffset(var1, var2);
         if (var3 - var2 > 1) {
            this.offset = var2;
            this.lexeme = var1.substring(var2, var3);
            return true;
         }
      }

      this.offset = -1;
      this.lexeme = null;
      return false;
   }

   @Override
   public int offset() {
      return this.offset;
   }

   private int getEndOffset(String var1, int var2) {
      int var3 = var2 + 1;

      for (int var4 = var1.length(); var3 < var4; var3++) {
         char var5 = var1.charAt(var3);
         if (!isValid(var5)) {
            break;
         }
      }

      return var3;
   }

   private static boolean isValid(char var0) {
      switch (var0) {
         case '!':
         case '"':
         case '&':
         case '\'':
         case '(':
         case ')':
         case '*':
         case '+':
         case ',':
         case '-':
         case '/':
         case ';':
         case '<':
         case '=':
         case '>':
         case '[':
         case ']':
         case '^':
         case '{':
         case '|':
         case '}':
            return false;
         case '#':
         case '$':
         case '%':
         case '.':
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case ':':
         case '?':
         case '@':
         case 'A':
         case 'B':
         case 'C':
         case 'D':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '\\':
         case '_':
         case '`':
         case 'a':
         case 'b':
         case 'c':
         case 'd':
         case 'e':
         case 'f':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'm':
         case 'n':
         case 'o':
         case 'p':
         case 'q':
         case 'r':
         case 's':
         case 't':
         case 'u':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         case 'z':
         default:
            return !Character.isWhitespace(var0);
      }
   }

   @Override
   public String toString() {
      return this.lexeme + " @" + this.offset;
   }
}
