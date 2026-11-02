package org.etlforge.util;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public final class SysOutHandler extends Handler {
   @Override
   public void close() throws SecurityException {
   }

   @Override
   public void flush() {
   }

   @Override
   public synchronized void publish(LogRecord var1) {
      System.out.println(var1.getMessage());
   }
}
