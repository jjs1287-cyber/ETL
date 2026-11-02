package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.AllOrNoneHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class AllOrNoneHeaderUi extends BooleanSoapHeaderUi {
   public AllOrNoneHeaderUi(AllOrNoneHeader var1) {
      super("All or none", var1.isAllOrNone());
   }

   @Override
   protected SoapHeader createHeader(boolean var1) {
      return new AllOrNoneHeader(var1);
   }
}
