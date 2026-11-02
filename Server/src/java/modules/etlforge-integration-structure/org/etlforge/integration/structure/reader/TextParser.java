package org.etlforge.integration.structure.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.util.csv.FileContents;

public class TextParser implements DocParser {
   protected char qualifier;
   protected char delimiter;
   protected String delimiter_str;
   protected int delimiter_len;
   protected boolean isFlat = false;
   protected long pos0 = 0L;
   protected long pos1 = 0L;
   protected long pos2 = -1L;
   protected long id_pos1;
   protected long id_pos2;
   protected boolean bNull = false;
   int endValuePosInString = -1;
   protected FileContents fc;

   public TextParser(FileContents var1, char var2, char var3, String var4, boolean var5) {
      this.fc = var1;
      this.qualifier = var2;
      this.delimiter = var3;
      this.delimiter_str = var4;
      this.delimiter_len = var4.length();
      this.isFlat = var5;
   }

   @Override
   public long getBeginRecordPos() {
      return this.pos0;
   }

   @Override
   public long getEndRecordPos() {
      return this.pos2;
   }

   public boolean isNull() {
      return this.bNull;
   }

   protected long locateNextQualifer(char var1, long var2) throws IOException {
      byte var4;
      while ((var4 = this.fc.at(++var2)) != 0) {
         if ((byte)var1 == var4) {
            var4 = this.fc.at(++var2);
            if (var4 == 0) {
               return var2;
            }
         } else if ((byte)this.qualifier == var4) {
            return var2;
         }
      }

      return var2;
   }

   @Override
   public long readValue(long var1) throws IOException {
      byte var3 = this.fc.at(var1);
      this.pos1 = var1;
      if (var3 == this.qualifier) {
         var1 = this.locateNextQualifer('\\', var1);
         var3 = this.fc.at(++var1);
         this.pos2 = var1;

         while (var3 != 13 && var3 != 10 && var3 != 0 && (var3 != (byte)this.delimiter || !this.fc.checkDelimiter(var1, this.delimiter_str))) {
            var3 = this.fc.at(++var1);
         }
      } else {
         while (var3 != 13 && var3 != 10 && var3 != 0 && (var3 != (byte)this.delimiter || !this.fc.checkDelimiter(var1, this.delimiter_str))) {
            var3 = this.fc.at(++var1);
         }

         this.pos2 = var1;
      }

      if (var3 == (byte)this.delimiter && this.fc.checkDelimiter(var1, this.delimiter_str)) {
         var1 += this.delimiter_str.length();
      }

      this.bNull = this.pos2 == this.pos1;
      return var1;
   }

   public boolean isEndOfRecord(long var1) throws IOException {
      byte var3 = this.fc.at(var1);
      return var3 == 13 || var3 == 10 || var3 == 0;
   }

   public long skipLineBreak(long var1) throws IOException {
      byte var3 = this.fc.at(var1);

      while (var3 == 13 || var3 == 10) {
         var3 = this.fc.at(++var1);
      }

      return var1;
   }

   public boolean isEndOfFile(long var1) throws IOException {
      return this.fc.at(var1) == 0;
   }

   @Override
   public String getIdentifier() throws IOException {
      if (this.isFlat) {
         return "";
      }

      String var1 = this.fc.getStr(this.id_pos1, (int)(this.id_pos2 - this.id_pos1));
      int var2 = var1.length();
      return var2 > 2 && var1.charAt(0) == this.qualifier && var1.charAt(var2 - 1) == this.qualifier ? var1.substring(1, var2 - 1) : var1;
   }

   public String getValue(long var1, int var3) throws IOException {
      if (this.bNull) {
         return null;
      }

      String var4 = this.fc.getStr(var1, var3);
      return this.getValue(var4);
   }

   @Override
   public long readRecord(long var1) throws IOException {
      byte var3 = this.fc.at(var1);

      while (var3 == 10 || var3 == 13) {
         var3 = this.fc.at(++var1);
         if (var3 == 0) {
            break;
         }
      }

      if (var3 == 0) {
         return -1L;
      }

      this.pos0 = var1;
      var1 = this.readValue(var1);
      this.id_pos1 = this.pos1;
      this.id_pos2 = this.pos2;

      while (!this.isEndOfRecord(var1)) {
         var1 = this.readValue(var1);
      }

      this.pos2 = var1;
      return var1;
   }

   public boolean isClosingTag() {
      return false;
   }

   protected int locateNextQualifer(String var1, char var2, int var3) {
      int var5 = var1.length();
      var3++;

      char var4;
      for (; (var4 = var1.charAt(var3)) != 0; var3++) {
         if (var2 == var4) {
            if (++var3 >= var5) {
               return var5;
            }
         } else if (this.qualifier == var4) {
            return var3;
         }
      }

      return var3;
   }

