package org.etlforge.integration.client.ui.interchange.component.page.entity;

import java.util.List;

class CompoundSaveConsequence implements SaveConsequence {
   private final List<SaveConsequence> parts;

   public CompoundSaveConsequence(List<SaveConsequence> var1) {
      this.parts = var1;
   }

   @Override
   public boolean isAllowed() {
      for (SaveConsequence var2 : this.parts) {
         if (!var2.isAllowed()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public void apply() {
      for (SaveConsequence var2 : this.parts) {
         var2.apply();
      }
   }
}
