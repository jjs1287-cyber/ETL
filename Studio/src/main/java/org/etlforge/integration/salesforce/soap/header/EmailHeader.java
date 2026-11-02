package org.etlforge.integration.salesforce.soap.header;

import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.lang.Objects;

public final class EmailHeader extends SoapHeader {
   private final boolean triggerAutoResponseEmail;
   private final boolean triggerOtherEmail;
   private final boolean triggerUserEmail;

   public EmailHeader() {
      this(false, false, false);
   }

   public EmailHeader(boolean var1, boolean var2, boolean var3) {
      super("EmailHeader");
      this.triggerAutoResponseEmail = var1;
      this.triggerOtherEmail = var2;
      this.triggerUserEmail = var3;
   }

   public boolean isTriggerAutoResponseEmail() {
      return this.triggerAutoResponseEmail;
   }

   public boolean isTriggerOtherEmail() {
      return this.triggerOtherEmail;
   }

   public boolean isTriggerUserEmail() {
      return this.triggerUserEmail;
   }

   @Override
   public String getStringRepresentation() {
      String var1 = "";
      var1 = var1 + (this.triggerAutoResponseEmail ? "1" : "0");
      var1 = var1 + (this.triggerOtherEmail ? "1" : "0");
      return var1 + (this.triggerUserEmail ? "1" : "0");
   }

   public static EmailHeader fromString(String var0) {
      return var0.length() != 3 ? new EmailHeader() : new EmailHeader(var0.charAt(0) == '1', var0.charAt(1) == '1', var0.charAt(2) == '1');
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      var1.add(SoapHeader.Mapping.forBoolean("triggerAutoResponseEmail$", this.triggerAutoResponseEmail));
      var1.add(SoapHeader.Mapping.forBoolean("triggerOtherEmail$", this.triggerOtherEmail));
      var1.add(SoapHeader.Mapping.forBoolean("triggerUserEmail$", this.triggerUserEmail));
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.triggerAutoResponseEmail, this.triggerOtherEmail, this.triggerUserEmail);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof EmailHeader)) {
         return false;
      }

      EmailHeader var2 = (EmailHeader)var1;
      return this.triggerAutoResponseEmail == var2.triggerAutoResponseEmail
         && this.triggerOtherEmail == var2.triggerOtherEmail
         && this.triggerUserEmail == var2.triggerUserEmail;
   }

   @Override
   public String toString() {
      return String.format("Auto response: %b, Other: %b, User: %b", this.triggerAutoResponseEmail, this.triggerOtherEmail, this.triggerUserEmail);
   }
}
