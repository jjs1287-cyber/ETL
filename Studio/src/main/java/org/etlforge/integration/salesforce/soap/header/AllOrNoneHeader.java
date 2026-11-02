package org.etlforge.integration.salesforce.soap.header;

public final class AllOrNoneHeader extends BooleanSoapHeader {
   public AllOrNoneHeader() {
      this(false);
   }

   public AllOrNoneHeader(boolean var1) {
      super("AllOrNoneHeader", "allOrNone", var1);
   }

   public boolean isAllOrNone() {
      return this.value;
   }

   public static AllOrNoneHeader fromString(String var0) {
      return new AllOrNoneHeader("1".equals(var0));
   }
}
