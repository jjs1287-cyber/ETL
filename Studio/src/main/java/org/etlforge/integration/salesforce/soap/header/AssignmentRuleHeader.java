package org.etlforge.integration.salesforce.soap.header;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.lang.Objects;

public final class AssignmentRuleHeader extends SoapHeader {
   private final String assignmentRuleId;
   private final Boolean useDefaultRule;

   public AssignmentRuleHeader() {
      this(null, null);
   }

   public AssignmentRuleHeader(String var1, Boolean var2) {
      super("AssignmentRuleHeader");
      this.assignmentRuleId = var1 == null ? "" : var1;
      this.useDefaultRule = var2;
   }

   public String getAssignmentRuleId() {
      return this.assignmentRuleId;
   }

   public Boolean isUseDefaultRule() {
      return this.useDefaultRule;
   }

   @Override
   public String getStringRepresentation() {
      String var1 = this.useDefaultRule == null ? "" : (this.useDefaultRule ? "1" : "0");
      return var1 + "|" + this.assignmentRuleId;
   }

   public static AssignmentRuleHeader fromString(String var0) {
      int var1 = var0.indexOf(124);
      if (var1 >= 0) {
         try {
            String var2 = var0.substring(0, var1);
            String var3 = var0.substring(var1 + 1);
            return new AssignmentRuleHeader(var3, var2.isEmpty() ? null : "1".equals(var2));
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return new AssignmentRuleHeader();
   }

   @Override
   protected void collectMappings(List<SoapHeader.Mapping> var1) {
      if (StringUtils.isNotEmpty(this.assignmentRuleId)) {
         var1.add(SoapHeader.Mapping.forString("assignmentRuleId$", this.assignmentRuleId));
      }

      if (this.useDefaultRule != null) {
         var1.add(SoapHeader.Mapping.forBoolean("useDefaultRule$", this.useDefaultRule));
      }
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.assignmentRuleId, this.useDefaultRule);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof AssignmentRuleHeader)) {
         return false;
      }

      AssignmentRuleHeader var2 = (AssignmentRuleHeader)var1;
      return Objects.equal(this.useDefaultRule, var2.useDefaultRule) && Objects.equal(this.assignmentRuleId, var2.assignmentRuleId);
   }

   @Override
   public String toString() {
      return String.format("Rule ID: [%s], Use Default: [%s]", this.assignmentRuleId, this.useDefaultRule);
   }
}
