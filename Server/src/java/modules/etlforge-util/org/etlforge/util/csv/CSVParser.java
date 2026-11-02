package org.etlforge.util.csv;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CSVParser {
   private long pos0;
   private long pos1;
   private long pos2 = -1L;
   private List<String> lastRecord;
   private final FileContents fc;
   private final CSVParams params;

   public CSVParser(FileContents var1) {
      this(var1, CSVParams.DEFAULT);
   }

   public CSVParser(FileContents var1, CSVParams var2) {
      this.fc = var1;
      this.params = var2;
   }

   public long readRecord(long var1) throws IOException {
      return this.readRecord(var1, false);
   }

   public long readRecord(long var1, boolean var3) throws IOException {
      if (var3) {
         this.lastRecord = Lists.newArrayList();
      }

      var1 = this.skipLineBreak(var1);
      if (var1 < 0L) {
         return var1;
      }

      byte var4 = this.fc.at(var1);
      if (var4 == 0) {
         return -1L;
      }

      this.pos0 = var1;

      for (var1 = this.readValue(var1); !this.isEndOfRecord(var1); var1 = this.readValue(var1)) {
         if (var3) {
            this.storeValue();
         }
      }

      if (var3) {
         this.storeValue();
         if (this.pos2 < var1) {
            this.pos1 = this.pos2 = var1;
            this.storeValue();
         }
      }

      this.pos2 = var1;
      return var1;
   }

   private void storeValue() {
      this.lastRecord.add(this.read(this.pos1, this.pos2));
   }

   public String read(long var1, long var3) {
      return this.fc.getStr(var1, (int)(var3 - var1));
   }

   public List<String> getValuesForLastRecord() {
      return this.lastRecord;
   }

   public List<String> getDecodedValuesForLastRecord() {
      ArrayList var1 = Lists.newArrayList();

      for (String var3 : this.getValuesForLastRecord()) {
         var1.add(this.CVSDecode(var3));
      }

      return var1;
   }

   private String CVSDecode(String var1) {
      return CVSDecode(var1, this.params.qualifier);
   }

   public static String CVSDecode(String var0, char var1) {
      int var2 = var0.length() - 1;
      if (var0.length() > 1 && var0.charAt(0) == var1 && var0.charAt(var2) == var1) {
         StringBuilder var3 = new StringBuilder();

         for (int var5 = 1; var5 < var2; var5++) {
            char var4 = var0.charAt(var5);
            if (var4 == var1 && var5 < var2 && var0.charAt(var5 + 1) == var1) {
               var5++;
            }

            var3.append(var4);
         }

         return var3.toString();
      } else {
         return var0;
      }
   }

   public long skipLineBreak(long var1) throws IOException {
      if (var1 < 0L) {
         return var1;
      }

      byte var3 = this.fc.at(var1);

      while (var3 != 0 && (var3 == 13 || var3 == 10)) {
         var3 = this.fc.at(++var1);
      }

      return var1;
   }

   public long getBeginRecordPos() {
      return this.pos0;
   }

   public long getEndRecordPos() {
      return this.pos2;
   }

   public boolean isEndOfRecord(long var1) throws IOException {
      byte var3 = this.fc.at(var1);
      return var3 == 13 || var3 == 10 || var3 == 0;
   }

   public boolean isEndOfFile(long var1) throws IOException {
      return this.fc.at(var1) == 0;
   }

   public long readValue(long var1) throws IOException {
      byte var3 = (byte)this.params.qualifier;
      byte var4 = (byte)this.params.delimiter;
      byte var5 = this.fc.at(var1);
      this.pos1 = var1;
      if (var5 == var3) {
         label48:
         while (true) {
            if ((var5 = this.fc.at(++var1)) != 0) {
               if (var5 != var3) {
                  continue;
               }

               var5 = this.fc.at(++var1);
               if (var5 == var3) {
                  continue;
               }
            }

            this.pos2 = var1;

            while (true) {
               if (var5 == 0 || var5 == var4 || var5 == 13 || var5 == 10 || var5 == 0) {
                  break label48;
               }

               var5 = this.fc.at(++var1);
            }
         }
      } else {
         while (var5 != 0 && var5 != var4 && var5 != 13 && var5 != 10 && var5 != 0) {
            var5 = this.fc.at(++var1);
         }

         this.pos2 = var1;
      }

      if (var5 == var4) {
         this.fc.at(++var1);
      }

      return var1;
   }

   public static int readValue(String var0, CSVParams var1, int var2) throws Exception {
      int var3 = var0.length();
      if (var2 >= var3) {
         return var2;
      }

      char var4 = var0.charAt(var2);
      if (var4 == var1.qualifier) {
         for (boolean var5 = false; !var5; var5 = (var4 = var0.charAt(var2)) != var1.qualifier) {
            var2 = var0.indexOf(var1.qualifier, ++var2);
            if (var2 < 0) {
               throw new Exception("missing closing quote, readValue error\n" + var0);
            }

            if (++var2 >= var3) {
               return var2;
            }
         }

         while (var4 != 0 && var4 != var1.delimiter && var4 != '\r' && var4 != '\n' && var4 != 0) {
            if (++var2 >= var3) {
               return var2;
            }

            var4 = var0.charAt(var2);
         }
      } else {
         while (var4 != 0 && var4 != var1.delimiter && var4 != '\r' && var4 != '\n' && var4 != 0) {
            if (++var2 >= var3) {
               return var2;
            }

            var4 = var0.charAt(var2);
         }
      }

      return var2;
   }

   public void readRecord(String var1, List<String> var2) throws Exception {
      readRecord(var1, var2, this.params);
   }

   public static void readRecord(String var0, List<String> var1, CSVParams var2) throws Exception {
      var1.clear();
      int var3 = 0;
      int var5 = var0.length();

      int var4;
      while ((var4 = readValue(var0, var2, var3)) <= var5) {
         var1.add(var0.substring(var3, var4));
         var3 = var4 + 1;
      }
   }

   public String converToLine(List<String> var1, int var2) {
      return converToLine(var1, this.params.delimiter, var2);
   }

   public static String converToLine(List<String> var0, char var1, int var2) {
      int var3 = var2 <= var0.size() && var2 >= 0 ? var2 : var0.size();
      StringBuilder var4 = new StringBuilder(1000);

      int var5;
      for (var5 = 0; var5 < var3; var5++) {
         if (var5 > 0) {
            var4.append(var1);
         }

         var4.append((String)var0.get(var5));
      }

      while (var5 < var2) {
         var4.append(var1);
         var5++;
      }

      return var4.toString();
   }

   public int getColumnNumber(String var1) {
      if (var1 == null) {
         return -2;
      }

      if (var1.isEmpty()) {
         return -1;
      }

      ArrayList var2 = Lists.newArrayList();

      try {
         readRecord(var1, var2, this.params);
      } catch (Exception var4) {
         var4.printStackTrace();
         return -3;
      }

      return var2.size();
   }

   public static String CSVEncodeOptional(String var0, char var1, char var2) {
      return requireCSVQualifier(var0, var1, var2) ? CSVEncode(var0, var1) : var0;
   }

   public static String CSVEncodeLineDefault(boolean var0, String... var1) {
      return CSVEncodeLine(var0, CSVParams.DEFAULT.qualifier, CSVParams.DEFAULT.delimiter, var1);
   }

   public static String CSVEncodeLine(boolean var0, char var1, char var2, String... var3) {
      StringBuilder var4 = new StringBuilder(1000);
      boolean var5 = false;

      for (String var9 : var3) {
         if (var5) {
            var4.append(var2);
         }

         var5 = true;
         if (var0) {
            var4.append(CSVEncodeOptional(var9, var1, var2));
         } else {
            var4.append(CSVEncode(var9, var1));
         }
      }

      var4.append('\n');
      return var4.toString();
   }

   private static boolean contains(char[] var0, char var1) {
      for (char var5 : var0) {
         if (var1 == var5) {
            return true;
         }
      }

      return false;
   }

   private static boolean containAny(String var0, char[] var1) {
      for (int var2 = 0; var2 < var0.length(); var2++) {
         if (contains(var1, var0.charAt(var2))) {
            return true;
         }
      }

      return false;
   }

   public static boolean requireCSVQualifier(String var0, char var1, char var2) {
      if (var0 != null && !var0.isEmpty()) {
         if (var0.charAt(0) == var1) {
            return true;
         }

         char[] var3 = new char[]{var2, '\r', '\n'};
         return containAny(var0, var3);
      } else {
         return false;
      }
   }

   public static String CSVEncode(String var0, char var1) {
      if (var0 == null) {
         return "";
      }

      int var2 = 0;
      StringBuilder var4 = new StringBuilder(1024);
      var4.append(var1);

      int var3;
      for (var3 = 0; var3 < var0.length(); var3++) {
         if (var0.charAt(var3) == var1) {
            if (var2 < var3) {
               var4.append(var0.substring(var2, var3));
            }

            var4.append(var1).append(var1);
            var2 = var3 + 1;
         }
      }

      if (var2 < var3) {
         var4.append(var0.substring(var2, var3));
      }

      var4.append(var1);
      return var4.toString();
   }
}
