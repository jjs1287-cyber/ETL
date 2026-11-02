package com.etlforge.integration.client.sfdc.data;

import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.system.ArgCheck;

public final class RetryPolicy {
   public static final RetryPolicy NONE = new RetryPolicy(-1, -1, false);
   public static final RetryPolicy NO_RETRIES = new RetryPolicy(0, 0);
   public static final RetryPolicy DATA_LOADER = new RetryPolicy(6, 10);
   public final int numberOfRetries;
   public final int retryIntervalInSeconds;

   public static RetryPolicy getDefaultPolicy() {
      return ClientInfo.isDataLoader() ? DATA_LOADER : NONE;
   }

   private RetryPolicy(int var1, int var2, boolean var3) {
      this.numberOfRetries = var1;
      this.retryIntervalInSeconds = var2;
   }

   public RetryPolicy(int var1, int var2) {
      this.numberOfRetries = ArgCheck.checkNotNegative(var1, "numberOfRetries");
      this.retryIntervalInSeconds = ArgCheck.checkNotNegative(var2, "retryIntervalInSeconds");
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.numberOfRetries;
      return 31 * var1 + this.retryIntervalInSeconds;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof RetryPolicy)) {
         return false;
      }

      RetryPolicy var2 = (RetryPolicy)var1;
      return this.numberOfRetries == var2.numberOfRetries && this.retryIntervalInSeconds == var2.retryIntervalInSeconds;
   }

   @Override
   public String toString() {
      return this.numberOfRetries + " times, every " + this.retryIntervalInSeconds + " secs";
   }
}
