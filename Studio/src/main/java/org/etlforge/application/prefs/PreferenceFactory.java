package org.etlforge.application.prefs;

import java.awt.Color;
import java.io.File;

public class PreferenceFactory {
   private final Class<?> applicationClass;

   public PreferenceFactory(Class<?> var1) {
      if (var1 == null) {
         throw new NullPointerException("applicationClass");
      }

      this.applicationClass = var1;
   }

   public Class<?> getApplicationClass() {
      return this.applicationClass;
   }

   public BooleanPreference newBooleanPreference(String[] var1, String var2, Boolean var3) {
      return new BooleanPreference(this.applicationClass, var1, var2, var3);
   }

   public IntPreference newIntPreference(String[] var1, String var2, Integer var3, Integer var4, Integer var5) {
      return new IntPreference(this.applicationClass, var1, var2, var3, var4, var5);
   }

   public StringPreference newStringPreference(String[] var1, String var2, String var3) {
      return new StringPreference(this.applicationClass, var1, var2, var3);
   }

   public FilePreference newFilePreference(String[] var1, String var2, File var3) {
      return new FilePreference(this.applicationClass, var1, var2, var3);
   }

   public ColorPreference newColorPreference(String[] var1, String var2, Color var3) {
      return new ColorPreference(this.applicationClass, var1, var2, var3);
   }

   public <T extends Enum<T>> EnumPreference<T> newEnumPreference(String[] var1, String var2, T var3, Class<T> var4) {
      return new EnumPreference<>(this.applicationClass, var1, var2, (T)var3, var4);
   }

   public <T> FixedSetPreference<T> newFixedSetPreference(String[] var1, String var2, T[] var3, T var4, String var5) {
      return new FixedSetPreference<>(this.applicationClass, var1, var2, (T[])var3, (T)var4, var5);
   }

   public <T> FixedSetPreference<T> newFixedSetPreference(String[] var1, String var2, Iterable<T> var3, T var4, String var5) {
      return new FixedSetPreference<>(this.applicationClass, var1, var2, var3, (T)var4, var5);
   }
}
