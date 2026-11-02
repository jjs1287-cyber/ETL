package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class AllowFieldTruncationCromHeader extends CromHeaderElement {
   public AllowFieldTruncationCromHeader() {
      super("AllowFieldTruncationHeader", CromHeaderValue.bool("allowFieldTruncation"));
   }
}
