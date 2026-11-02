package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class DebuggingCromHeader extends CromHeaderElement {
   public DebuggingCromHeader() {
      super("DebuggingHeader", CromHeaderValue.string("debugLevel"));
   }
}
