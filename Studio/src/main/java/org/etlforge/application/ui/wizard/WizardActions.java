package org.etlforge.application.ui.wizard;

import java.awt.event.ActionEvent;
import javax.swing.Action;

public final class WizardActions {
   public static Action getPreviousPageAction(Wizard var0) {
      return new WizardAction(var0, "< Back", null) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            this.getWizard().previousPageRequested();
         }
      };
   }

   public static Action getNextPageAction(Wizard var0) {
      return new WizardAction(var0, "Next >", null) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            this.getWizard().nextPageRequested();
         }
      };
   }

   public static Action getSaveAction(Wizard var0) {
      return new WizardAction(var0, "Finish", null) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            this.getWizard().saveRequested();
         }
      };
   }

   public static Action getCancelAction(Wizard var0) {
      return new WizardAction(var0, "Cancel", null) {
         @Override
         public void actionPerformed(ActionEvent var1) {
            this.getWizard().cancelWizard();
         }
      };
   }

   private WizardActions() {
   }
}
