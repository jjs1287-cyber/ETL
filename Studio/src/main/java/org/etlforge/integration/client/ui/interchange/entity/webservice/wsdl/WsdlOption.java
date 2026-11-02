package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.util.option.InputOption;
import org.etlforge.ui.util.option.OptionSelector;

public class WsdlOption implements InputOption<WsdlSelector> {
   private final WsdlSelector wsdlDefiner;
   private final String label;
   private boolean isActive;

   public WsdlOption(WsdlSelector var1, String var2) {
      this.wsdlDefiner = var1;
      var1.setActive(false);
      this.label = var2;
      this.isActive = false;
   }

   public WsdlSelector getValue() {
      return this.wsdlDefiner;
   }

   @Override
   public boolean isActive() {
      return this.isActive;
   }

   @Override
   public void setActive(boolean var1) {
      this.isActive = var1;
      this.wsdlDefiner.setActive(var1);
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(this.label);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.wsdlDefiner.getUi().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.wsdlDefiner.getUi().requestFocus();
   }
}
