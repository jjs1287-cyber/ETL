package org.etlforge.application.prefs;

public class StringPreference extends Preference<String> {
   public StringPreference(Class<?> var1, String[] var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public String get() {
      return this.getNode().get(this.getKey(), this.getDefaultValue());
   }

   public void set(String var1) {
      String var2 = this.get();
      if (var1 != null) {
         this.getNode().put(this.getKey(), var1);
      } else {
         this.clear();
      }

      this.fireValueChanged(var2, var1);
   }
}
