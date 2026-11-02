package org.etlforge.integration.salesforce.wsdl.partner.crom;

public final class StreamingEnabledCromHeader extends CromHeaderElement {
   public StreamingEnabledCromHeader() {
      super("StreamingEnabledHeader", CromHeaderValue.bool("streamingEnabled"));
   }
}
