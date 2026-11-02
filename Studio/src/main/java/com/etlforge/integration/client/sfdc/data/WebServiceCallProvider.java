package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Optional;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class WebServiceCallProvider {
   private final IntegrationProject project;
   private final SfdcMethod method;

   public WebServiceCallProvider(IntegrationProject var1, SfdcMethod var2) {
      this.project = var1;
      this.method = var2;
   }

   public WebServiceCall getWebServiceCall() {
      Optional var1 = this.findExistingWsCall();
      return var1.isPresent() ? (WebServiceCall)var1.get() : this.createNewWsCall();
   }

   private Optional<WebServiceCall> findExistingWsCall() {
      ExistingSfdcWsCallFilter var1 = new ExistingSfdcWsCallFilter(this.method);
      var1.setWsdlLocator("etlforge.salesforce.partner.29.wsdl");
      return this.project.getAllEntitiesOfClass(WebServiceCall.class).firstMatch(var1);
   }

   private WebServiceCall createNewWsCall() {
      WebServiceCallFactory var1 = new WebServiceCallFactory(this.project, this.method);
      return var1.createWebServiceCall();
   }
}
