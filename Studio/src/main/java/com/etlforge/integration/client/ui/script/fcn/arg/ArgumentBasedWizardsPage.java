package com.etlforge.integration.client.ui.script.fcn.arg;

import com.etlforge.integration.client.ui.script.fcn.AbstractSimpleFunctionWizardPage;
import com.etlforge.integration.client.ui.script.fcn.VariableSelector;
import org.etlforge.integration.data.script.Function;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeListener;

final class ArgumentBasedWizardsPage extends AbstractSimpleFunctionWizardPage {
   private final ArgumentFieldsPanel fieldsPanel;

   public ArgumentBasedWizardsPage(Function var1, ArgumentFieldsPanel var2, VariableSelector var3, UiProvider var4) {
      super(var1, var3, var4);
      this.fieldsPanel = var2;
      this.setInputArea(var2);
      VariableDestinationCoordinator.install(var3, var2);
   }

   @Override
   public void addChangeListener(InputChangeListener var1) {
      this.fieldsPanel.addChangeListener(var1);
   }

   @Override
   public void removeChangeListener(InputChangeListener var1) {
      this.fieldsPanel.removeChangeListener(var1);
   }

   @Override
   public boolean isInputValid() {
      return this.fieldsPanel.isInputValid();
   }
}
