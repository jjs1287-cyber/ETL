package org.etlforge.integration.salesforce.soap.header;

public final class DisableFeedTrackingHeader extends BooleanSoapHeader {
   public DisableFeedTrackingHeader() {
      this(false);
   }

   public DisableFeedTrackingHeader(boolean var1) {
      super("DisableFeedTrackingHeader", "disableFeedTracking", var1);
   }

   public boolean isFeedTrackingDisabled() {
      return this.value;
   }

   public static DisableFeedTrackingHeader fromString(String var0) {
      return new DisableFeedTrackingHeader("1".equals(var0));
   }
}
