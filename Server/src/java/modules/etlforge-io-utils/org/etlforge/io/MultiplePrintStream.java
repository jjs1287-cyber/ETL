package org.etlforge.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class MultiplePrintStream extends PrintStream {
   private final PrintStream mainStream;
   private final PrintStream[] additionalStreams;

   public MultiplePrintStream(File var1, String var2, PrintStream[] var3) throws FileNotFoundException, UnsupportedEncodingException {
      super(var1, var2);
      this.mainStream = new PrintStream(var1, var2);
      this.additionalStreams = var3;
   }

   public MultiplePrintStream(File var1, PrintStream[] var2) throws FileNotFoundException {
      super(var1);
      this.mainStream = new PrintStream(var1);
      this.additionalStreams = var2;
   }

   public MultiplePrintStream(OutputStream var1, boolean var2, String var3, PrintStream[] var4) throws UnsupportedEncodingException {
      super(var1, var2, var3);
      this.mainStream = new PrintStream(var1, var2, var3);
      this.additionalStreams = var4;
   }

   public MultiplePrintStream(OutputStream var1, boolean var2, PrintStream[] var3) {
      super(var1, var2);
      this.mainStream = new PrintStream(var1, var2);
      this.additionalStreams = var3;
   }

   public MultiplePrintStream(OutputStream var1, PrintStream[] var2) {
      super(var1);
      this.mainStream = new PrintStream(var1);
      this.additionalStreams = var2;
   }

   public MultiplePrintStream(String var1, String var2, PrintStream[] var3) throws FileNotFoundException, UnsupportedEncodingException {
      super(var1, var2);
      this.mainStream = new PrintStream(var1, var2);
      this.additionalStreams = var3;
   }

   public MultiplePrintStream(String var1, PrintStream[] var2) throws FileNotFoundException {
      super(var1);
      this.mainStream = new PrintStream(var1);
      this.additionalStreams = var2;
   }

   @Override
   public PrintStream append(char var1) {
      this.mainStream.append(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.append(var1);
      }

      return this;
   }

   @Override
   public PrintStream append(CharSequence var1, int var2, int var3) {
      this.mainStream.append(var1, var2, var3);

      for (PrintStream var7 : this.additionalStreams) {
         var7.append(var1, var2, var3);
      }

      return this;
   }

   @Override
   public PrintStream append(CharSequence var1) {
      this.mainStream.append(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.append(var1);
      }

      return this;
   }

   @Override
   public boolean checkError() {
      boolean var1 = this.mainStream.checkError();

      for (PrintStream var5 : this.additionalStreams) {
         var5.checkError();
      }

      return var1;
   }

   @Override
   public void close() {
      this.mainStream.close();

      for (PrintStream var4 : this.additionalStreams) {
         var4.close();
      }
   }

   @Override
   public void flush() {
      this.mainStream.flush();

      for (PrintStream var4 : this.additionalStreams) {
         var4.flush();
      }
   }

   @Override
   public PrintStream format(Locale var1, String var2, Object... var3) {
      this.mainStream.format(var1, var2, var3);

      for (PrintStream var7 : this.additionalStreams) {
         var7.format(var1, var2, var3);
      }

      return this;
   }

   @Override
   public PrintStream format(String var1, Object... var2) {
      this.mainStream.format(var1, var2);

      for (PrintStream var6 : this.additionalStreams) {
         var6.format(var1, var2);
      }

      return this;
   }

   @Override
   public void print(boolean var1) {
      this.mainStream.print(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.print(var1);
      }
   }

   @Override
   public void print(char var1) {
      this.mainStream.print(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.print(var1);
      }
   }

   @Override
   public void print(char[] var1) {
      this.mainStream.print(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.print(var1);
      }
   }

   @Override
   public void print(double var1) {
      this.mainStream.print(var1);

      for (PrintStream var6 : this.additionalStreams) {
         var6.print(var1);
      }
   }

   @Override
   public void print(float var1) {
      this.mainStream.print(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.print(var1);
      }
   }

   @Override
   public void print(int var1) {
      this.mainStream.print(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.print(var1);
      }
   }

   @Override
   public void print(long var1) {
      this.mainStream.print(var1);

      for (PrintStream var6 : this.additionalStreams) {
         var6.print(var1);
      }
   }

   @Override
   public void print(Object var1) {
      this.mainStream.print(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.print(var1);
      }
   }

   @Override
   public void print(String var1) {
      this.mainStream.print(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.print(var1);
      }
   }

   @Override
   public PrintStream printf(Locale var1, String var2, Object... var3) {
      this.mainStream.printf(var1, var2, var3);

      for (PrintStream var7 : this.additionalStreams) {
         var7.printf(var1, var2, var3);
      }

      return this;
   }

   @Override
   public PrintStream printf(String var1, Object... var2) {
      this.mainStream.printf(var1, var2);

      for (PrintStream var6 : this.additionalStreams) {
         var6.printf(var1, var2);
      }

      return this;
   }

   @Override
   public void println() {
      this.mainStream.println();

      for (PrintStream var4 : this.additionalStreams) {
         var4.println();
      }
   }

   @Override
   public void println(boolean var1) {
      this.mainStream.println(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.println(var1);
      }
   }

   @Override
   public void println(char var1) {
      this.mainStream.println(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.println(var1);
      }
   }

   @Override
   public void println(char[] var1) {
      this.mainStream.println(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.println(var1);
      }
   }

   @Override
   public void println(double var1) {
      this.mainStream.println(var1);

      for (PrintStream var6 : this.additionalStreams) {
         var6.println(var1);
      }
   }

   @Override
   public void println(float var1) {
      this.mainStream.println(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.println(var1);
      }
   }

   @Override
   public void println(int var1) {
      this.mainStream.println(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.println(var1);
      }
   }

   @Override
   public void println(long var1) {
      this.mainStream.println(var1);

      for (PrintStream var6 : this.additionalStreams) {
         var6.println(var1);
      }
   }

   @Override
   public void println(Object var1) {
      this.mainStream.println(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.println(var1);
      }
   }

   @Override
   public void println(String var1) {
      this.mainStream.println(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.println(var1);
      }
   }

   @Override
   public void write(byte[] var1, int var2, int var3) {
      this.mainStream.write(var1, var2, var3);

      for (PrintStream var7 : this.additionalStreams) {
         var7.write(var1, var2, var3);
      }
   }

   @Override
   public void write(int var1) {
      this.mainStream.write(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.write(var1);
      }
   }

   @Override
   public void write(byte[] var1) throws IOException {
      this.mainStream.write(var1);

      for (PrintStream var5 : this.additionalStreams) {
         var5.write(var1);
      }
   }
}
