package org.etlforge.io;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.Map;

public class FileChunkReader {
   private final File file;
   private final int chunkSize;
   private final String charsetName;
   private boolean open;
   private int currentChunk;
   private RandomAccessFile reader;
   private Map<Integer, Long> pageBreaks;
   private long fileSize;

   public FileChunkReader(File var1, int var2) {
      this(var1, var2, null);
   }

   public FileChunkReader(File var1, int var2, String var3) {
      Preconditions.checkNotNull(var1, "file");
      Preconditions.checkArgument(var2 > 0, "chunkSize must be > 0 (was " + var2 + ")");
      this.file = var1;
      this.chunkSize = var2;
      this.charsetName = var3 != null ? var3 : Charset.defaultCharset().name();
      this.currentChunk = -1;
   }

   public void close() {
      if (this.open) {
         KongaIoUtils.close(this.reader);
         this.reader = null;
      }

      this.open = false;
   }

   public String next() throws IOException {
      this.checkOpen();
      return !this.hasNext() ? null : this.readForward();
   }

   private void checkOpen() throws IOException {
      if (!this.open) {
         throw new IOException("This m_reader has not been opened.");
      }
   }

   public boolean hasNext() {
      return this.currentChunk < this.getNumberOfChunks() - 1;
   }

   private String readForward() throws IOException {
      return this.moveToChunk(this.currentChunk + 1);
   }

   private String moveToChunk(int var1) throws IOException {
      long var2 = this.pageBreaks.get(var1);
      long var4;
      if (var1 == this.getNumberOfChunks()) {
         var4 = this.fileSize;
      } else {
         long var6 = this.pageBreaks.get(var1 + 1);
         var4 = var6 - 1L;
      }

      String var8 = this.readFromFile(var2, var4);
      this.currentChunk = var1;
      return var8;
   }

   private String readFromFile(long var1, long var3) throws IOException {
      this.reader.seek(var1);
      byte[] var5 = new byte[(int)(var3 - var1) + 1];
      int var6 = this.reader.read(var5, 0, var5.length);
      if (var6 > 0 && (var5[var6 - 1] == 10 || var5[var6 - 1] == 13)) {
         var6--;
      }

      return new String(var5, 0, var6, this.charsetName);
   }

   public String previous() throws IOException {
      this.checkOpen();
      return !this.hasPrevious() ? null : this.readBackward();
   }

   public boolean hasPrevious() {
      return this.currentChunk > 0;
   }

   private String readBackward() throws IOException {
      return this.moveToChunk(this.currentChunk - 1);
   }

   public void open() throws IOException {
      if (!this.open) {
         this.pageBreaks = Maps.newHashMap();
         this.reader = new RandomAccessFile(this.file, "r");

         try {
            this.fileSize = this.reader.length();
            if (this.fileSize > 0L) {
               this.locatePageBreaks();
            }

            this.open = true;
         } finally {
            if (!this.open) {
               KongaIoUtils.close(this.reader);
            }
         }
      }
   }

   private void locatePageBreaks() throws IOException {
      int var1 = 1;
      this.pageBreaks.put(var1, 0L);
      long var2 = this.chunkSize;

      while (var2 < this.fileSize) {
         long var4 = var2;
         var2 = this.lookForLineBreak(var4);
         this.pageBreaks.put(++var1, var2);
         var2 += this.chunkSize;
      }
   }

   private long lookForLineBreak(long var1) throws IOException {
      long var3 = var1;

      while (var3 < this.fileSize) {
         long var5 = Math.min(var3 + 500L, this.fileSize);
         int var7 = (int)(var5 - var3 + 1L);
         byte[] var8 = new byte[var7];
         this.reader.seek(var3);
         if (this.reader.read(var8) == -1) {
            break;
         }

         for (int var9 = 0; var9 < var7; var9++) {
            if (var8[var9] == 10) {
               return var3 + var9 + 1L;
            }
         }

         var3 += var7;
      }

      return this.fileSize;
   }

   public int getNumberOfChunks() {
      return this.pageBreaks.size();
   }

   public String gotoChunk(int var1) throws IOException {
      Preconditions.checkArgument(var1 <= this.getNumberOfChunks(), var1 + " number of chunks are not available");
      return this.moveToChunk(var1);
   }
}
