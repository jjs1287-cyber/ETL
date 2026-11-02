package org.etlforge.integration.client.ui.connector.netsuite.endpoint.page;

import javax.swing.text.JTextComponent;
import org.etlforge.integration.design.netsuite.NetSuiteWsdlDownloadUrl;
import org.etlforge.ui.input.TextInputField2;

public class NetSuiteWsdlDownloadUrlInputField extends TextInputField2 {
   public NetSuiteWsdlDownloadUrlInputField(String var1, JTextComponent var2, String var3) {
      super(var1, var2, var3);
   }

   @Override
   public boolean isInputValid(String var1) {
      NetSuiteWsdlDownloadUrl var2 = new NetSuiteWsdlDownloadUrl(var1);
      return var2.isValid();
   }

   @Override
   public String getInvalidMessage(String var1) {
      NetSuiteWsdlDownloadUrl var2 = new NetSuiteWsdlDownloadUrl(var1);
      return var2.getInvalidMessage();
   }
}
