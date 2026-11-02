package org.etlforge.application.ui.wizard;

public interface WizardListener<T> {
   void wizardCompleted(T var1);

   void wizardFailed(Throwable var1);

   void wizardCancelled();
}
