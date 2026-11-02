package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class SessionCromHeader extends CromHeaderElement {
   public SessionCromHeader() {
      super("SessionHeader", CromHeaderValue.string("sessionId"));
   }
}
