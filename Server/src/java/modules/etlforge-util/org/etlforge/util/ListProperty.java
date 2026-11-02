package org.etlforge.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ListProperty<T> implements Property<List<T>>, Iterable<T> {
   private final Property<List<T>> impl;
   private volatile boolean useEmptyListForNull = true;
   private volatile boolean doDefensiveCopy = true;

   public ListProperty(String var1, PropertyChangeSupport var2) {
      this.impl = new DefaultProperty<>(var1, var2);
   }

   public void setUseEmptyListForNull(boolean var1) {
      this.useEmptyListForNull = var1;
   }

   public void setDoDefensiveCopy(boolean var1) {
      this.doDefensiveCopy = var1;
   }

   @Override
   public String name() {
      return this.impl.name();
   }

   public List<T> get() {
      List var1 = this.impl.get();
      if (var1 == null) {
         return this.useEmptyListForNull ? Collections.emptyList() : null;
      } else {
         return this.doDefensiveCopy ? Lists.newArrayList(var1) : var1;
      }
   }

   public boolean contains(T var1) {
      List var2 = this.impl.get();
      return var2 != null && var2.contains(var1);
   }

   public boolean isEmpty() {
      List var1 = this.impl.get();
      return var1 == null || var1.isEmpty();
   }

   public int size() {
      return this.isNull() ? 0 : this.impl.get().size();
   }

   @Override
   public boolean isNull() {
      return this.impl.isNull();
   }

   public void clear() {
      this.impl.set(Collections.emptyList());
   }

   public void set(List<T> var1) {
      List var2 = this.valueToSet(var1);
      this.rawSet(var2);
   }

   private void rawSet(List<T> var1) {
      this.impl.set(var1);
   }

   public void setIterable(Iterable<T> var1) {
      this.set(ImmutableList.copyOf(var1));
   }

   private List<T> valueToSet(List<T> var1) {
      return var1 == null ? null : (this.doDefensiveCopy ? Lists.newArrayList(var1) : var1);
   }

   public void setSilently(List<T> var1) {
      this.impl.setSilently(this.valueToSet(var1));
   }

   public void add(T var1) {
      if (this.isNull()) {
         this.set(Collections.singletonList((T)var1));
      } else {
         ArrayList var2 = this.copyList();
         var2.add(var1);
         this.rawSet(var2);
      }
   }

   public void addAll(Collection<T> var1) {
      if (!var1.isEmpty()) {
         if (this.isNull()) {
            this.set(Lists.newArrayList(var1));
         } else {
            ArrayList var2 = this.copyList();
            var2.addAll(var1);
            this.rawSet(var2);
         }
      }
   }

   private ArrayList<T> copyList() {
      return Lists.newArrayList(this.impl.get());
   }

   public void remove(T var1) {
      if (!this.isEmpty()) {
         if (this.contains((T)var1)) {
            ArrayList var2 = this.copyList();
            var2.remove(var1);
            this.rawSet(var2);
         }
      }
   }

   public void removeAll(Iterable<? extends T> var1) {
      this.removeAll(Lists.newArrayList(var1));
   }

   public void removeAll(Collection<? extends T> var1) {
      if (!this.isEmpty()) {
         ArrayList var2 = this.copyList();
         boolean var3 = var2.removeAll(var1);
         if (var3) {
            this.rawSet(var2);
         }
      }
   }

   public void identityReplace(T var1, T var2) {
      if (!this.isNull()) {
         boolean var3 = false;
         List var4 = this.impl.get();
         ArrayList var5 = Lists.newArrayList();

         for (Object var7 : var4) {
            if (var7 == var1) {
               var5.add(var2);
               var3 = true;
            } else {
               var5.add(var7);
            }
         }

         if (var3) {
            this.impl.set(var5);
         }
      }
   }

   public void swap(int var1, int var2) {
      if (var1 != var2) {
         ArrayList var3 = this.copyList();
         Collections.swap(var3, var1, var2);
         this.rawSet(var3);
      }
   }

   public T itemAt(int var1) {
      return this.impl.get().get(var1);
   }

   @Override
   public void setAllowsNull(boolean var1) {
      this.impl.setAllowsNull(var1);
   }

   @Override
   public void addListener(PropertyChangeListener var1) {
      this.impl.addListener(var1);
   }

   @Override
   public void removeListener(PropertyChangeListener var1) {
      this.impl.removeListener(var1);
   }

   @Override
   public String toString() {
      List var1 = this.impl.get();
      return var1 == null ? "null" : var1.toString();
   }

   @Override
   public Iterator<T> iterator() {
      if (this.isNull()) {
         return Iterators.emptyIterator();
      }

      List var1 = this.impl.get();
      return Iterators.unmodifiableIterator(var1.iterator());
   }
}
