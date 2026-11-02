package org.etlforge.integration.salesforce.bulkload;

import com.google.common.base.Preconditions;
import java.util.TimeZone;

public final class TimeZoneId {
   public static final TimeZoneId SERVER_LOCAL = new TimeZoneId("");
   public static final TimeZoneId UTC = new TimeZoneId("UTC");
   private final String id;

   public static TimeZoneId forTimeZone(TimeZone var0) {
      return new TimeZoneId(var0.getID());
   }

   public static TimeZoneId valueOf(String var0) {
      if (var0.equals(UTC.id)) {
         return UTC;
      } else {
         return var0.equals(SERVER_LOCAL.id) ? SERVER_LOCAL : new TimeZoneId(var0);
      }
   }

   private TimeZoneId(String var1) {
      Preconditions.checkNotNull(var1, "id");
      this.id = var1;
   }

   public String getId() {
      return this.id;
   }

   @Override
   public int hashCode() {
      return this.id.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof TimeZoneId && this.id.equals(((TimeZoneId)var1).id);
   }

   @Override
   public String toString() {
      return this.id;
   }

   public TimeZone getTimeZone() {
      return this.id.equals(SERVER_LOCAL.id) ? TimeZone.getDefault() : TimeZone.getTimeZone(this.id);
   }
}
