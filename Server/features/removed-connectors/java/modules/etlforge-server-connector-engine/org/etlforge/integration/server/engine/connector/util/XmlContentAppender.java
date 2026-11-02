package org.etlforge.integration.server.engine.connector.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class XmlContentAppender {
   private final BufferedReader reader;
   private final BufferedWriter writer;
   private static int blockSize = 8000;
   private final char[] buf = new char[blockSize];
   private int size;
   private boolean eof;
   private boolean headerIsNotSkipped;
   private char previous;

   XmlContentAppender(BufferedReader var1, BufferedWriter var2) {
      this.reader = var1;
      this.writer = var2;
      this.headerIsNotSkipped = true;
      this.previous = 0;
   }

   public void doWork() throws IOException {
      this.headerIsNotSkipped = true;
      this.previous = 0;

      while (true) {
         this.size = this.reader.read(this.buf);
         this.eof = this.size == -1;
         if (this.eof) {
            return;
         }

         this.append();
      }
   }

   private void append() throws IOException {
      if (this.headerIsNotSkipped) {
         for (int var1 = 0; var1 < this.size; var1++) {
            char var2 = this.buf[var1];
            if (this.previous == '<' && var2 != '?') {
               this.headerIsNotSkipped = false;
               this.writer.write(this.previous);
               this.writer.write(this.buf, var1, this.size - var1);
               break;
            }

            this.previous = var2;
         }
      } else {
         this.writer.write(this.buf, 0, this.size);
      }
   }
}
