package com.etlforge.integration.client.sfdc.data;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;
import org.etlforge.integration.salesforce.wsdl.partner.PartnerWsdlInfo;

public final class WebServiceCallFactory {
   private final SfdcMethod method;
   private final String suggestedName;
   private Folder parent;

   public WebServiceCallFactory(IntegrationProject var1, SfdcMethod var2) {
      this(var1, "SFDC " + StringUtils.capitalize(var2.getMethodName()), var2);
   }

   public WebServiceCallFactory(IntegrationProject var1, String var2, SfdcMethod var3) {
      this.parent = var1.getRootFolder(EntityType.WebServiceCall);
      this.suggestedName = var2;
      this.method = var3;
   }

   public void setParent(Folder var1) {
      this.parent = Preconditions.checkNotNull(var1);
   }

   public Folder getParent() {
      return this.parent;
   }

   public ImplementingObject<WebServiceCall> createWebServiceCallUnderParent() {
      String var1 = EntityUtils.getAvailableChildName(this.parent, this.suggestedName);
      WebServiceCall var2 = this.createWebServiceCallImpl(var1);
      return ImplementingObject.newObject(this.parent, var2);
   }

   public WebServiceCall createWebServiceCall() {
      return this.createWebServiceCallImpl(this.suggestedName);
   }

   private WebServiceCall createWebServiceCallImpl(String var1) {
      WebServiceCall var2 = new WebServiceCall(var1);
      WsdlFile var3 = new WsdlFile("etlforge.salesforce.partner.29.wsdl");
      var2.setWsdlFile(var3);
      String var4 = "[__Salesforce.ServiceURL]";
      var2.setUrl(var4);
      WebServiceOperationInfo var5 = this.createWsOpInfo(var3, var4);
      var2.setOperationInfo(var5);
      var2.setTargetNamespace("urn:partner.soap.sforce.com");
      String var6 = WebServiceInfo.getOtherNamespacesString(PartnerWsdlInfo.getOtherNamespaceMappings());
      var2.setOtherNamespaces(var6);
      var2.setTransient(true);
      var2.setPartOfSalesforceWizard(true);
      return var2;
   }

   private WebServiceOperationInfo createWsOpInfo(WsdlFile var1, String var2) {
      WebServiceOperationInfo var3 = new WebServiceOperationInfo();
      var3.setProperty("WebService_Url", var2);
      var3.setProperty("Service", "SforceService");
      this.setWebServiceOperation(var3);
      var3.setProperty("SoapStyle", "document");
      var3.setProperty("SoapBodyUse", "literal");
      var3.setProperty("SoapAction", "");
      var3.setProperty("Soap_Transport", "HTTP");
      var3.setProperty("Port", "Soap");
      var3.setProperty("SoapVersion", "1");
      var3.setProperty("Identifier", var1.getLocator());
      var3.setProperty("Binding", "SoapBinding");
      return var3;
   }

   private void setWebServiceOperation(WebServiceOperationInfo var1) {
      var1.setProperty("Operation", this.method.getMethodName());
      var1.setProperty("InputName", SalesforceWsdlConstants.getInputMessageName(this.method));
      var1.setProperty("OutputName", SalesforceWsdlConstants.getOutputMessageName(this.method));
   }
}
