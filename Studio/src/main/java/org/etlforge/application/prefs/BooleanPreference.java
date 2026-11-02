package org.etlforge.application.prefs;

public class BooleanPreference extends Preference<Boolean> {
   public BooleanPreference(Class<?> var1, String var2, String var3, Boolean var4) {
      this(var1, new String[]{var2}, var3, var4);
   }

   public BooleanPreference(Class<?> var1, String[] var2, String var3, Boolean var4) {
      super(var1, var2, var3, var4 != null ? var4 : Boolean.FALSE);
   }

   public boolean get() {
      return this.getNode().getBoolean(this.getKey(), this.getDefaultValue());
   }

   public void set(boolean var1) {
      Boolean var2 = this.get();
      this.getNode().putBoolean(this.getKey(), var1);
      this.fireValueChanged(var2, var1);
   }
}
