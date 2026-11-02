package org.etlforge.integration.data.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.CallRestrictions;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.entity.id.WebServiceCallId;
import org.etlforge.integration.data.entity.webservicecall.WebServiceCallValidator;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.http.HttpConnector;
import org.etlforge.integration.http.HttpProperties;
import org.etlforge.integration.http.HttpPropertiesSupport;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.http.SslVersion;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.id.KongaID;
import org.etlforge.util.persist.Persistor;

public final class WebServiceCall extends IntegrationEntity implements HttpConnector, Function {
   @IdProperty
   public static final String WEB_SERVICE_ID = "webservice_id";
   public static final String FUNCTION_ID = "function_id";
   @NameProperty
   public static final String WEB_SERVICE_NAME = "webservice_name";
   public static final String WEB_SERVICE_URL = "webservice_url";
   public static final String WSDL_LOCATOR = "wsdl_id";
   public static final String SERVICE = "service";
   public static final String PORT = "port";
   public static final String BINDING = "binding";
   public static final String OPERATION = "operation";
   public static final String OPERATION_INPUT = "operation_input";
   public static final String OPERATION_OUTPUT = "operation_output";
   public static final String SOAP_VERSION = "soapversion";
   public static final String SOAP_ACTION = "soapaction";
   public static final String SOAP_STYLE = "soapstyle";
   public static final String SOAP_BODY_USE = "soapbodyuse";
   public static final String INPUT_XSD_FILENAME = "inputxsdfilename";
   public static final String OUTPUT_XSD_FILENAME = "outputxsdfilename";
   public static final String HTTP_USER_NAME = "httpusername";
   public static final String HTTP_PASSWORD = "httppassword";
   public static final String CERTIFICATE = "certificate";
   public static final String TARGET_NAMESPACE = "targetnamespace";
   public static final String OTHER_NAMESPACES = "othernamespaces";
   public static final String USED_IN_SFDC_PARTNER_WIZARD = "sfdc_partner_wsdl";
   private static final String[] PROPERTIES = new String[]{
      "webservice_id",
      "wsdl_id",
      "webservice_name",
      "webservice_url",
      "service",
      "port",
      "binding",
      "operation",
      "soapversion",
      "soapaction",
      "soapstyle",
      "soapbodyuse",
      "inputxsdfilename",
      "outputxsdfilename",
      "httpusername",
      "httppassword",
      "certificate",
      "targetnamespace",
      "othernamespaces",
      "enable_content_encoding",
      "enable_chunked_transfer_encoding",
      "send_expect_100_continue",
      "http_request_headers",
      "http_ssl_version",
      "operation_input",
      "operation_output",
      "salesforce_wizard_object",
      "sfdc_partner_wsdl"
   };
   public static final String CALL_RESTRICTION = "WebService#CallRestriction";
   public static final String WSDL_FILE = "WebService#WsdlFile";
   public static final String OPERATION_INFO = "WebService#OperationInfo";
   public static final String PROPERTY_HTTP_PROPERTIES = "httpProperties";
   private CallRestrictions callRestriction;
   private WsdlFile wsdlFile;

   @Override
   public String[] getPropertyKeys() {
      return PROPERTIES;
   }

   public WebServiceCall(String var1) {
      this(null, var1);
   }

   public WebServiceCall(WebServiceCallId var1, String var2) {
      super(EntityType.WebServiceCall, var1, var2);
   }

   public WebServiceCallId getID() {
      return (WebServiceCallId)super.getID();
   }

   @Override
   protected void setID(KongaID var1) {
      super.setID(var1);
      if (this.callRestriction != null) {
         this.callRestriction.setProperty("function_id", var1.toString());
      }
   }

   public CallRestrictions getCallRestrictions() {
      return this.callRestriction == null ? new CallRestrictions(this) : this.callRestriction.getCopy();
   }

   public void setCallRestrictions(CallRestrictions var1) {
      if (var1 == null && this.callRestriction != null || var1 != null && !var1.equals(this.callRestriction)) {
         CallRestrictions var2 = this.callRestriction;
         this.callRestriction = var1 == null ? null : var1.getCopy();
         this.firePropertyChange("WebService#CallRestriction", var2, var1);
      }
   }

   public WsdlFile getWsdlFile() {
      return this.wsdlFile == null ? new WsdlFile() : this.wsdlFile.getCopy();
   }

