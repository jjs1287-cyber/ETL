package org.etlforge.integration.client.ui.entity;

import org.etlforge.application.prefs.BooleanPreference;

public final class CompoundDeployDirtyIndicator implements DeployDirtyIndicator {
   private boolean active;
   private final Iterable<? extends DeployDirtyIndicator> indicators;
   private final DecorateDeployDirtyAction action;

   public CompoundDeployDirtyIndicator(Iterable<? extends DeployDirtyIndicator> var1) {
      this.indicators = var1;
      this.action = new DecorateDeployDirtyAction(this);
   }

   public void setPreference(BooleanPreference var1) {
      this.action.setPreference(var1);
   }

   @Override
   public boolean isDecoratingDeployDirtyItems() {
      return this.active;
   }

   @Override
   public void setDecorateDeployDirtyItems(boolean var1) {
      if (this.active != var1) {
         this.active = var1;

         for (DeployDirtyIndicator var3 : this.indicators) {
            var3.setDecorateDeployDirtyItems(var1);
         }
      }
   }

   public DecorateDeployDirtyAction getSwitch() {
      return this.action;
   }
}
