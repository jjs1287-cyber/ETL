package com.etlforge.integration.client.sfdc.data;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class ExistingSfdcWsCallFilter implements EntityFilter {
   private final String expectedUrl = "[__Salesforce.ServiceURL]";
   private final String operationName;
   private String wsdlLocator;

   public ExistingSfdcWsCallFilter(SfdcMethod var1) {
      this(var1.getMethodName());
   }

   public ExistingSfdcWsCallFilter(String var1) {
      this.operationName = var1;
   }

   public void setWsdlLocator(String var1) {
      this.wsdlLocator = var1;
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return var1 instanceof WebServiceCall ? this.isProbablySfdcCall((WebServiceCall)var1) : false;
   }

   private boolean isProbablySfdcCall(WebServiceCall var1) {
      return !var1.isPartOfSalesforceWizard() && !var1.isPartOfSalesforcePartnerWizard()
         ? false
         : this.isCallingTheCorrectMethod(var1) && this.isUsingTheCorrectWsdl(var1) && this.isUsingTheCorrectVariableAsUrl(var1);
   }

   private boolean isCallingTheCorrectMethod(WebServiceCall var1) {
      String var2 = var1.getOperationName();
      return this.operationName.equals(var2);
   }

   private boolean isUsingTheCorrectWsdl(WebServiceCall var1) {
      return this.wsdlLocator == null || this.wsdlLocator.equals(var1.getWsdlFile().getLocator());
   }

   private boolean isUsingTheCorrectVariableAsUrl(WebServiceCall var1) {
      return var1.getUrl().equalsIgnoreCase("[__Salesforce.ServiceURL]");
   }
}
