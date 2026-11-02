package com.etlforge.integration.client.ui.script.fcn;

import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;

public interface SimpleFunctionWizardPage extends UiProvider {
   void showTestSection();

   void postDisplay();

   void addChangeListener(InputChangeListener var1);

   void removeChangeListener(InputChangeListener var1);

   boolean isInputValid();
}
