package org.etlforge.application.prefs;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.prefs.Preferences;

public abstract class Preference<T> {
   private final Class<?> applicationClass;
   private final String key;
   private final String[] path;
   private final T defaultValue;
   private final List<PreferenceListener<T>> listeners;

   protected Preference(Class<?> var1, String[] var2, String var3, T var4) {
      this.applicationClass = var1;
      this.key = var3;
      this.path = (String[])var2.clone();
      this.defaultValue = (T)var4;
      this.listeners = new CopyOnWriteArrayList<>();
   }

   public final String getKey() {
      return this.key;
   }

   public final String[] getPath() {
      return (String[])this.path.clone();
   }

   public final void clear() {
      this.getNode().remove(this.key);
   }

   public final T getDefaultValue() {
      return this.defaultValue;
   }

   public final void addListener(PreferenceListener<T> var1) {
      this.listeners.add(var1);
   }

   public final void removeListener(PreferenceListener<T> var1) {
      this.listeners.remove(var1);
   }

   protected final void fireValueChanged(T var1, T var2) {
      if (!equals(var1, var2)) {
         for (PreferenceListener var4 : this.listeners) {
            var4.valueChanged(this, (T)var1, (T)var2);
         }
      }
   }

   protected final Preferences getNode() {
      Preferences var1 = Preferences.userNodeForPackage(this.applicationClass);

      for (String var5 : this.path) {
         var1 = var1.node(var5);
      }

      return var1;
   }

   private static boolean equals(Object var0, Object var1) {
      return var0 == null ? var1 == null : var0.equals(var1);
   }
}
