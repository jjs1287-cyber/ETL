package org.etlforge.util;

import com.google.common.base.Strings;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public final class StringProperty implements Property<String> {
   private final DefaultProperty<String> impl;
   private boolean autoTrim;

   public StringProperty(String var1, PropertyChangeSupport var2) {
      this(var1, null, var2);
   }

   public StringProperty(String var1, String var2, PropertyChangeSupport var3) {
      this.impl = new DefaultProperty<>(var1, var2, var3);
   }

   public void setAutoTrim(boolean var1) {
      if (var1 != this.autoTrim) {
         this.autoTrim = var1;
         if (var1) {
            String var2 = this.get();
            if (var2 == null) {
               return;
            }

            String var3 = var2.trim();
            if (var3.length() < var2.length()) {
               this.set(var3);
            }
         }
      }
   }

   @Override
   public String name() {
      return this.impl.name();
   }

   public String get() {
      return this.impl.get();
   }

   @Override
   public boolean isNull() {
      return this.impl.isNull();
   }

   public void set(String var1) {
      if (this.autoTrim && var1 != null) {
         this.impl.set(var1.trim());
      } else {
         this.impl.set(var1);
      }
   }

   public void setSilently(String var1) {
      if (this.autoTrim && var1 != null) {
         this.impl.setSilently(var1.trim());
      } else {
         this.impl.setSilently(var1);
      }
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

   public boolean isEmpty() {
      return Strings.isNullOrEmpty(this.get());
   }
}
