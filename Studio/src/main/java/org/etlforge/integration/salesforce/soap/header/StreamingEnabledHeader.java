package org.etlforge.integration.salesforce.soap.header;

public final class StreamingEnabledHeader extends BooleanSoapHeader {
   public static final String NAME = "StreamingEnabledHeader";
   public static final String FIELD = "streamingEnabled";

   public StreamingEnabledHeader() {
      this(false);
   }

   public StreamingEnabledHeader(boolean var1) {
      super("StreamingEnabledHeader", "streamingEnabled", var1);
   }

   public static StreamingEnabledHeader fromString(String var0) {
      return new StreamingEnabledHeader("1".equals(var0));
   }
}
