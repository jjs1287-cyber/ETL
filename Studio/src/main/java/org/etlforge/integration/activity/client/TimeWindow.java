package org.etlforge.integration.activity.client;

import java.util.Date;

public final class TimeWindow {
   private final Date start;
   private final Date end;

   public TimeWindow(Date var1, Date var2) {
      this.start = var1;
      this.end = var2;
   }

   public boolean isOverlapping(Date var1, Date var2) {
      if (this.start == null && this.end == null) {
         return true;
      } else {
         return this.start == null || var1 != null && !this.start.after(var1) ? this.end == null || var2 != null && !this.end.before(var2) : false;
      }
   }

   public boolean isInside(Date var1) {
      return this.isOverlapping(var1, var1);
   }
}
