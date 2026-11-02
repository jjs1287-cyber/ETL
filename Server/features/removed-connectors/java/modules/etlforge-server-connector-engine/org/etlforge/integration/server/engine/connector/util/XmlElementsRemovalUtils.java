package org.etlforge.integration.server.engine.connector.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class XmlElementsRemovalUtils {
   private final BufferedReader reader;
   private final BufferedWriter writer;
   private static int blockSize = 8000;
   private char[] buf1;
   private char[] buf2;
   private char[] buf;
   private int size;
   private boolean eof;
   private boolean startElementsNotProcessed;
   private final int skipElements;

   XmlElementsRemovalUtils(int var1, BufferedReader var2, BufferedWriter var3) {
      this.skipElements = var1;
      this.reader = var2;
      this.writer = var3;
      this.buf1 = new char[blockSize];
      this.buf2 = new char[blockSize];
      this.startElementsNotProcessed = true;
   }

   void bufferread(char[] var1) throws IOException {
      this.size = -1;
      if (!this.eof) {
         this.size = this.reader.read(var1);
         this.eof = this.size == -1;
         if (!this.eof) {
            while (this.size < var1.length) {
               int var2 = this.reader.read(var1, this.size, var1.length - this.size);
               this.eof = var2 == -1;
               if (this.eof) {
                  return;
               }

               if (var2 <= 0) {
                  throw new IOException("can not read!");
               }

               this.size += var2;
            }
         }
      }
   }

   void doWork() throws IOException {
      this.bufferread(this.buf1);

      while (this.process()) {
      }
   }

   private void removeElements() throws IOException {
      XmlElementsSkipUtils var1 = new XmlElementsSkipUtils(this.buf, this.size);
      if (this.startElementsNotProcessed) {
         var1.skipXmlHeaders();
         int var2 = 0;

         while (var2 < this.skipElements && var1.skipElementStart()) {
            var2++;
         }

         this.startElementsNotProcessed = false;
      }

      if (this.eof) {
         int var3 = 0;

         while (var3 < this.skipElements && var1.skipElementEnd()) {
            var3++;
         }
      }

      if (var1.getStartIndex() >= 0 && var1.getEndIndex() <= this.size) {
         int var4 = var1.getEndIndex() - var1.getStartIndex();
         if (var4 > 0) {
            this.writer.write(this.buf, var1.getStartIndex(), var4);
         }
      }
   }

   private boolean process() throws IOException {
      this.buf = this.buf1;
      if (!this.eof) {
         this.bufferread(this.buf2);
         if (this.eof) {
            if (this.size > 0) {
               StringBuilder var1 = new StringBuilder();
               var1.append(this.buf1);
               var1.append(this.buf2, 0, this.size);
               this.buf = var1.toString().toCharArray();
               this.size = this.buf.length;
            } else {
               this.buf = this.buf1;
               this.size = this.buf.length;
            }
         }
      }

      if (!this.startElementsNotProcessed && !this.eof) {
         this.writer.write(this.buf);
      } else {
         this.removeElements();
      }

      if (this.eof) {
         return false;
      }

      this.size = -1;
      this.buf = this.buf1;
      this.buf1 = this.buf2;
      this.buf2 = this.buf;
      return true;
   }
}
