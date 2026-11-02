package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.etlforge.integration.client.sfdc.data.SalesforceModifierResponseMapper;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;

final class ResponseMapper extends SalesforceModifierResponseMapper {
   private final String objectName;
   private String externalIdName;

   public ResponseMapper(SfdcMethod var1, String var2) {
      super(var1);
      this.objectName = var2;
   }

   public void setExternalIdName(String var1) {
      this.externalIdName = var1;
   }

   @Override
   protected void appendErrorMessage(StringBuilder var1) {
      if (this.externalIdName != null) {
         String var2 = this.getPathToExternalFieldNode(this.objectName);
         if (var2 == null) {
            var1.append("\"The issue was: \" +\n\t");
         } else {
            var1.append("\"The following record failed to process. ")
               .append(this.objectName)
               .append(" \" +\n\tFindByPos(SourceInstanceCount(), ")
               .append(var2)
               .append(") +\n\t\" The issue was: \" +\n\t");
         }
      }
   }

   private String getPathToExternalFieldNode(String var1) {
      return SalesforceWsdlConstants.getPathToExternalIdFieldInResponse(var1, this.externalIdName);
   }
}
