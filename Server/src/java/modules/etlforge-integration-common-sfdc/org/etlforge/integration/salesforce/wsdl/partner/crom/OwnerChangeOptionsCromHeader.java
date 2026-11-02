package org.etlforge.integration.salesforce.wsdl.partner.crom;

public final class OwnerChangeOptionsCromHeader extends CromHeaderElement {
   public OwnerChangeOptionsCromHeader() {
      super("OwnerChangeOptions", CromHeaderValue.bool("transferAttachments"), CromHeaderValue.bool("transferOpenActivities"));
   }
}
