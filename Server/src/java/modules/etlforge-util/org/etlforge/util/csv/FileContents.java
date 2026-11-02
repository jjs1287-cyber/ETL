package org.etlforge.util.csv;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import org.etlforge.system.ArgCheck;

public final class FileContents implements Closeable {
   private static final int nbuf = 2;
   private int buffer_length = 10000;
   private byte[] current_buf;
   private long current_offset;
   private long current_end_pos;
   private int current_length;
   private final long[] start_pos;
   private final int[] length;
   private int current = -1;
   private final byte[][] buf;
   private RandomAccessFile raf;
   private long size0 = 0L;
   private final File localFile;
   private Charset encoding;

   public FileContents(String var1) {
      this(var1, java.nio.charset.StandardCharsets.UTF_8);
   }

   public FileContents(String var1, Charset var2) {
      this(new File(var1), var2);
   }

   public FileContents(File var1) {
      this(var1, java.nio.charset.StandardCharsets.UTF_8);
   }

   public FileContents(File var1, Charset var2) {
      this.localFile = var1;
      this.setEncoding(var2);
      this.start_pos = new long[2];
      this.length = new int[2];
      this.buf = new byte[2][this.buffer_length];
      this.open();
      this.size();
   }

   public void setBufferLength(int var1) {
      this.buffer_length = ArgCheck.checkPositive(var1);
   }

   public void setEncoding(Charset var1) {
      this.encoding = Preconditions.checkNotNull(var1);
   }

