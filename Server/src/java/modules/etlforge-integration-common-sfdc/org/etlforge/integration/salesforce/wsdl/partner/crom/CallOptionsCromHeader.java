package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class CallOptionsCromHeader extends CromHeaderElement {
   public CallOptionsCromHeader() {
      super("CallOptions", CromHeaderValue.string("client").optional(), CromHeaderValue.string("defaultNamespace").optional());
   }
}
