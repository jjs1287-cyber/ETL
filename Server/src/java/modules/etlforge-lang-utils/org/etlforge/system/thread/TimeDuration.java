package org.etlforge.system.thread;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import org.etlforge.system.ArgCheck;

public final class TimeDuration implements Serializable {
   private final long value;
   private final TimeUnit unit;

   public static TimeDuration milliseconds(long var0) {
      return new TimeDuration(var0, TimeUnit.MILLISECONDS);
   }

   public static TimeDuration seconds(long var0) {
      return new TimeDuration(var0, TimeUnit.SECONDS);
   }

   public TimeDuration(long var1, TimeUnit var3) {
      this.value = ArgCheck.checkPositive(var1);
      this.unit = Preconditions.checkNotNull(var3);
   }

   public long getValue() {
      return this.value;
   }

   public TimeUnit getUnit() {
      return this.unit;
   }

   public Timeout toTimeout() {
      return Timeout.fromDuration(this);
   }

   public void sleep() throws InterruptedException {
      this.unit.sleep(this.value);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.value, this.unit);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof TimeDuration)) {
         return false;
      }

      TimeDuration var2 = (TimeDuration)var1;
      return this.value == var2.value && this.unit == var2.unit;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("value", this.value).add("unit", this.unit).toString();
   }
}
