package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class EmailCromHeader extends CromHeaderElement {
   public EmailCromHeader() {
      super(
         "EmailHeader", CromHeaderValue.bool("triggerAutoResponseEmail"), CromHeaderValue.bool("triggerOtherEmail"), CromHeaderValue.bool("triggerUserEmail")
      );
   }
}
