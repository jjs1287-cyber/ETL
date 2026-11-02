package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import org.etlforge.util.persist.Persistor;

public final class BooleanSourceAndTargetSetting {
   private final String name;
   private final boolean defaultValue;
   private final Persistor settings;
   private Boolean sourceValue;
   private Boolean targetValue;

   public BooleanSourceAndTargetSetting(String var1, boolean var2, Persistor var3) {
      this.name = var1;
      this.defaultValue = var2;
      this.settings = var3;
      this.restore();
   }

   private void restore() {
      this.sourceValue = this.defaultValue;
      this.targetValue = this.defaultValue;
      Persistor var1 = this.settings.getFirstChild(this.name);
      if (var1 != null) {
         Boolean var2 = var1.getBoolean("source");
         if (var2 != null) {
            this.sourceValue = var2;
         }

         var2 = var1.getBoolean("target");
         if (var2 != null) {
            this.targetValue = var2;
         }
      }
   }

   public boolean forSource() {
      return this.sourceValue;
   }

   public boolean forTarget() {
      return this.targetValue;
   }

   public void save(boolean var1, boolean var2) {
      this.settings.removeChildren(this.name);
      if (var1 != this.defaultValue || var2 != this.defaultValue) {
         Persistor var3 = this.settings.createChild(this.name);
         if (var1 != this.defaultValue) {
            var3.putBoolean("source", var1);
         }

         if (var2 != this.defaultValue) {
            var3.putBoolean("target", var2);
         }
      }

      this.sourceValue = var1;
      this.targetValue = var2;
   }
}
