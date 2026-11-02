package org.etlforge.integration.client.ui.entity.validation;

import org.etlforge.application.prefs.BooleanPreference;

public final class CompoundValidationStatusIndicator implements ValidationStatusIndicator {
   private boolean active;
   private final Iterable<? extends ValidationStatusIndicator> indicators;
   private final DecorateInvalidItemsAction action;

   public CompoundValidationStatusIndicator(Iterable<? extends ValidationStatusIndicator> var1) {
      this.indicators = var1;
      this.action = new DecorateInvalidItemsAction(this);
   }

   @Override
   public boolean isIndicatingValidationStatus() {
      return this.active;
   }

   @Override
   public void setIndicateValidationStatus(boolean var1) {
      if (this.active != var1) {
         this.active = var1;

         for (ValidationStatusIndicator var3 : this.indicators) {
            var3.setIndicateValidationStatus(var1);
         }
      }
   }

   public DecorateInvalidItemsAction getSwitch() {
      return this.action;
   }

   public void setPreference(BooleanPreference var1) {
      this.action.setPreference(var1);
   }
}
