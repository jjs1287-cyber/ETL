package org.etlforge.integration.data.entity.sfdc;

import org.apache.axis.utils.StringUtils;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.salesforce.ConnectionResult;
import org.etlforge.integration.salesforce.ErrorCode;
import org.etlforge.integration.salesforce.OrgIdentifier;

public abstract class OrgConnectionStatus {
   public static final OrgConnectionStatus MISSING = new OrgConnectionStatus.Missing();
   public static final OrgConnectionStatus UNVERIFIED = new OrgConnectionStatus.Unverified();
   public static final OrgConnectionStatus VERIFIED = new OrgConnectionStatus.Verified();

   public static OrgConnectionStatus fromConnectionResult(OrgIdentifier var0, ConnectionResult var1) {
      if (var0.isIncomplete()) {
         return MISSING;
      }

      if (var1.isSuccess()) {
         return VERIFIED;
      }

      ErrorCode var2 = var1.getErrorCode();
      switch (var2) {
         case INVALID_LOGIN:
         case API_DISABLED_FOR_ORG:
         case INSUFFICIENT_ACCESS:
         case LOGIN_MUST_USE_SECURITY_TOKEN:
         case UNSUPPORTED_API_VERSION:
            return new OrgConnectionStatus.Failed(var1.getMessage());
         default:
            return UNVERIFIED;
      }
   }

   public static OrgConnectionStatus decode(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return MISSING;
      } else {
         char var1 = var0.charAt(0);
         if (var1 == '1') {
            return VERIFIED;
         } else if (var1 == '2' && var0.length() > 2) {
            String var2 = var0.substring(2);
            return new OrgConnectionStatus.Failed(var2);
         } else {
            return UNVERIFIED;
         }
      }
   }

   public abstract void reportTo(ValidationMessageCollector var1);

   public abstract String encode();

   private static class Failed extends OrgConnectionStatus {
      private final String errorMessage;

      public Failed(String var1) {
         this.errorMessage = var1;
      }

      @Override
      public void reportTo(ValidationMessageCollector var1) {
         var1.addMessage(this.errorMessage, ValidationStatus.INVALID);
      }

      @Override
      public String encode() {
         return "2|" + this.errorMessage;
      }

      @Override
      public int hashCode() {
         return this.errorMessage.hashCode();
      }

      @Override
      public boolean equals(Object var1) {
         return var1 instanceof OrgConnectionStatus.Failed && this.errorMessage.equals(((OrgConnectionStatus.Failed)var1).errorMessage);
      }
   }

   private static class Missing extends OrgConnectionStatus {
      private Missing() {
      }

      @Override
      public void reportTo(ValidationMessageCollector var1) {
      }

      @Override
      public String encode() {
         return "";
      }
   }

   private static class Unverified extends OrgConnectionStatus {
      private Unverified() {
      }

      @Override
      public void reportTo(ValidationMessageCollector var1) {
         var1.addMessage("This Salesforce Org has not been verified with a connection test.", ValidationStatus.WARNING);
      }

      @Override
      public String encode() {
         return "0";
      }
   }

   private static class Verified extends OrgConnectionStatus {
      private Verified() {
      }

      @Override
      public void reportTo(ValidationMessageCollector var1) {
      }

      @Override
      public String encode() {
         return "1";
      }
   }
}
