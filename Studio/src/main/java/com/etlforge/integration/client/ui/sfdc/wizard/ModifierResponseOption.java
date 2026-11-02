package com.etlforge.integration.client.ui.sfdc.wizard;

import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaCheckBox;

public final class ModifierResponseOption implements WizardOption {
   private final KongaCheckBox useCustomResponse = new KongaCheckBox("I need to interact with the response from Salesforce", true);

   @Override
   public void registerWithParent(InputPanel var1) {
      var1.addInputField(new CheckBoxInputField("Use custom response", this.useCustomResponse));
   }

   @Override
   public boolean allowsNavigation() {
      return true;
   }

   @Override
   public void dispose() {
   }

   @Override
   public JComponent getUi() {
      return new OneColumnPanel()
         .withBorder(StyleSheet.getDefaultTitleBorder("Response Option"))
         .withInsets(0, 0, 10, 0)
         .stretchHorizontally()
         .add(this.useCustomResponse)
         .add(
            "<html>Select this option if you want to map information directly from the Salesforce response.<br>The wizard will create a response Transformation with default mappings, that you can edit later.</html>"
         )
         .invisible();
   }

   public boolean isCustomResponseSelected() {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), "isCustomResponseSelected");
      return this.useCustomResponse.isSelected();
   }
}
