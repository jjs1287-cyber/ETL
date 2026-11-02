package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.MruHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class MruHeaderUi extends BooleanSoapHeaderUi {
   public MruHeaderUi(MruHeader var1) {
      super("Update MRU", var1.isUpdateMru());
   }

   @Override
   protected SoapHeader createHeader(boolean var1) {
      return new MruHeader(var1);
   }
}
