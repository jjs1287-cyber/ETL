package org.etlforge.integration.salesforce.bulkload.backend;

import java.util.Date;

public final class TimeWindowFilter {
   public static final TimeWindowFilter NONE = new TimeWindowFilter(null, null);
   private final Long startCutoff;
   private final Long endCutoff;

   public TimeWindowFilter(Date var1, Date var2) {
      this.startCutoff = var1 == null ? null : var1.getTime();
      this.endCutoff = var2 == null ? null : var2.getTime();
   }

   public Date getStartCutoff() {
      return this.startCutoff == null ? null : new Date(this.startCutoff);
   }

   public Date getEndCutoff() {
      return this.endCutoff == null ? null : new Date(this.endCutoff);
   }
}
