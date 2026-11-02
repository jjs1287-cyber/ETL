package org.etlforge.integration.salesforce.soap.header;

public final class AllowFieldTruncationHeader extends BooleanSoapHeader {
   public AllowFieldTruncationHeader() {
      this(false);
   }

   public AllowFieldTruncationHeader(boolean var1) {
      super("AllowFieldTruncationHeader", "allowFieldTruncation", var1);
   }

   public boolean allowFieldTruncation() {
      return this.value;
   }

   public static AllowFieldTruncationHeader fromString(String var0) {
      return new AllowFieldTruncationHeader("1".equals(var0));
   }
}
