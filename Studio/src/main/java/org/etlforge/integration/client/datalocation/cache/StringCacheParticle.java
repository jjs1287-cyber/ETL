package org.etlforge.integration.client.datalocation.cache;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

abstract class StringCacheParticle<T> {
   private final LimitedSizeStringCache cache;

   public StringCacheParticle(String var1, File var2, int var3, boolean var4) {
      this.cache = new LimitedSizeStringCache(var1, var2, var3, var4);
   }

   public final String[] getValues() {
      return this.cache.getValues();
   }

   public final boolean remove(String var1) {
      return this.cache.remove(var1);
   }

   public final void update(T var1) {
      this.cache.store(this.getValueFromObject((T)var1));
   }

   public final void update(Collection<T> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Object var4 : var1) {
         var2.add(this.getValueFromObject((T)var4));
      }

      this.cache.store(var2);
   }

   protected abstract String getValueFromObject(T var1);
}
