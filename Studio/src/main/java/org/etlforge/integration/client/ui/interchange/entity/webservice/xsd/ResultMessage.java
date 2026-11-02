package org.etlforge.integration.client.ui.interchange.entity.webservice.xsd;

import javax.swing.JLabel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.label.HtmlLabel;
import org.etlforge.ui.widget.text.KongaTextField;

final class ResultMessage implements UiProvider {
   private final KongaTextField requestName;
   private final KongaTextField responseName;

   public ResultMessage(SoapSchemaModel var1) {
      this.requestName = createField(var1.getRequestXsdName());
      this.responseName = createField(var1.getResponseXsdName());
   }

   private static KongaTextField createField(String var0) {
      KongaTextField var1 = new KongaTextField(var0, 30);
      var1.setEditable(false);
      return var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = new TwoColumnPanel();
      byte var3 = 10;
      var2.withInsets(0, 0, var3, 0);
      HtmlLabel var4 = new HtmlLabel(
         "The XSD files have been generated and are ready for use.\nTo use the files, create a new Transformation, and select XML as source or target.\nThe XSDs that were created will show up in the list of available files.\n",
         400
      );
      var2.addRow(var4);
      var2.addRow(new JLabel("Request XSD: "), this.requestName);
      var2.addRow(new JLabel("Response XSD: "), this.responseName);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }
}
