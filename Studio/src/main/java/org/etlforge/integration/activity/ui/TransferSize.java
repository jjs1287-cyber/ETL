package org.etlforge.integration.activity.ui;

import com.google.common.primitives.Longs;
import org.etlforge.system.ArgCheck;

public final class TransferSize implements Comparable<TransferSize> {
   private final long bytes;

   public TransferSize(long var1) {
      this.bytes = ArgCheck.checkNotNegative(var1);
   }

   public long getBytes() {
      return this.bytes;
   }

   public int compareTo(TransferSize var1) {
      return Longs.compare(this.bytes, var1.bytes);
   }

   @Override
   public String toString() {
      return this.bytes + " b";
   }

   @Override
   public int hashCode() {
      return Longs.hashCode(this.bytes);
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof TransferSize && this.bytes == ((TransferSize)var1).bytes;
   }
}
