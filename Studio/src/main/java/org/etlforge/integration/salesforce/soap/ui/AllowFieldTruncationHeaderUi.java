package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.AllowFieldTruncationHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class AllowFieldTruncationHeaderUi extends BooleanSoapHeaderUi {
   public AllowFieldTruncationHeaderUi(AllowFieldTruncationHeader var1) {
      super("Allow field truncation", var1.allowFieldTruncation());
   }

   @Override
   protected SoapHeader createHeader(boolean var1) {
      return new AllowFieldTruncationHeader(var1);
   }
}