   public long size() {
      if (this.size0 == 0L) {
         try {
            this.size0 = this.raf.length();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }

      return this.size0;
   }

   public byte at(long var1) throws IOException {
      if (var1 < this.size0 && var1 >= 0L) {
         if (!this.findBuffer(var1)) {
            this.load(var1);
         }

         return this.current_buf[(int)(var1 - this.current_offset)];
      } else {
         return 0;
      }
   }

   public String getStr(long var1, int var3) {
      try {
         if (!this.findBuffer(var1)) {
            this.load(var1);
         }

         long var4 = var1 + var3;
         if (var4 > this.size()) {
            var4 = this.size();
            var3 = (int)(var4 - var1);
         }

         byte[] var6 = new byte[var3];
         if (var4 <= this.current_end_pos) {
            return new String(this.current_buf, (int)(var1 - this.current_offset), var3, this.encoding);
         }

         int var7 = 0;

         while (var4 >= this.current_end_pos) {
            int var8 = (int)(this.current_end_pos - var1);
            int var9 = 0;
            int var10 = (int)(var1 - this.current_offset);

            while (var9 < var8) {
               var6[var9 + var7] = this.current_buf[var9 + var10];
               var9++;
            }

            var7 += var8;
            var1 = this.current_end_pos;
            if (!this.findBuffer(var1)) {
               boolean var13 = this.load(var1);
               if (var13) {
                  break;
               }
            }
         }

         if (var4 > var1) {
            int var12 = (int)(var4 - var1);
            int var14 = 0;
            int var15 = (int)(var1 - this.current_offset);

            while (var14 < var12) {
               var6[var14 + var7] = this.current_buf[var14 + var15];
               var14++;
            }
         }

         return new String(var6, this.encoding);
      } catch (Exception var11) {
         var11.printStackTrace();
         throw new RuntimeException("FileContents(" + this.localFile + ") fail to getStr.\n" + var11);
      }
   }

   public int findNextTokenLength(long var1, long var3, char var5, ParseQuoteFlag var6) throws IOException {
      long var7 = var1;
      boolean var9 = false;
      char var10 = '\u0000';
      char var11 = 0;

      while (var7 < var3) {
         if (!this.findBuffer(var7)) {
            var9 = this.load(var7);
         }

         if (var6 == ParseQuoteFlag.TRUE) {
            for (int var12 = (int)(var7 - this.current_offset); var12 < this.current_length; var7++) {
               var10 = (char)this.current_buf[var12];
               if (var11 == 0) {
                  if (var10 == '"' || var10 == '\'') {
                     var11 = var10;
                  }
               } else if (var11 == var10) {
                  var11 = 0;
               }

               if (var5 == var10 && var11 == 0) {
                  break;
               }

               var12++;
            }
         } else {
            for (int var15 = (int)(var7 - this.current_offset); var15 < this.current_length; var7++) {
               var10 = (char)this.current_buf[var15];
               if (var5 == var10) {
                  break;
               }

               var15++;
            }
         }

         if (var9 || var7 < this.current_end_pos) {
            break;
         }
      }

      if (var7 > var3) {
         var7 = var3;
      }

      return (int)(var7 - var1);
   }

   public int findNextTokenLength(long var1, long var3, String var5) {
      long var6 = var1;
      boolean var8 = false;

      while (var6 < var3) {
         if (!this.findBuffer(var6)) {
            var8 = this.load(var6);
         }

         for (int var9 = (int)(var6 - this.current_offset); var9 < this.current_length; var6++) {
            char var10 = (char)this.current_buf[var9];
            if (var5.indexOf(var10) >= 0) {
               break;
            }

            var9++;
         }

         if (var8 || var6 < this.current_end_pos) {
            break;
         }
      }

      if (var6 > var3) {
         var6 = var3;
      }

      return (int)(var6 - var1);
   }

   public long indexOf(String var1, long var2) {
      try {
         int var4 = var1.length();
         char[] var5 = var1.toCharArray();
         long var6 = var2;

         for (long var8 = this.size(); var6 < var8; var6++) {
            boolean var10 = true;

            for (int var11 = 0; var11 < var4; var11++) {
               if (this.at(var11 + var6) != var5[var11]) {
                  var10 = false;
                  break;
               }
            }

            if (var10) {
               return var6;
            }
         }

         return var2;
      } catch (IOException var12) {
         return var2;
      }
   }

   private void open() {
      if (this.raf == null) {
         try {
            this.raf = new RandomAccessFile(this.localFile, "r");
         } catch (Exception var2) {
            throw new RuntimeException("FileContents cannot open RandomAccessFile: " + this.localFile + "\n" + var2);
         }
      }
   }

   @Override
   public void close() throws IOException {
      if (this.raf != null) {
         this.raf.close();
         this.raf = null;
      }
   }

   private boolean load(long var1) {
      try {
         this.open();
         this.current_offset = var1 / this.buffer_length * this.buffer_length;
         this.raf.seek(this.current_offset);
         this.current_length = this.raf.read(this.buf[this.current]);
         this.current_end_pos = this.current_offset + this.current_length;
         this.start_pos[this.current] = this.current_offset;
         this.length[this.current] = this.current_length;
         this.current_buf = this.buf[this.current];
         return this.current_length < this.buffer_length;
      } catch (IOException var4) {
         throw new RuntimeException("FileContents failed to load buffer.\n" + var4);
      }
   }

   private boolean findBuffer(long var1) {
      if (var1 >= this.current_offset && var1 < this.current_end_pos) {
         return true;
      }

      if (this.current == -1) {
         this.current = 0;
         return false;
      }

      for (int var3 = 1; var3 < 2; var3++) {
         this.current = (this.current + 1) % 2;
         if (var1 >= this.start_pos[this.current] && var1 < this.start_pos[this.current] + this.length[this.current]) {
            this.current_offset = this.start_pos[this.current];
            this.current_length = this.length[this.current];
            this.current_end_pos = this.current_offset + this.current_length;
            this.current_buf = this.buf[this.current];
            return true;
         }
      }

      return false;
   }

   public boolean checkDelimiter(long var1, String var3) throws IOException {
      int var4 = var3.length();
      if (var4 == 1) {
         return true;
      }

      for (int var5 = 1; var5 < var4; var5++) {
         byte var6 = this.at(var1 + var5);
         if (var6 != (byte)var3.charAt(var5)) {
            return false;
         }
      }

      return true;
   }

   public FileContents.Location getLocation(long var1) throws IOException {
      int var3 = 1;
      long var4 = 0L;

      for (long var6 = 0L; var6 < var1; var6++) {
         if (this.at(var6) == 10) {
            var3++;
            var4 = var6;
         }
      }

      return new FileContents.Location(var3, (int)(var1 - var4) + 1);
   }

   public String getFileName() {
      return this.localFile.getName();
   }

   public static class Location {
      int lineNumber;
      int columnNumber;

      Location(int var1, int var2) {
         this.lineNumber = var1;
         this.columnNumber = var2;
      }

      @Override
      public String toString() {
         return "line " + this.lineNumber + ", column " + this.columnNumber;
      }
   }
}
