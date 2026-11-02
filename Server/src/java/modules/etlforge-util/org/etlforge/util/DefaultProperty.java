package org.etlforge.util;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DefaultProperty<T> implements Property<T> {
   private final String name;
   private final PropertyChangeSupport changeSupport;
   private T value;
   private boolean allowsNull;

   public DefaultProperty(String var1, PropertyChangeSupport var2) {
      this(var1, null, var2);
   }

   public DefaultProperty(String var1, T var2, PropertyChangeSupport var3) {
      Preconditions.checkNotNull(var1, "name");
      Preconditions.checkNotNull(var3, "changeSupport");
      this.name = var1;
      this.value = (T)var2;
      this.changeSupport = var3;
      this.allowsNull = true;
   }

   @Override
   public String name() {
      return this.name;
   }

   @Override
   public synchronized T get() {
      return this.value;
   }

   @Override
   public synchronized boolean isNull() {
      return this.value == null;
   }

   @Override
   public void set(T var1) {
      this.setImpl((T)var1, true);
   }

   @Override
   public void setSilently(T var1) {
      this.setImpl((T)var1, false);
   }

   @Override
   public synchronized void setAllowsNull(boolean var1) {
      if (!var1 && this.isNull()) {
         throw new IllegalStateException("The current value is NULL, this Property cannot disallow NULL values.");
      }

      this.allowsNull = var1;
   }

   @Override
   public String toString() {
      return String.valueOf(this.get());
   }

   private void setImpl(T var1, boolean var2) {
      Object var3 = null;
      synchronized (this) {
         if (var1 == null && !this.allowsNull) {
            throw new NullPointerException("This property does not accept null values");
         }

         if (var2 && this.changeSupport != null) {
            if (this.value == var1) {
               return;
            }

            var3 = this.value;
            this.value = (T)var1;
         } else {
            this.value = (T)var1;
         }
      }

      if (var2) {
         this.changeSupport.firePropertyChange(this.name, var3, var1);
      }
   }

   @Override
   public void addListener(PropertyChangeListener var1) {
      this.changeSupport.addPropertyChangeListener(this.name, var1);
   }

   @Override
   public void removeListener(PropertyChangeListener var1) {
      this.changeSupport.removePropertyChangeListener(this.name, var1);
   }
}
