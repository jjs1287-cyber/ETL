package org.etlforge.ui.util;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.SystemUtils;

public final class ButtonOrder {
   public static <T> ImmutableList<T> okAndCancelOrder(T var0, T var1) {
      return SystemUtils.IS_OS_MAC ? ImmutableList.of((T)var1, (T)var0) : ImmutableList.of((T)var0, (T)var1);
   }

   public static <T> ImmutableList<T> okCancelAndApplyOrder(T var0, T var1, T var2) {
      return SystemUtils.IS_OS_MAC ? ImmutableList.of((T)var2, (T)var1, (T)var0) : ImmutableList.of((T)var0, (T)var1, (T)var2);
   }

   public static <T> ImmutableList<T> defaultActionCancelAndNoAction(T var0, T var1, T var2) {
      return SystemUtils.IS_OS_MAC ? ImmutableList.of((T)var2, (T)var1, (T)var0) : ImmutableList.of((T)var0, (T)var2, (T)var1);
   }
}
