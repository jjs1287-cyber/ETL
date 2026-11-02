package org.etlforge.integration.salesforce.wsdl.partner.crom;

public final class DebuggingInfoCromHeader extends CromHeaderElement {
   public static final String NAME = "DebuggingInfo";

   public DebuggingInfoCromHeader() {
      super("DebuggingInfo", CromHeaderValue.string("debugLog"));
   }
}
