package org.etlforge.integration.client.entity.valuecache;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.name.Nameable;

public final class DefaultHistory implements History, Nameable {
   private final String propertyName;
   private final CacheStorage storage;
   private final int maxSize;
   private LinkedHashSet<String> values;
   private final Object lock = new Object();

   public DefaultHistory(String var1, CacheStorage var2, int var3) {
      this.propertyName = Preconditions.checkNotNull(var1, "propertyName");
      this.storage = Preconditions.checkNotNull(var2, "storage");
      this.maxSize = ArgCheck.checkPositive(var3, "maxSize");
   }

   @Override
   public String getName() {
      return this.propertyName;
   }

   public void addValues(Iterable<String> var1) {
      this.addValues(var1, true);
   }

   private void addValues(Iterable<String> var1, boolean var2) {
      synchronized (this.lock) {
         Iterables.addAll(this.getInternalValues(), var1);
         this.trimToSize();
         if (var2) {
            this.saveToDisk();
         }
      }
   }

   private LinkedHashSet<String> getInternalValues() {
      if (this.values == null) {
         this.values = Sets.newLinkedHashSet(this.storage.read());
      }

      return this.values;
   }

   private void trimToSize() {
      if (this.values.size() > this.maxSize) {
         this.values = Sets.newLinkedHashSet(Iterables.skip(this.values, this.values.size() - this.maxSize));
      }
   }

   private void saveToDisk() {
      this.storage.write(this.values);
   }

   @Override
   public ImmutableSet<String> getValues() {
      synchronized (this.lock) {
         return ImmutableSet.copyOf(this.getInternalValues());
      }
   }

   @Override
   public boolean delete(String var1) {
      synchronized (this.lock) {
         boolean var3 = this.getInternalValues().remove(var1);
         if (var3) {
            this.saveToDisk();
         }

         return var3;
      }
   }
}
