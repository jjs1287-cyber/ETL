package org.etlforge.util.csv;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public final class CsvUtils {
   public static String toRecord(Collection<String> var0) {
      return toRecord(var0, CSVParams.DEFAULT);
   }

   public static String toRecord(Collection<String> var0, CSVParams var1) {
      return toRecord(var0.toArray(new String[var0.size()]), var1);
   }

   public static String toRecord(String[] var0, CSVParams var1) {
      try {
         StringWriter var2 = new StringWriter();
         CsvListWriter var3 = new CsvListWriter(var2, singleLinePrefs(var1));
         var3.write(var0);
         var3.close();
         return var2.toString().trim();
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static CsvPreference singleLinePrefs(CSVParams var0) {
      String var1 = "\n";
      return new CsvPreference(var0.qualifier, var0.delimiter, var1);
   }

   public static ImmutableList<String> toValues(String var0) {
      return toValues(var0, CSVParams.DEFAULT);
   }

   public static ImmutableList<String> toValues(String var0, CSVParams var1) {
      try {
         CsvListReader var2 = new CsvListReader(new StringReader(var0), singleLinePrefs(var1));
         List var3 = var2.read();
         return ImmutableList.copyOf(var3);
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   private CsvUtils() {
   }
}
