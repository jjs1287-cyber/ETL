package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;

public interface SoapHeaderUi {
   SoapHeader getDefinedSoapHeader();

   void layoutIn(TwoColumnLayout var1);

   void requestFocus();

   void addToParent(InputPanel var1);
}
