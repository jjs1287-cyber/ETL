package org.etlforge.util.preference;

public final class PreferencesKey {
   private final Class<?> clazz;
   private final String suffix;
   private final String name;

   public PreferencesKey(Class<?> var1) {
      this(var1, null, null);
   }

   public PreferencesKey(Class<?> var1, String var2) {
      this(var1, null, var2);
   }

   public PreferencesKey(Class<?> var1, String var2, String var3) {
      this.clazz = var1;
      this.suffix = var2;
      this.name = var3;
   }

   public Class<?> keyClass() {
      return this.clazz;
   }

   public String keyName() {
      return this.name;
   }

   public String keySuffix() {
      return this.suffix;
   }
}
