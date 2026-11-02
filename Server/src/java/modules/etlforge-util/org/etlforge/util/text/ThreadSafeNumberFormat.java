package org.etlforge.util.text;

import java.text.NumberFormat;

public final class ThreadSafeNumberFormat {
   private final NumberFormat m_prototype;
   private final ThreadLocal<NumberFormat> m_formats;

   public ThreadSafeNumberFormat(NumberFormat var1) {
      this.m_prototype = (NumberFormat)var1.clone();
      this.m_formats = new ThreadLocal<NumberFormat>() {
         protected NumberFormat initialValue() {
            return (NumberFormat)ThreadSafeNumberFormat.this.m_prototype.clone();
         }
      };
   }

   public NumberFormat get() {
      return this.m_formats.get();
   }
}
