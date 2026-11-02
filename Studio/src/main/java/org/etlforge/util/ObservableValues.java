package org.etlforge.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.IdentityHashMap;

public final class ObservableValues {
   public static ObservableValue<Boolean> constantBoolean(boolean var0) {
      return new ObservableValues.Constant<>(var0);
   }

   public static <T> ObservableValue<T> constant(T var0) {
      Preconditions.checkNotNull(var0);
      return new ObservableValues.Constant<>((T)var0);
   }

   public static <T> ObservableValue<T> fromProperty(Property<T> var0) {
      Preconditions.checkNotNull(var0);
      return new ObservableValues.FromProperty<>(var0);
   }

   private static final class Constant<V> implements ObservableValue<V>, Serializable {
      private final V value;

      public Constant(V var1) {
         assert var1 != null;
         this.value = (V)var1;
      }

      @Override
      public V get() {
         return this.value;
      }

      @Override
      public void addListener(ObservableValue.Listener<? super V> var1) {
      }

      @Override
      public void removeListener(ObservableValue.Listener<? super V> var1) {
      }

      @Override
      public int hashCode() {
         return this.value.hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this ? true : var1 instanceof ObservableValues.Constant && this.value.equals(((ObservableValues.Constant)var1).value);
      }

      @Override
      public String toString() {
         return this.value.toString();
      }
   }

   private static class FromProperty<V> implements ObservableValue<V> {
      private final Property<V> property;
      private final IdentityHashMap<ObservableValue.Listener, PropertyChangeListener> listeners = Maps.newIdentityHashMap();

      public FromProperty(Property<V> var1) {
         this.property = var1;
      }

      @Override
      public V get() {
         return this.property.get();
      }

      @Override
      public void addListener(final ObservableValue.Listener<? super V> var1) {
         if (!this.listeners.containsKey(var1)) {
            PropertyChangeListener var2 = new PropertyChangeListener() {
               @Override
               public void propertyChange(PropertyChangeEvent var1x) {
                  Object var2x = var1x.getOldValue();
                  Object var3 = var1x.getNewValue();
                  var1.valueChanged(FromProperty.this, (V)var2x, (V)var3);
               }
            };
            this.listeners.put(var1, var2);
            this.property.addListener(var2);
         }
      }

      @Override
      public void removeListener(ObservableValue.Listener<? super V> var1) {
         PropertyChangeListener var2 = this.listeners.remove(var1);
         if (var2 != null) {
            this.property.removeListener(var2);
         }
      }

      @Override
      public String toString() {
         return this.property.toString();
      }
   }
}
