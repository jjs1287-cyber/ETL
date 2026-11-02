package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class AssignmentRuleCromHeader extends CromHeaderElement {
   public AssignmentRuleCromHeader() {
      super("AssignmentRuleHeader", CromHeaderValue.salesforceId("assignmentRuleId").optional(), CromHeaderValue.bool("useDefaultRule").optional());
   }
}
