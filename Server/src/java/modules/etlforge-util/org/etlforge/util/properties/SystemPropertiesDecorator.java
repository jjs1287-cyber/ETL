package org.etlforge.util.properties;

import com.google.common.base.Preconditions;

public class SystemPropertiesDecorator implements ReadOnlyProperties {
   private final ReadOnlyProperties wrappee;

   public SystemPropertiesDecorator(ReadOnlyProperties var1) {
      Preconditions.checkNotNull(var1, "wrappee");
      this.wrappee = var1;
   }

   @Override
   public boolean getBoolean(Class<?> var1, String var2, boolean var3) {
      return this.getBoolean(getClassKey(var1, var2), var3);
   }

   @Override
   public boolean getBoolean(String var1, boolean var2) {
      return System.getProperty(var1) != null ? Boolean.getBoolean(var1) : this.wrappee.getBoolean(var1, var2);
   }

   @Override
   public int getInt(Class<?> var1, String var2, int var3) {
      return this.getInt(getClassKey(var1, var2), var3);
   }

   @Override
   public int getInt(String var1, int var2) {
      Integer var3 = Integer.getInteger(var1);
      return var3 != null ? var3 : this.wrappee.getInt(var1, var2);
   }

   @Override
   public String getString(Class<?> var1, String var2, String var3) {
      return this.getString(getClassKey(var1, var2), var3);
   }

   @Override
   public String getString(String var1, String var2) {
      String var3 = System.getProperty(var1);
      return var3 != null ? var3 : this.wrappee.getString(var1, var2);
   }

   private static String getClassKey(Class<?> var0, String var1) {
      return var0.getName() + "." + var1;
   }
}
