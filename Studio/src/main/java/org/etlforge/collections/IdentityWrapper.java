package org.etlforge.collections;

public class IdentityWrapper<T> {
   private final T object;

   public IdentityWrapper(T var1) {
      this.object = (T)var1;
   }

   public T getObject() {
      return this.object;
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof IdentityWrapper && ((IdentityWrapper)var1).object == this.object;
   }

   @Override
   public int hashCode() {
      return System.identityHashCode(this.object);
   }

   @Override
   public String toString() {
      return this.object == null ? "null" : this.object.toString();
   }
}
