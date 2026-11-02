package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class DisableFeedTrackingCromHeader extends CromHeaderElement {
   public DisableFeedTrackingCromHeader() {
      super("DisableFeedTrackingHeader", CromHeaderValue.bool("disableFeedTracking"));
   }
}
