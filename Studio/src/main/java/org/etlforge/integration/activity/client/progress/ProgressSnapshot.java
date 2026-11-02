package org.etlforge.integration.activity.client.progress;

import com.google.common.base.Preconditions;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.etlforge.system.ArgCheck;

public final class ProgressSnapshot {
   private final String section;
   private final String message;
   private final int percentageDone;
   private final Date estimatedEndTime;

   public ProgressSnapshot(String var1, String var2, int var3, Date var4) {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "Must provide a section");
      ArgCheck.checkInRange(var3, -1, 100, "percentageDone");
      var3 = Math.min(var3, 100);
      this.section = var1;
      this.message = var2 == null ? "" : var2;
      this.percentageDone = var3;
      this.estimatedEndTime = copy(var4);
   }

   private static Date copy(Date var0) {
      return var0 == null ? null : new Date(var0.getTime());
   }

   public String getSection() {
      return this.section;
   }

   public String getMessage() {
      return this.message;
   }

   public int getPercentageDone() {
      return this.percentageDone;
   }

   public Date getEstimatedEndTime() {
      return copy(this.estimatedEndTime);
   }
}