   private int readValue(String var1, int var2, String var3) {
      if (var2 == 0 && var3 != null) {
         int var5 = var3.length();
         if (var1.startsWith(var3)) {
            int var6 = var1.length();
            if (var6 == var5) {
               this.endValuePosInString = var5;
               return var5;
            }

            char var4 = var1.charAt(var5);
            if (var6 > var5) {
               if (var4 == this.delimiter) {
                  this.endValuePosInString = var5;
                  return var5 + 1;
               }

               if (var4 == '\r' || var4 == '\n') {
                  return var5;
               }
            }
         }
      }

      char var7 = var1.charAt(var2);
      int var8 = var2;
      int var9 = var1.length();
      if (var7 == this.qualifier) {
         var2 = this.locateNextQualifer(var1, '\\', var2);
         if (var2 < var9) {
            for (this.endValuePosInString = ++var2; var2 < var9; var2++) {
               var7 = var1.charAt(var2);
               if (var7 == '\r' || var7 == '\n' || var7 == this.delimiter) {
                  break;
               }
            }
         } else {
            this.endValuePosInString = var2;
         }
      } else {
         while (var7 != '\r' && var7 != '\n' && var7 != 0 && var7 != this.delimiter) {
            if (++var2 >= var9) {
               break;
            }

            var7 = var1.charAt(var2);
         }

         this.endValuePosInString = var2;
      }

      if (var7 == this.delimiter) {
         var2 += this.delimiter_str.length();
      }

      this.bNull = this.endValuePosInString == var8;
      return var2;
   }

   @Override
   public String getStr(long var1, int var3) throws IOException {
      return var3 <= 0 ? "" : this.fc.getStr(var1, var3);
   }

   @Override
   public List<String> splitValues(JbNode var1, boolean var2) throws IOException {
      CROM var3 = var1.getCrom();
      if (var1.getLength() == 0) {
         return null;
      }

      ArrayList var4 = new ArrayList();
      if (var3 == null) {
         long var5 = var1.getPos();

         while (!this.isEndOfRecord(var5)) {
            var5 = this.readValue(var5);
            String var7 = this.isNull() ? "NULL" : this.getValue(this.pos1, (int)(this.pos2 - this.pos1));
            var4.add(var7);
         }
      } else {
         String var13 = this.fc.getStr(var1.getPos(), var1.getLength());
         Iterator var6 = var3.getChildren().iterator();
         int var8 = 0;
         int var9 = var13.length();
         if (!this.isFlat && (var3.isDatabase() || var3.isLdap())) {
            var8 = this.readValue(var13, var8, null);
         }

         String var11 = var3.getSegmentIdentifier();

         CROM var14;
         for (var14 = CROM.getNextValueAttribute(var6); var8 < var9; var14 = CROM.getNextValueAttribute(var6)) {
            int var10 = this.readValue(var13, var8, var11);
            String var12 = null;
            if (!this.isNull()) {
               var12 = this.getValue(var13.substring(var8, this.endValuePosInString));
            }

            if (var2 && var14 != null) {
               var12 = var14 + "=" + var12;
            }

            var4.add(var12);
            var8 = var10;
         }

         while (var14 != null) {
            var4.add(null);
            var14 = CROM.getNextValueAttribute(var6);
         }
      }

      return var4;
   }

   private String getValue(String var1) {
      return this.getValue(var1, this.qualifier);
   }

   @Override
   public String getValue(String var1, char var2) {
      int var3 = var1.length();
      if (var3 < 2) {
         return var1;
      }

      char var4 = var1.charAt(0);
      if (var4 != var2) {
         return var1;
      }

      StringBuilder var5 = new StringBuilder(var3);

      for (int var6 = 1; var6 < var3; var6++) {
         var4 = var1.charAt(var6);
         if (var4 == 0 || var6 == var3 - 1 && var4 == var2) {
            break;
         }

         if (var4 == '\\') {
            if (++var6 == var3 - 1) {
               throw new RuntimeException("last character is escape, the escaped character is missing.");
            }

            var4 = var1.charAt(var6);
            switch (var4) {
               case '"':
               case '\'':
               case '\\':
                  break;
               case 'b':
                  var4 = '\b';
                  break;
               case 'f':
                  var4 = '\f';
                  break;
               case 'n':
                  var4 = '\n';
                  break;
               case 'r':
                  var4 = '\r';
                  break;
               case 't':
                  var4 = '\t';
                  break;
               default:
                  var5.append('\\');
            }
         }

         var5.append(var4);
      }

      return var5.toString();
   }

   public static void main(String[] var0) {
   }
}
