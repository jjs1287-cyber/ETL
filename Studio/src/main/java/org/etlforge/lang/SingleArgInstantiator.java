package org.etlforge.lang;

import java.lang.reflect.Constructor;

public class SingleArgInstantiator<T> {
   private final Class<T> clazz;
   private Class<?>[] argTypesInOrderToLook;
   private Object[] args;

   public static <E> SingleArgInstantiator<E> create(Class<E> var0) {
      return new SingleArgInstantiator(var0);
   }

   public SingleArgInstantiator(Class<T> var1) {
      this.clazz = var1;
   }

   public void setArgumentTypesInOrderToLookFor(Class<?>... var1) {
      this.argTypesInOrderToLook = var1;
   }

   public void setArguments(Object... var1) {
      this.args = var1;
   }

   public T instantiate() throws Exception {
      for (int var1 = 0; var1 < this.argTypesInOrderToLook.length; var1++) {
         Constructor var2 = KongaClassUtils.getSingleArgumentConstructor(this.clazz, this.argTypesInOrderToLook[var1]);
         if (var2 != null) {
            return (T)var2.newInstance(this.args[var1]);
         }
      }

      return null;
   }
}
