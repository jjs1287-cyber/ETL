package org.etlforge.integration.salesforce.soap.header;

import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

abstract class BooleanSoapHeader extends SoapHeader {
   private final String valueName;
   protected final boolean value;

   protected BooleanSoapHeader(String var1, String var2, boolean var3) {
      super(var1);
      this.valueName = var2;
      this.value = var3;
   }

   protected BooleanSoapHeader(String var1, String var2, String var3) {
      this(var1, var2, "1".equals(var3));
   }

   @Override
   protected final void collectMappings(List<SoapHeader.Mapping> var1) {
      var1.add(SoapHeader.Mapping.forBoolean(this.valueName + "$", this.value));
   }

   @Override
   public final String getStringRepresentation() {
      return this.value ? "1" : "0";
   }

   @Override
   public final int hashCode() {
      return Boolean.valueOf(this.value).hashCode();
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 == null ? false : this.getClass().equals(var1.getClass()) && this.value == ((BooleanSoapHeader)var1).value;
      }
   }

   @Override
   public final String toString() {
      return "" + this.value;
   }
}
