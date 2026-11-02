package org.etlforge.application.ui.wizard;

public abstract class WizardAdapter<T> implements WizardListener<T> {
   @Override
   public void wizardCancelled() {
   }

   @Override
   public void wizardCompleted(T var1) {
   }

   @Override
   public void wizardFailed(Throwable var1) {
   }
}
