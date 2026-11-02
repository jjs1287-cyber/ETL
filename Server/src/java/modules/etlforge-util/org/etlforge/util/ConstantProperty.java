package org.etlforge.util;

import java.beans.PropertyChangeListener;

public final class ConstantProperty<T> implements Property<T> {
   private final String name;
   private final T value;

   public static <V> ConstantProperty<V> from(String var0, V var1) {
      return (ConstantProperty<V>)(new ConstantProperty<>(var0, var1));
   }

   public ConstantProperty(String var1, T var2) {
      this.name = var1;
      this.value = (T)var2;
   }

   @Override
   public String name() {
      return this.name;
   }

   @Override
   public T get() {
      return this.value;
   }

   @Override
   public boolean isNull() {
      return this.value == null;
   }

   @Override
   public void set(T var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setSilently(T var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void setAllowsNull(boolean var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public void addListener(PropertyChangeListener var1) {
   }

   @Override
   public void removeListener(PropertyChangeListener var1) {
   }
}
