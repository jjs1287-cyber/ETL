package org.etlforge.io;

import java.io.Writer;

public final class NullWriter extends Writer {
   @Override
   public void write(char[] var1, int var2, int var3) {
   }

   @Override
   public void flush() {
   }

   @Override
   public void close() {
   }
}
