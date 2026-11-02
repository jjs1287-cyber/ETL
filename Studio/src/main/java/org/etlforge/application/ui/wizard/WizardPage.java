package org.etlforge.application.ui.wizard;

import org.etlforge.ui.UiProvider;

public interface WizardPage extends UiProvider {
   String getTitle();

   void addToWizard();

   void removeFromWizard();

   Wizard getWizard();

   boolean canContinue();

   boolean prepareForForwardNavigation();

   boolean prepareForBackwardNavigation();

   String getHelpId();
}
