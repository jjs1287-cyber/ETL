package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class AllOrNoneCromHeader extends CromHeaderElement {
   public AllOrNoneCromHeader() {
      super("AllOrNoneHeader", CromHeaderValue.bool("allOrNone"));
   }
}
