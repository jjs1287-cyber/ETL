package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QuerySyntaxValidator;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QueryValidator;
import com.etlforge.integration.salesforce.wsdl.SalesforceWsdlInfo;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.xml.ExpandedName;

public class WsdlBasedQueryValidator implements QueryValidator {
   private final QuerySyntaxValidator syntaxValidator;
   private final SalesforceWsdlInfo wsdlInfo;
   private ExpandedName fixedObject;

   public WsdlBasedQueryValidator(SalesforceWsdlInfo var1) {
      Preconditions.checkNotNull(var1, "wsdlInfo");
      this.wsdlInfo = var1;
      this.syntaxValidator = new QuerySyntaxValidator();
   }

   @Override
   public String validate(String var1) {
      String var2 = this.syntaxValidator.validate(var1);
      if (var2 != null) {
         return var2;
      } else {
         String var3 = this.syntaxValidator.getObjectName();
         assert var3 != null : "There should have been a syntax error detected";
         if (this.wsdlInfo.findObjectName(var3) == null) {
            return "The object \"" + var3 + "\" is not defined in your salesforce WSDL.";
         } else {
            return this.fixedObject != null && !this.fixedObject.getLocalName().equalsIgnoreCase(var3)
               ? "Changing the queried object (" + this.fixedObject.getLocalName() + " -> " + var3 + ") is not supported in the current version."
               : null;
         }
      }
   }

   public void setFixedObjectName(ExpandedName var1) {
      this.fixedObject = var1;
   }

   public SObject getFixedObject() {
      return this.fixedObject == null ? null : this.wsdlInfo.lookupObject(this.fixedObject);
   }
}
