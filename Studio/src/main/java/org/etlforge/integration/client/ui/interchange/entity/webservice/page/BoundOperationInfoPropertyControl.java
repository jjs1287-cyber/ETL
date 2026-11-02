package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import javax.swing.JTextField;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.ui.util.UiUtils;

final class BoundOperationInfoPropertyControl implements BoundProperty {
   private final JTextField serviceField;
   private final JTextField portField;
   private final JTextField bindingField;
   private final JTextField operationField;
   private final JTextField soapAction;

   public BoundOperationInfoPropertyControl(JTextField var1, JTextField var2, JTextField var3, JTextField var4, JTextField var5) {
      this.serviceField = var1;
      this.portField = var2;
      this.bindingField = var3;
      this.operationField = var4;
      this.soapAction = var5;
   }

   @Override
   public String getName() {
      return "WebService#OperationInfo";
   }

   @Override
   public void newValue(Object var1) {
      if (var1 instanceof WebServiceOperationInfo) {
         final WebServiceOperationInfo var2 = (WebServiceOperationInfo)var1;
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               BoundOperationInfoPropertyControl.this.serviceField.setText(var2.getProperty("Service"));
               BoundOperationInfoPropertyControl.this.portField.setText(var2.getProperty("Port"));
               BoundOperationInfoPropertyControl.this.bindingField.setText(var2.getProperty("Binding"));
               BoundOperationInfoPropertyControl.this.operationField.setText(var2.getProperty("Operation"));
               BoundOperationInfoPropertyControl.this.soapAction.setText(var2.getProperty("SoapAction"));
            }
         });
      }
   }
}
