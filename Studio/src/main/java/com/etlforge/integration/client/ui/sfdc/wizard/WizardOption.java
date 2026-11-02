package com.etlforge.integration.client.ui.sfdc.wizard;

import javax.swing.JComponent;
import org.etlforge.system.Disposable;
import org.etlforge.ui.input.InputPanel;

public interface WizardOption extends Disposable {
   boolean allowsNavigation();

   void registerWithParent(InputPanel var1);

   JComponent getUi();
}