   public void setWsdlFile(WsdlFile var1) {
      this.setProperty("wsdl_id", var1.getLocator());
      if (!var1.equals(this.wsdlFile)) {
         WsdlFile var2 = this.wsdlFile;
         this.wsdlFile = var1.getCopy();
         this.firePropertyChange("WebService#WsdlFile", var2, var1);
      }
   }

   @Override
   public IntegrationEntityId getFunctionID() {
      return this.getID();
   }

   public WebServiceOperationInfo getOperationInfo() {
      synchronized (this.getDataLock()) {
         WebServiceOperationInfo var2 = new WebServiceOperationInfo();
         var2.setProperty("Identifier", this.getWsdlFile().getLocator());
         var2.setProperty("Operation", this.getProperty("operation"));
         var2.setProperty("InputName", this.getProperty("operation_input"));
         var2.setProperty("OutputName", this.getProperty("operation_output"));
         var2.setProperty("Service", this.getProperty("service"));
         var2.setProperty("Port", this.getProperty("port"));
         var2.setProperty("Binding", this.getProperty("binding"));
         var2.setProperty("SoapVersion", this.getProperty("soapversion"));
         var2.setProperty("SoapAction", this.getProperty("soapaction"));
         var2.setProperty("SoapStyle", this.getProperty("soapstyle"));
         var2.setProperty("SoapBodyUse", this.getProperty("soapbodyuse"));
         return var2;
      }
   }

   public void setOperationInfo(WebServiceOperationInfo var1) {
      WebServiceOperationInfo var2;
      synchronized (this.getDataLock()) {
         var2 = this.getOperationInfo();
         this.setProperty("operation", var1.getProperty("Operation"));
         this.setProperty("operation_input", var1.getProperty("InputName"));
         this.setProperty("operation_output", var1.getProperty("OutputName"));
         this.setProperty("service", var1.getProperty("Service"));
         this.setProperty("port", var1.getProperty("Port"));
         this.setProperty("binding", var1.getProperty("Binding"));
         this.setProperty("soapversion", var1.getProperty("SoapVersion"));
         this.setProperty("soapaction", var1.getProperty("SoapAction"));
         this.setProperty("soapstyle", var1.getProperty("SoapStyle"));
         this.setProperty("soapbodyuse", var1.getProperty("SoapBodyUse"));
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("WebService#OperationInfo", var2, var1);
      }
   }

   public String getOperationName() {
      return this.getProperty("operation");
   }

   public String getServiceName() {
      return this.getProperty("service");
   }

   public WebServiceRequestStructure getInputStructure() {
      WebServiceRequestStructure var1 = new WebServiceRequestStructure(this.getID());
      var1.setOpInfo(this.getOperationInfo());
      return var1;
   }

   public WebServiceResponseStructure getOutputStructure() {
      WebServiceResponseStructure var1 = new WebServiceResponseStructure(this.getID());
      var1.setOpInfo(this.getOperationInfo());
      return var1;
   }

   @Override
   public boolean acceptsOutputFrom(Function var1) {
      if (var1 == null) {
         return false;
      } else {
         return var1 != SystemTransformation.SALESFORCE_QUERY_REQUEST
            ? this.getInputStructure().equals(var1.getOutputStructure())
            : this.isPartOfSalesforcePartnerWizard() && SfdcMethod.QUERY.getMethodName().equals(this.getOperationName());
      }
   }

   @Override
   public boolean hasRequestTransformation() {
      return this.getBooleanProperty("has_request", true);
   }

   @VisibleForTesting
   public void setHasRequestTransformation(boolean var1) {
      this.setBooleanProperty("has_request", var1);
   }

   @Override
   protected String getIdKey() {
      return "webservice_id";
   }

   @Override
   protected String getNameKey() {
      return "webservice_name";
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      StringKeyValuePairs var1 = new StringKeyValuePairs();
      var1.putAll(super.getCopyOfKeyValues());
      var1.put("webservice_id", this.getID().toString());
      var1.put("wsdl_id", this.getWsdlFile().getLocator());
      var1.put("webservice_name", this.getName());
      return var1;
   }

   public static final String[] getFunctionPropertyKeys() {
      return new String[]{"function_id", "functiontype_id", "foreignkeyfunction_id"};
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      new WebServiceCallValidator(this, var1).run();
   }

