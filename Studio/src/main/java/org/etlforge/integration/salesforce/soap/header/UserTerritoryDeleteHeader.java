package org.etlforge.integration.salesforce.soap.header;

import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class UserTerritoryDeleteHeader extends SoapHeader {
   private final String transferToUserId;

   public UserTerritoryDeleteHeader() {
      this("");
   }

   public UserTerritoryDeleteHeader(String var1) {
      super("UserTerritoryDeleteHeader");
      this.transferToUserId = var1 == null ? "" : var1;
   }

   public String getTransferToUserId() {
      return this.transferToUserId;
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      if (!this.transferToUserId.isEmpty()) {
         var1.add(SoapHeader.Mapping.forString("transferToUserId$", this.transferToUserId));
      }
   }

   @Override
   public String getStringRepresentation() {
      return this.transferToUserId;
   }

   public static UserTerritoryDeleteHeader fromString(String var0) {
      return new UserTerritoryDeleteHeader(var0);
   }

   @Override
   public int hashCode() {
      return this.transferToUserId.hashCode();
   }

   @Override
   public boolean equals(Object var1) {
      return var1 == this
         ? true
         : var1 instanceof UserTerritoryDeleteHeader && this.transferToUserId.equals(((UserTerritoryDeleteHeader)var1).transferToUserId);
   }

   @Override
   public String toString() {
      return "transferToUserId: [" + this.transferToUserId + "]";
   }
}
