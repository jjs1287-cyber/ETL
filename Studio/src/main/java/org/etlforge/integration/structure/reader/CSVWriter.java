package org.etlforge.integration.structure.reader;

import com.google.common.io.Files;
import com.google.common.io.OutputSupplier;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

public class CSVWriter implements Closeable {
   private final OutputSupplier<? extends Writer> outputSupplier;
   private final char qualifier;
   private final char delimiter;
   private BufferedWriter writer;

   public CSVWriter(OutputSupplier<? extends Writer> var1, char var2, char var3) {
      this.outputSupplier = var1;
      this.qualifier = var2;
      this.delimiter = var3;
   }

   public CSVWriter(String var1, Charset var2, char var3, char var4) {
      this(Files.newWriterSupplier(new File(var1), var2), var3, var4);
   }

   public void open() throws IOException {
      this.writer = new BufferedWriter(this.outputSupplier.getOutput());
   }

   @Override
   public void close() throws IOException {
      if (this.writer != null) {
         this.writer.close();
      }
   }

   public void writeRecord(List<String> var1) throws IOException {
      int var2 = 0;

      for (String var4 : var1) {
         if (var2++ > 0) {
            this.writer.write(this.delimiter);
         }

         this.writer.write(var4);
      }

      if (var1.size() > 0) {
         this.writer.newLine();
      }
   }

   public void writeRecordForTest(List<String> var1) throws IOException {
      int var2 = 0;

      for (String var4 : var1) {
         if (var2++ > 0) {
            this.writer.write(this.delimiter);
         }

         this.writer.write(91);
         Integer var5 = var2;
         this.writer.write(var5.toString());
         this.writer.write(93);
         this.writer.write(123);
         this.writer.write(var4);
         this.writer.write(125);
      }

      if (var1.size() > 0) {
         this.writer.newLine();
      }
   }
}
