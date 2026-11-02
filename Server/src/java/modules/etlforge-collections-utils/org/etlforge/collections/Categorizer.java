package org.etlforge.collections;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;
import java.util.Arrays;

public final class Categorizer<K, V> {
   private final Function<? super V, K> function;

   public static <X, Y> Categorizer<X, Y> forFunction(Function<? super Y, X> var0) {
      return new Categorizer(var0);
   }

   public Categorizer(Function<? super V, K> var1) {
      this.function = var1;
   }

   public ImmutableListMultimap<K, V> categorize(V[] var1) {
      return this.categorize(Arrays.asList((V[])var1));
   }

   public ImmutableListMultimap<K, V> categorize(Iterable<? extends V> var1) {
      return Multimaps.index(ImmutableList.copyOf(var1), this.function);
   }
}
