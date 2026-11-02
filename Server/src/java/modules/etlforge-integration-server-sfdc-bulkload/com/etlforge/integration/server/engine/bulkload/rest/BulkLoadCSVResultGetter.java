package com.etlforge.integration.server.engine.bulkload.rest;

import com.etlforge.integration.server.engine.bulkload.BulkLoadQueryResultParameter;
import com.etlforge.integration.server.engine.bulkload.ETLForgeSystemProperty;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.etlforge.io.KongaIoUtils;

public class BulkLoadCSVResultGetter {
   private static final char QUALIFIER = '"';
   private static final char LF = '\n';
   private static final char CR = '\r';
   private static final String xmlString = "<xml? ";
   private final String CSVResultUrl;
   private final String SessionID;
   private final HttpClient client;
   private final String resultFilePath;
   private final Charset resultFileCharset;
   private final char[] buffer = new char[65536];
   private final boolean removeFirstLine;
   private Date d1;
   private Date d2;
   private int numberCharRead;
   private InputStreamReader reader;
   private OutputStreamWriter writer;
   private char currChar;
   private char PrevChar;
   private int currPosi;
   private int bufferoffset;
   private volatile long records;

   public BulkLoadCSVResultGetter(String var1, String var2, HttpClient var3, String var4, Charset var5, boolean var6) {
      this.CSVResultUrl = var1;
      this.SessionID = var2;
      this.client = var3;
      this.resultFilePath = var4;
      this.resultFileCharset = var5;
      this.removeFirstLine = var6;
      this.reader = null;
      this.writer = null;
      this.initReadNextChar();
      this.d1 = this.d2 = new Date();
      this.numberCharRead = 0;
   }

   public boolean getResult(BulkLoadQueryResultParameter var1) throws Exception {
      InputStream var2 = this.getResultStream();
      this.save(var2, var1);
      return true;
   }

   private InputStream getResultStream() throws ClientProtocolException, IOException {
      HttpGet var1 = new HttpGet(this.CSVResultUrl);
      var1.setHeader("Accept-Encoding", "gzip");
      var1.setHeader("X-SFDC-Session", this.SessionID);
      HttpResponse var2 = this.client.execute(var1);
      if (var2 != null) {
         HttpEntity var3 = var2.getEntity();
         Header var4 = var3.getContentEncoding();
         return var4 != null && "gzip".equalsIgnoreCase(var4.getValue()) ? new GZIPInputStream(var3.getContent()) : var2.getEntity().getContent();
      } else {
         return null;
      }
   }

   private boolean isXmlError() {
      if (this.numberCharRead == -1) {
         return true;
      }

      if (this.numberCharRead < "<xml? ".length()) {
         return true;
      }

      for (int var1 = 0; var1 < "<xml? ".length(); var1++) {
         if (this.buffer[var1] != "<xml? ".charAt(var1)) {
            return false;
         }
      }

      return true;
   }

   private boolean eof() {
      return this.numberCharRead == -1;
   }

   public long getRecords() {
      return this.records;
   }

   private boolean shouldWriteFile() {
      return this.bufferoffset >= 0;
   }

   private void setNotWriteFile() {
      this.bufferoffset = -1;
   }

   private boolean readNextChar() throws IOException {
      if (this.currPosi >= this.numberCharRead) {
         if (this.numberCharRead > 0 && this.shouldWriteFile()) {
            if (this.bufferoffset == 0) {
               this.writer.write(this.buffer, 0, this.numberCharRead);
            } else {
               int var1 = this.numberCharRead - this.bufferoffset;
               if (var1 > 0) {
                  this.writer.write(this.buffer, this.bufferoffset, var1);
               }

               this.bufferoffset = 0;
            }
         }

         this.numberCharRead = this.reader.read(this.buffer);
         if (this.eof()) {
            return false;
         }

         this.currPosi = 0;
      }

      this.PrevChar = this.currChar;
      this.currChar = this.buffer[this.currPosi++];
      return true;
   }

   private boolean isCurrCharLineBreak() {
      return '\n' == this.currChar || '\r' == this.currChar;
   }

   private boolean readLine() throws IOException {
      if (this.eof()) {
         return false;
      }

      while (this.readNextChar() && (!this.isCurrCharLineBreak() || '"' != this.PrevChar)) {
      }

      if (!this.eof()) {
         while (this.readNextChar()) {
            if (!this.isCurrCharLineBreak()) {
               this.currPosi--;
               break;
            }
         }
      }

      return true;
   }

   private void initReadNextChar() {
      this.currChar = this.PrevChar = 0;
      this.bufferoffset = 0;
      this.currPosi = 0;
      this.records = 0L;
   }

   private void save(InputStream var1, BulkLoadQueryResultParameter var2) throws Exception {
      Charset var3 = ETLForgeSystemProperty.getCharset("UTF-8");
      this.reader = new InputStreamReader(var1, var3);

      try {
         this.d1 = new Date();
         this.numberCharRead = this.reader.read(this.buffer);
         boolean var4 = !this.isXmlError();
         var4 = true;
         if (!var4) {
            throw new Exception(new String(this.buffer));
         }

         this.writer = new OutputStreamWriter(new FileOutputStream(this.resultFilePath), this.resultFileCharset);
         this.initReadNextChar();
         if (this.removeFirstLine) {
            this.setNotWriteFile();
         }

         this.readLine();
         if (this.removeFirstLine) {
            this.bufferoffset = this.currPosi;
         }

         if (!this.eof()) {
            while (this.readLine()) {
               this.records++;
               if (var2 != null) {
                  var2.setReceivedRecords(this.records);
               }
            }
         }

         this.d2 = new Date();
      } finally {
         KongaIoUtils.close(this.reader);
         KongaIoUtils.close(this.writer);
      }
   }

   public static void main(String[] var0) throws Exception {
      System.out.println("start...");
      String var1 = "Z:/dev/dataloader/bulkload/test/bulkquery.csv";
      String var2 = "Z:/dev/dataloader/bulkload/test/bulkqueryOut.csv";
      FileInputStream var3 = new FileInputStream(var1);
      BulkLoadCSVResultGetter var4 = new BulkLoadCSVResultGetter("", "", null, var2, Charset.defaultCharset(), true);
      var4.save(var3, null);
      System.out.println(var4.records);
      System.out.println(var4.d2.getTime() - var4.d1.getTime());
      System.out.println("end...");
   }
}
