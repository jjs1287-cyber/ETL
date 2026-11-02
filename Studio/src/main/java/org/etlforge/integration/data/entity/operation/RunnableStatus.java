package org.etlforge.integration.data.entity.operation;

import com.google.common.base.Preconditions;

public final class RunnableStatus {
   public static final RunnableStatus RUNNABLE = new RunnableStatus(true, null);
   private final boolean runnable;
   private final String reasonNotRunnable;

   private RunnableStatus(boolean var1, String var2) {
      this.runnable = var1;
      this.reasonNotRunnable = var2;
   }

   public static RunnableStatus notRunnable(String var0) {
      Preconditions.checkNotNull(var0, "reason");
      return new RunnableStatus(false, var0);
   }

   public boolean isRunnable() {
      return this.runnable;
   }

   public String getReasonNotRunnable() {
      return this.reasonNotRunnable;
   }
}
