package org.etlforge.util.time;

import com.google.common.base.Function;
import java.util.TimeZone;

public final class TimeZones {
   public static final TimeZone DEFAULT = TimeZone.getDefault();
   public static final TimeZone UTC = TimeZone.getTimeZone("UTC");
   public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
   public static final Function<String, TimeZone> FROM_ID = TimeZones.FromId.INSTANCE;

   private TimeZones() {
   }

   private enum FromId implements Function<String, TimeZone> {
      INSTANCE;

      public TimeZone apply(String var1) {
         return TimeZone.getTimeZone(var1);
      }
   }
}
