package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.ui.util.UiUtils;

final class BoundWsdlPropertyControl implements BoundProperty {
   private final JTextComponent textField;

   public BoundWsdlPropertyControl(JTextComponent var1) {
      this.textField = var1;
   }

   @Override
   public String getName() {
      return "WebService#WsdlFile";
   }

   @Override
   public void newValue(Object var1) {
      final String var2 = var1 instanceof WsdlFile ? ((WsdlFile)var1).getLocator() : "";
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            BoundWsdlPropertyControl.this.textField.setText(var2);
         }
      });
   }
}
