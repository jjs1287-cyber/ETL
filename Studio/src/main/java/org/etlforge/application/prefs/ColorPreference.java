package org.etlforge.application.prefs;

import java.awt.Color;

public class ColorPreference extends Preference<Color> {
   public ColorPreference(Class<?> var1, String[] var2, String var3, Color var4) {
      super(var1, var2, var3, var4);
   }

   public Color get() {
      String var1 = this.getNode().get(this.getKey(), null);
      if (var1 != null) {
         try {
            return new Color(Integer.parseInt(var1), true);
         } catch (NumberFormatException var3) {
         }
      }

      return this.getDefaultValue();
   }

   public void set(Color var1) {
      Color var2 = this.get();
      if (var1 != null) {
         this.getNode().put(this.getKey(), String.valueOf(var1.getRGB()));
      } else {
         this.clear();
      }

      this.fireValueChanged(var2, var1);
   }
}
