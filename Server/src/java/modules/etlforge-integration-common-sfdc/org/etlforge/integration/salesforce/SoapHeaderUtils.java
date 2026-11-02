package org.etlforge.integration.salesforce;

import org.etlforge.xml.ExpandedName;

public final class SoapHeaderUtils {
   public static boolean isAssignmentRuleApplicable(SfdcMethod var0, ExpandedName var1) {
      return var1 == null ? false : isAssignmentRuleApplicable(var0, var1.getLocalName());
   }

   public static boolean isAssignmentRuleApplicable(SfdcMethod var0, String var1) {
      switch (var0) {
         case CREATE:
         case UPDATE:
         case UPSERT:
            return isAssignmentRuleApplicable(var1);
         default:
            return false;
      }
   }

   public static boolean isAssignmentRuleApplicable(String var0) {
      return "Case".equals(var0) || "Lead".equals(var0);
   }

   public static boolean isAssignmentRuleApplicable(ExpandedName var0) {
      return var0 == null ? false : isAssignmentRuleApplicable(var0.getLocalName());
   }

   private SoapHeaderUtils() {
   }
}
