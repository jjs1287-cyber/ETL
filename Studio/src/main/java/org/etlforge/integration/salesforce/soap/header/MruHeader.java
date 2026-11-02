package org.etlforge.integration.salesforce.soap.header;

public final class MruHeader extends BooleanSoapHeader {
   public static final String NAME = "MruHeader";
   public static final String FIELD = "updateMru";

   public MruHeader() {
      this(false);
   }

   public MruHeader(boolean var1) {
      super("MruHeader", "updateMru", var1);
   }

   public boolean isUpdateMru() {
      return this.value;
   }

   public static MruHeader fromString(String var0) {
      return new MruHeader("1".equals(var0));
   }
}
