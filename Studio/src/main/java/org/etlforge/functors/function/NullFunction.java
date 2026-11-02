package org.etlforge.functors.function;

import com.google.common.base.Function;
import java.io.Serializable;

public final class NullFunction<I, O> implements Function<I, O>, Serializable {
   public static <U, V> Function<U, V> create() {
      return new NullFunction();
   }

   @Override
   public O apply(I var1) {
      return null;
   }
}
