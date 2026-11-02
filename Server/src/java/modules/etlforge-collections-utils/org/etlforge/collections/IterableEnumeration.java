package org.etlforge.collections;

import java.util.Enumeration;
import java.util.Iterator;

public final class IterableEnumeration<T> implements Iterable<T>, Enumeration<T> {
   private final Enumeration<T> enumeration;
   private final Iterator<T> iterator;

   public static <E> IterableEnumeration<E> of(Enumeration<E> var0) {
      return new IterableEnumeration(var0);
   }

   public IterableEnumeration(Enumeration<T> var1) {
      this.enumeration = var1;
      this.iterator = new Iterator<T>() {
         @Override
         public boolean hasNext() {
            return IterableEnumeration.this.enumeration.hasMoreElements();
         }

         @Override
         public T next() {
            return IterableEnumeration.this.enumeration.nextElement();
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public IterableEnumeration(Iterable<T> var1) {
      this(var1.iterator());
   }

   public IterableEnumeration(Iterator<T> var1) {
      this.iterator = var1;
      this.enumeration = new Enumeration<T>() {
         @Override
         public boolean hasMoreElements() {
            return IterableEnumeration.this.iterator.hasNext();
         }

         @Override
         public T nextElement() {
            return IterableEnumeration.this.iterator.next();
         }
      };
   }

   @Override
   public Iterator<T> iterator() {
      return this.iterator;
   }

   @Override
   public boolean hasMoreElements() {
      return this.enumeration.hasMoreElements();
   }

   @Override
   public T nextElement() {
      return this.enumeration.nextElement();
   }
}
