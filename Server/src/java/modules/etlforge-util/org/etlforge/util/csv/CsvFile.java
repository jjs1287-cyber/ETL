package org.etlforge.util.csv;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.system.ArgCheck;

public final class CsvFile {
   private final Record header;
   private final List<Record> records;

   public static CsvFile empty() {
      return new CsvFile(Record.of());
   }

   public CsvFile() {
      this(Record.of());
   }

   public CsvFile(Record var1) {
      this.header = Preconditions.checkNotNull(var1);
      this.records = Lists.newArrayList();
   }

   public CsvFile(Record var1, List<Record> var2) {
      this.header = Preconditions.checkNotNull(var1);
      this.records = Lists.newArrayList(var2);
   }

   public void addRecord(Record var1) {
      this.records.add(Preconditions.checkNotNull(var1));
   }

   public Record getHeader() {
      return this.header;
   }

   public int getNumberOfRecords() {
      return this.records.size();
   }

   public int getNumberOfColumns() {
      if (this.isEmpty()) {
         return 0;
      }

      int var1 = this.header.size();
      if (var1 == 0) {
         var1 = this.records.get(0).size();
      }

      return var1;
   }

   public List<Record> getRecords() {
      return Lists.newArrayList(this.records);
   }

   public Record getRecordAt(int var1) {
      return this.records.get(var1);
   }

   public boolean isEmpty() {
      return this.header.isEmpty() && this.records.isEmpty();
   }

   public static CsvFile.Factory newFactory() {
      return new CsvFile.Factory();
   }

   public static final class Factory {
      private CSVParams params = CSVParams.DEFAULT;
      private Charset encoding = java.nio.charset.StandardCharsets.UTF_8;
      private Integer maxRecords = Integer.MAX_VALUE;
      private boolean firstLineIsHeader = true;
      private boolean complete;

      public CsvFile.Factory withCsvParams(CSVParams var1) {
         this.params = Preconditions.checkNotNull(var1);
         return this;
      }

      public CsvFile.Factory withoutHeader() {
         this.firstLineIsHeader = false;
         return this;
      }

      public CsvFile.Factory withEncoding(Charset var1) {
         this.encoding = Preconditions.checkNotNull(var1);
         return this;
      }

      public CsvFile.Factory limitRecordCountTo(int var1) {
         this.maxRecords = ArgCheck.checkPositive(var1);
         return this;
      }

      public CsvFile build(File var1) throws IOException {
         this.complete = false;
         FileContents var2 = new FileContents(var1, this.encoding);
         Record var3 = null;
         ArrayList var4 = Lists.newArrayList();
         long var5 = 0L;

         try {
            CSVParser var7 = new CSVParser(var2, this.params);
            long var8 = 0L;

            while ((var5 = var7.readRecord(var8, true)) >= 0L) {
               List var10 = var7.getValuesForLastRecord();
               if (var3 == null && this.firstLineIsHeader) {
                  var3 = stripQuotesFromHeader(var10);
               } else {
                  var4.add(new Record(var10));
                  if (var4.size() == this.maxRecords) {
                     break;
                  }
               }

               var8 = var5;
            }

            this.complete = var5 == -1L || var5 == var2.size();
            if (var3 == null) {
               var3 = Record.of();
            }

            return new CsvFile(var3, var4);
         } finally {
            KongaIoUtils.close(var2);
         }
      }

      private static Record stripQuotesFromHeader(List<String> var0) {
         ArrayList var1 = Lists.newArrayList();

         for (String var3 : var0) {
            var1.add(KongaStringUtils.removeStartAndEndIfBothPresent(var3.trim(), "\""));
         }

         return new Record(var1);
      }

      public boolean isFileComplete() {
         return this.complete;
      }
   }
}
