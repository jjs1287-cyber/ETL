package org.etlforge.collections;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class ReferenceList<R> implements Iterable<R> {
   private List<Reference<R>> list = Collections.EMPTY_LIST;

   public static <E> ReferenceList<E> weakReferences() {
      return new ReferenceList<E>() {
         @Override
         protected Reference<E> ref(E var1) {
            return new WeakReference<>((E)var1);
         }
      };
   }

   public final ReferenceList<R> addAll(Iterable<? extends R> var1) {
      this.list = Lists.newArrayList();

      for (Object var3 : var1) {
         this.list.add(this.ref((R)var3));
      }

      return this;
   }

   public final ReferenceList<R> addAll(R... var1) {
      return this.addAll(Arrays.asList((R[])var1));
   }

   protected abstract Reference<R> ref(R var1);

   public final void add(R var1) {
      if (this.list == Collections.EMPTY_LIST) {
         this.list = Lists.newArrayList();
      }

      this.list.add(this.ref((R)var1));
   }

   public final boolean isEmpty() {
      return this.list.isEmpty();
   }

   public final List<R> getReferents() {
      ArrayList var1 = Lists.newArrayList();

      for (Reference var3 : this.list) {
         Object var4 = var3.get();
         if (var4 != null) {
            var1.add(var4);
         }
      }

      return var1;
   }

   @Override
   public final Iterator<R> iterator() {
      return new ReferenceList.Itr<>(this.list.iterator());
   }

   @VisibleForTesting
   final List<Reference<R>> refList() {
      return this.list;
   }

   public static void main(String[] var0) {
      Iterator var1 = Arrays.asList("a", "b", "c").iterator();
      System.out.println((String)var1.next());
   }

   private static final class Itr<R> implements Iterator<R> {
      private Iterator<Reference<R>> impl;
      private Optional<R> next;

      public Itr(Iterator<Reference<R>> var1) {
         this.impl = var1;
      }

      @Override
      public boolean hasNext() {
         if (this.next == null) {
            this.next = this.findNext();
         }

         return this.next.isPresent();
      }

      private Optional<R> findNext() {
         while (this.impl.hasNext()) {
            Object var1 = this.impl.next().get();
            if (var1 != null) {
               return Optional.of((R)var1);
            }
         }

         this.impl = null;
         return Optional.absent();
      }

      @Override
      public R next() {
         if (this.hasNext()) {
            Object var1 = this.next.get();
            this.next = this.findNext();
            return (R)var1;
         } else {
            throw new NoSuchElementException();
         }
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
