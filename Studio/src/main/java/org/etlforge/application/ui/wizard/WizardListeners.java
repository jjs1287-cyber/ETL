package org.etlforge.application.ui.wizard;

import org.etlforge.util.event.AbstractListenerManager;

public class WizardListeners<T> extends AbstractListenerManager<WizardListener<T>> {
   protected WizardListener<T> createNotifier() {
      return new WizardListener<T>() {
         @Override
         public void wizardCancelled() {
            for (WizardListener var2 : WizardListeners.this.getListeners()) {
               var2.wizardCancelled();
            }
         }

         @Override
         public void wizardCompleted(T var1) {
            for (WizardListener var3 : WizardListeners.this.getListeners()) {
               var3.wizardCompleted(var1);
            }
         }

         @Override
         public void wizardFailed(Throwable var1) {
            for (WizardListener var3 : WizardListeners.this.getListeners()) {
               var3.wizardFailed(var1);
            }
         }
      };
   }
}
