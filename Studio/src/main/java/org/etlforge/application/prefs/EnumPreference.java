package org.etlforge.application.prefs;

public class EnumPreference<T extends Enum<T>> extends FixedSetPreference<T> {
   private final Class<T> enumType;

   public EnumPreference(Class<?> var1, String var2, String var3, T var4, Class<T> var5) {
      this(var1, new String[]{var2}, var3, (T)var4, var5);
   }

   public EnumPreference(Class<?> var1, String[] var2, String var3, T var4, Class<T> var5) {
      super(var1, var2, var3, (T[])var5.getEnumConstants(), (T)var4, "name");
      this.enumType = var5;
   }

   public Class<T> getEnumType() {
      return this.enumType;
   }

   protected T convertIdToValue(String var1) {
      try {
         return Enum.valueOf(this.enumType, var1);
      } catch (RuntimeException var3) {
         return null;
      }
   }
}
