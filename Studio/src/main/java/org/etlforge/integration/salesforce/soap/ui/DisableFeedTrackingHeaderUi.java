package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.DisableFeedTrackingHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class DisableFeedTrackingHeaderUi extends BooleanSoapHeaderUi {
   public DisableFeedTrackingHeaderUi(DisableFeedTrackingHeader var1) {
      super(var1.getName(), var1.isFeedTrackingDisabled());
   }

   @Override
   protected SoapHeader createHeader(boolean var1) {
      return new DisableFeedTrackingHeader();
   }
}