   public WebServiceCall clone() {
      WebServiceCall var1 = (WebServiceCall)super.clone();
      if (this.callRestriction != null) {
         var1.callRestriction = this.callRestriction.getCopy();
      }

      if (this.wsdlFile != null) {
         var1.wsdlFile = this.wsdlFile.getCopy();
      }

      return var1;
   }

   @Override
   public void setUrl(String var1) {
      this.setStringProperty("webservice_url", var1);
   }

   @Override
   public String getUrl() {
      return this.getProperty("webservice_url", "");
   }

   @Override
   public HttpProperties getHttpProperties() {
      synchronized (this.getDataLock()) {
         return new HttpPropertiesSupport(this.getData()).createProperties();
      }
   }

   @Override
   public void setHttpProperties(HttpProperties var1) {
      HttpProperties var2;
      synchronized (this.getDataLock()) {
         var2 = this.getHttpProperties();
         if (!var1.equals(this.getHttpProperties())) {
            new HttpPropertiesSupport(this.getData()).applyProperties(var1);
         }
      }

      this.firePropertyChange("httpProperties", var2, var1);
   }

   @Override
   public HttpVerb getHttpVerb() {
      return HttpVerb.POST;
   }

   @Override
   public void setHttpVerb(HttpVerb var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public SslVersion getSslVersion() {
      SslVersion var1 = SslVersion.defaultVersion();
      int var2 = this.getIntProperty("http_ssl_version", var1.getId());
      return EnumWithIdLookup.lookupOr(var2, var1);
   }

   @Override
   public void setSslVersion(SslVersion var1) {
      this.setIntProperty("http_ssl_version", var1.getId());
   }

   public void setSoapAction(String var1) {
      this.setStringProperty("soapaction", var1);
   }

   public String getHttpUserName() {
      synchronized (this.getDataLock()) {
         return this.getProperty("httpusername");
      }
   }

   public void setHttpUserName(String var1) {
      this.setStringProperty("httpusername", var1);
   }

   public Optional<Password> getHttpPassword() {
      return PasswordUtils.getPassword(this, "httppassword");
   }

   public void setHttpPassword(Optional<Password> var1) {
      PasswordUtils.setPassword(this, "httppassword", var1);
   }

   public void setCertificateName(String var1) {
      this.setStringProperty("certificate", var1);
   }

   public void setTargetNamespace(String var1) {
      this.setStringProperty("targetnamespace", var1);
   }

   public void setOtherNamespaces(String var1) {
      this.setStringProperty("othernamespaces", var1);
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      this.writeCallRestriction(var1);
      this.writeWsdlFile(var1);
   }

   private void writeCallRestriction(Persistor var1) {
      if (this.callRestriction != null) {
         this.callRestriction.writeTo(var1.createChild("CallRestrictions"));
      }
   }

   private void writeWsdlFile(Persistor var1) {
      this.getWsdlFile().writeTo(var1.createChild("WsdlFile"));
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      this.restoreWsdlFile(var1);
      this.restoreCallRestrictions(var1);
   }

   private void restoreWsdlFile(Persistor var1) {
      this.wsdlFile = WsdlFile.fromPersistor(var1.getFirstChild("WsdlFile"));
   }

   private void restoreCallRestrictions(Persistor var1) {
      Persistor var2 = var1.getFirstChild("CallRestrictions");
      if (var2 != null) {
         this.callRestriction = new CallRestrictions();
         this.callRestriction.restoreFrom(var2);
      }
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      WebServiceCall var2 = (WebServiceCall)var1;
      this.callRestriction = var2.callRestriction != null ? var2.callRestriction.getCopy() : null;
      this.wsdlFile = var2.wsdlFile != null ? var2.wsdlFile.getCopy() : null;
   }

   @Override
   public String getRequestHeaders() {
      return this.getProperty("http_request_headers");
   }

   @Override
   public void setRequestHeaders(String var1) {
      this.setStringProperty("http_request_headers", var1);
   }

   public boolean isPartOfSalesforceWizard() {
      return SalesforceUtils.isSalesforceWizardObject(this);
   }

   public void setPartOfSalesforceWizard(boolean var1) {
      SalesforceUtils.setSalesforceWizardObject(this, var1);
      this.setBooleanProperty("sfdc_partner_wsdl", true);
   }

   public boolean isPartOfSalesforcePartnerWizard() {
      synchronized (this.getDataLock()) {
         return this.getBooleanProperty("sfdc_partner_wsdl", false);
      }
   }
}
