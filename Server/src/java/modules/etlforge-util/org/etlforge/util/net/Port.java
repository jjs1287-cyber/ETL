package org.etlforge.util.net;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public final class Port {
   public static final Range<Integer> VALID_PORT_RANGE = Ranges.closed(1, 65536);
   public static final Port INVALID = new Port(-2);
   public static final Port DEFAULT = new Port(0);
   private final int port;

   private Port(int var1) {
      this.port = var1;
   }

   public static Port valueOf(int var0) {
      if (var0 < 0 || var0 > 65536) {
         return INVALID;
      } else {
         return var0 == 0 ? DEFAULT : new Port(var0);
      }
   }

   public static Port valueOf(String var0) {
      try {
         Integer var1 = Integer.parseInt(var0);
         return valueOf(var1);
      } catch (NumberFormatException var2) {
         return INVALID;
      }
   }

   public int getPort() {
      return this.port;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof Port && this.port == ((Port)var1).port;
   }

   @Override
   public int hashCode() {
      return this.port;
   }

   @Override
   public String toString() {
      return "[Port = " + this.port + "]";
   }
}
