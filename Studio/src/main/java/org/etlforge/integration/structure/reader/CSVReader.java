package org.etlforge.integration.structure.reader;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.util.csv.FileContents;

public class CSVReader {
   private final File file;
   private final Charset encoding;
   private final char qualifier;
   private final char delimiter;
   private final String delimiter_str;
   private FileContents fc = null;
   private CSVParser parser = null;
   private boolean eof;
   private long currBeginPosi;
   private long currEndPosi;
   private CSVReader.IndexNode currRecordIndex;
   static final char ef = '\u00ef';
   static final char bb = '\u00bb';
   static final char bf = '\u00bf';

   public CSVReader(File var1, Charset var2, char var3, char var4) {
      this.file = var1;
      this.encoding = var2;
      this.qualifier = var3;
      this.delimiter = var4;
      this.delimiter_str = Character.toString(var4);
   }

   public CSVReader(String var1, Charset var2, char var3, char var4) {
      this(new File(var1), var2, var3, var4);
   }

   public CSVReader(File var1, char var2, char var3) {
      this(var1, Charset.defaultCharset(), var2, var3);
   }

   public void open() throws IOException {
      this.fc = new FileContents(this.file, this.encoding);
      this.parser = new CSVParser(this.fc, this.qualifier, this.delimiter, this.delimiter_str, true);
      this.currBeginPosi = this.currEndPosi = 0L;
      this.currRecordIndex = null;
      this.skipInitialLineBreak();
   }

   public void close() {
      try {
         if (this.fc != null) {
            this.currBeginPosi = this.fc.size();
            this.fc.close();
         }
      } catch (IOException var5) {
         var5.printStackTrace();
      } finally {
         this.fc = null;
         this.parser = null;
      }
   }

   public boolean eof() {
      return this.eof;
   }

   private List<CSVReader.IndexNode> getRecordIndexList(long var1, int var3) throws IOException {
      this.currBeginPosi = var1;
      this.skipInitialLineBreak();
      ArrayList var4 = Lists.newArrayList();

      while (this.nextRecord()) {
         var4.add(this.currRecordIndex);
         if (var4.size() > var3) {
            break;
         }
      }

      return var4;
   }

   public List<CSVReader.IndexNode> getRecordIndexList(int var1) throws IOException {
      return this.getRecordIndexList(0L, var1);
   }

   public CSVReader.IndexNode getRecordIndex() {
      return this.currRecordIndex;
   }

   private void skipBOM() throws IOException {
      if (this.currBeginPosi == 0L) {
         if ((this.fc.at(0L) & 255) == 239 && (this.fc.at(1L) & 255) == 187 && (this.fc.at(2L) & 255) == 191) {
            this.currBeginPosi = 3L;
         }
      }
   }

   private void skipInitialLineBreak() throws IOException {
      this.skipBOM();
      this.currBeginPosi = this.parser.skipLineBreak(this.currBeginPosi);
   }

   public boolean nextRecord() throws IOException {
      this.eof = this.parser.isEndOfFile(this.currBeginPosi);
      if (this.eof()) {
         return false;
      }

      this.currEndPosi = this.parser.readRecord(this.currBeginPosi);
      this.currRecordIndex = new CSVReader.IndexNode(this.currBeginPosi, this.currEndPosi);
      this.currBeginPosi = this.parser.skipLineBreak(this.currEndPosi);
      return true;
   }

   public List<CSVReader.IndexNode> getRecordFieldsIndex(CSVReader.IndexNode var1) throws IOException {
      ArrayList var2 = Lists.newArrayList();
      if (var1 == null) {
         return var2;
      }

      long var3 = var1.beginIndex;
      long var5 = var1.endIndex;
      boolean var7 = false;
      long var10 = var3;

      while (var10 < var5) {
         long var8 = this.parser.readValue(var10);
         var7 = this.fc.at(var8 - 1L) == this.delimiter;
         if (var7) {
            var2.add(new CSVReader.IndexNode(var10, var8 - 1L));
         } else {
            var2.add(new CSVReader.IndexNode(var10, var8));
         }

         var10 = var8;
      }

      if (var7) {
         var2.add(new CSVReader.IndexNode(var5, var5));
      }

      return var2;
   }

   public List<String> getFieldsValue(List<CSVReader.IndexNode> var1) throws IOException {
      ArrayList var2 = Lists.newArrayList();

      for (CSVReader.IndexNode var4 : var1) {
         int var5 = (int)(var4.endIndex - var4.beginIndex);
         String var6 = this.parser.getStr(var4.beginIndex, var5);
         var2.add(var6);
      }

      return var2;
   }

   public List<String> getRecordFields() throws IOException {
      return this.getRecordFields(this.getRecordIndex());
   }

   public List<String> getRecordFields(CSVReader.IndexNode var1) throws IOException {
      List var2 = this.getRecordFieldsIndex(var1);
      return this.getFieldsValue(var2);
   }

   static class IndexNode {
      long beginIndex;
      long endIndex;

      IndexNode(long var1, long var3) {
         this.beginIndex = var1;
         this.endIndex = var3;
      }
   }
}
