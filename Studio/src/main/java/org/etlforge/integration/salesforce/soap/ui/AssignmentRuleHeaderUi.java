package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.AssignmentRuleHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;
import org.etlforge.ui.layout.NColumnPanel;

public final class AssignmentRuleHeaderUi extends StringValueHeaderUi implements FourColumnSoapHeaderUi {
   public AssignmentRuleHeaderUi(AssignmentRuleHeader var1) {
      super("Assignment rule ID", var1.getAssignmentRuleId());
   }

   @Override
   protected SoapHeader createHeader(String var1) {
      return new AssignmentRuleHeader(var1, null);
   }

   @Override
   public void layoutInFourColumns(NColumnPanel var1) {
      var1.add(this.getLabel());
      var1.add(this.getTextField(), 3);
   }

   public String getAssignmentRuleId() {
      AssignmentRuleHeader var1 = (AssignmentRuleHeader)this.getDefinedSoapHeader();
      return var1.getAssignmentRuleId();
   }
}
