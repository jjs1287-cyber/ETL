package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

public class NetSuiteQueryPageSize {
   public static final NetSuiteQueryPageSize DEFAULT = new NetSuiteQueryPageSize(0);
   private static final ImmutableList<NetSuiteQueryPageSize> SUPPORTED_SIZES = ImmutableList.of(
      DEFAULT,
      new NetSuiteQueryPageSize(5),
      new NetSuiteQueryPageSize(10),
      new NetSuiteQueryPageSize(20),
      new NetSuiteQueryPageSize(50),
      new NetSuiteQueryPageSize(100),
      new NetSuiteQueryPageSize(200),
      new NetSuiteQueryPageSize(500),
      new NetSuiteQueryPageSize(1000)
   );
   private final int pageSize;

   private NetSuiteQueryPageSize(int var1) {
      this.pageSize = var1;
   }

   public static ImmutableList<NetSuiteQueryPageSize> getSupportedSizes() {
      return SUPPORTED_SIZES;
   }

   public static NetSuiteQueryPageSize valueOf(String var0) {
      if (Strings.isNullOrEmpty(var0)) {
         return DEFAULT;
      }

      int var1 = Integer.parseInt(var0);
      return valueOf(var1);
   }

   public static NetSuiteQueryPageSize valueOf(int var0) {
      return var0 <= 0 ? DEFAULT : new NetSuiteQueryPageSize(var0);
   }

   public int getPageSize() {
      return this.pageSize;
   }

   @Override
   public String toString() {
      return this.pageSize == 0 ? "DEFAULT" : String.valueOf(this.pageSize);
   }

   @Override
   public int hashCode() {
      return this.pageSize;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this ? true : var1 instanceof NetSuiteQueryPageSize && this.pageSize == ((NetSuiteQueryPageSize)var1).pageSize;
   }
}
