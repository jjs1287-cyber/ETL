package org.etlforge.integration.structure.reader;

import java.io.IOException;
import org.etlforge.util.csv.FileContents;

public class CSVParser extends TextParser {
   public CSVParser(FileContents var1, char var2, char var3, String var4, boolean var5) {
      super(var1, var2, var3, var4, var5);
   }

   @Override
   protected long locateNextQualifer(char var1, long var2) throws IOException {
      byte var4;
      while ((var4 = this.fc.at(++var2)) != 0) {
         if (var4 == (byte)this.qualifier) {
            var4 = this.fc.at(++var2);
            if (var4 == 0) {
               return var2;
            }

            if (var4 != (byte)this.qualifier) {
               return var2 - 1L;
            }
         }
      }

      return var2;
   }

   @Override
   protected int locateNextQualifer(String var1, char var2, int var3) {
      int var5 = var1.length();

      char var4;
      while ((var4 = var1.charAt(++var3)) != 0) {
         if (var4 == this.qualifier) {
            if (++var3 >= var5) {
               return var5;
            }

            var4 = var1.charAt(var3);
            if (var4 != this.qualifier) {
               return var3 - 1;
            }
         }
      }

      return var3;
   }

   @Override
   public long readValue(long var1) throws IOException {
      byte var3 = this.fc.at(var1);
      this.pos1 = var1;
      if (var3 == this.qualifier) {
         this.bNull = false;
         var1 = this.locateNextQualifer(this.qualifier, var1);
         var3 = this.fc.at(var1);
         this.pos2 = var1;

         while (var3 != 0 && var3 != 13 && var3 != 10 && var3 != 0 && (var3 != this.delimiter || !this.fc.checkDelimiter(var1, this.delimiter_str))) {
            var3 = this.fc.at(++var1);
         }
      } else {
         while (var3 != 0 && var3 != 13 && var3 != 10 && var3 != 0 && (var3 != this.delimiter || !this.fc.checkDelimiter(var1, this.delimiter_str))) {
            var3 = this.fc.at(++var1);
         }

         this.pos2 = var1;
         this.bNull = this.pos2 == this.pos1;
      }

      if (var3 == this.delimiter && this.fc.checkDelimiter(var1, this.delimiter_str)) {
         var1 += this.delimiter_str.length();
      }

      return var1;
   }

   @Override
   public String getValue(long var1, int var3) throws IOException {
      if (this.bNull) {
         return null;
      }

      String var4 = this.fc.getStr(this.pos1, var3);
      return this.getValue(var4);
   }

   private String getValue(String var1) {
      return this.getValue(var1, this.qualifier);
   }

   @Override
   public String getValue(String var1, char var2) {
      if (var1 != null && var1.length() >= 2) {
         String var3 = new String() + var2 + var2;
         if (var1.charAt(0) == var2) {
            int var4 = 1;
            int var5 = var1.length() - 1;
            StringBuilder var7 = new StringBuilder();

            int var6;
            while ((var6 = var1.indexOf(var3, var4)) > 0 && var6 < var5) {
               var7.append(var1.substring(var4, var6 + 1));
               var4 = var6 + 2;
            }

            if (var4 == 1) {
               return var1.substring(var4, var5);
            }

            if (var4 < var5) {
               var7.append(var1.substring(var4, var5));
            }

            var1 = var7.toString();
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void main(String[] var0) {
      byte var1 = 13;
      System.out.println(13 == var1 ? "true" : "false");
   }
}
