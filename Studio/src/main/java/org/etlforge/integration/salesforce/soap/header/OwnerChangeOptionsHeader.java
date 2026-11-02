package org.etlforge.integration.salesforce.soap.header;

import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.lang.Objects;

public final class OwnerChangeOptionsHeader extends SoapHeader {
   public static final String NAME = "OwnerChangeOptions";
   public static final String TRANSFER_ATTACHMENTS = "transferAttachments";
   public static final String TRANSFER_OPEN_ACTIVITIES = "transferOpenActivities";
   private final boolean transferAttachments;
   private final boolean transferOpenActivities;

   public OwnerChangeOptionsHeader() {
      this(true, true);
   }

   public OwnerChangeOptionsHeader(boolean var1, boolean var2) {
      super("OwnerChangeOptions");
      this.transferAttachments = var1;
      this.transferOpenActivities = var2;
   }

   public boolean isTransferAttachments() {
      return this.transferAttachments;
   }

   public boolean isTransferOpenActivities() {
      return this.transferOpenActivities;
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      var1.add(SoapHeader.Mapping.forBoolean("transferAttachments$", this.transferAttachments));
      var1.add(SoapHeader.Mapping.forBoolean("transferOpenActivities$", this.transferOpenActivities));
   }

   @Override
   public String getStringRepresentation() {
      String var1 = "";
      var1 = var1 + (this.transferAttachments ? "1" : "0");
      return var1 + (this.transferOpenActivities ? "1" : "0");
   }

   public static OwnerChangeOptionsHeader fromString(String var0) {
      return var0.length() != 2 ? new OwnerChangeOptionsHeader() : new OwnerChangeOptionsHeader(var0.charAt(0) == '1', var0.charAt(1) == '1');
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.transferAttachments, this.transferOpenActivities);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof OwnerChangeOptionsHeader)) {
         return false;
      }

      OwnerChangeOptionsHeader var2 = (OwnerChangeOptionsHeader)var1;
      return this.transferAttachments == var2.transferAttachments && this.transferOpenActivities == var2.transferOpenActivities;
   }

   @Override
   public String toString() {
      return String.format("transferOpenAttachements: %b, transferOpenActivities: %b", this.transferAttachments, this.transferOpenActivities);
   }
}
