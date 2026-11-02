package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class MruCromHeader extends CromHeaderElement {
   public MruCromHeader() {
      super("MruHeader", CromHeaderValue.bool("updateMru"));
   }
}
