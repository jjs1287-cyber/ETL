package org.etlforge.integration.client.wsdl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.wsdl.extensions.soap12.SOAP12Binding;
import javax.wsdl.extensions.soap12.SOAP12Body;
import javax.wsdl.extensions.soap12.SOAP12Operation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.integration.data.entity.webservicecall.WebServiceInfo;
import org.etlforge.integration.data.entity.webservicecall.WebServiceOperationIdentifier;
import org.etlforge.integration.data.structure.WebServiceOperationInfo;
import org.w3c.dom.Element;

public final class WebServiceInfoBuilder {
   public WebServiceInfo build(File var1) throws WsdlParserException {
      WebServiceInfoBuilder.DefinitionInfo var2 = this.buildFromUri(var1.toURI().toString());
      return this.createWebServiceInfo(var2, var1.getAbsolutePath());
   }

   public WebServiceInfo build(URL var1) throws WsdlParserException {
      WebServiceInfoBuilder.DefinitionInfo var2 = this.buildFromUri(var1.toString());
      return this.createWebServiceInfo(var2, var1.toString());
   }

   public WebServiceInfo build(byte[] var1) throws WsdlParserException {
      try {
         File var2 = createTemporaryWsdlFile(var1);
         return this.build(var2);
      } catch (IOException var3) {
         throw new WsdlParserException("Failed to create temporary WSDL file on the local disk.", var3);
      }
   }

   private static File createTemporaryWsdlFile(byte[] var0) throws IOException {
      File var1 = ApplicationTempFolder.newFolder("wsdlparser");
      File var2 = File.createTempFile("tmp", "wsdl.xml", var1);
      Files.write(var0, var2);
      var2.deleteOnExit();
      return var2;
   }

   private WebServiceInfoBuilder.DefinitionInfo buildFromUri(String var1) throws WsdlParserException {
      try {
         WSDLFactory var2 = WSDLFactory.newInstance();
         WSDLReader var3 = var2.newWSDLReader();
         var3.setFeature("javax.wsdl.verbose", false);
         var3.setFeature("javax.wsdl.importDocuments", false);
         Definition var4 = var3.readWSDL(var1);
         WebServiceInfoBuilder.DefinitionParser var5 = new WebServiceInfoBuilder.DefinitionParser(var4);
         return var5.parse();
      } catch (WSDLException var6) {
         throw new WsdlParserException("Failed to parse the WSDL file.", var6);
      }
   }

   private WebServiceInfo createWebServiceInfo(WebServiceInfoBuilder.DefinitionInfo var1, String var2) {
      WebServiceInfo var3 = new WebServiceInfo("identifier", null, var2);
      var1.applyTo(var3);
      return var3;
   }

   private static class BindingInfo {
      public final List<WebServiceOperationInfo> operations;

      public BindingInfo(List<WebServiceOperationInfo> var1) {
         this.operations = var1;
      }
   }

   private static class BindingOperationParser {
      private final BindingOperation operation;
      private final List<WebServiceOperationIdentifier> operationIdentifiers;
      private WebServiceOperationInfo opInfo;

      public BindingOperationParser(BindingOperation var1, List<WebServiceOperationIdentifier> var2) {
         this.operation = var1;
         this.operationIdentifiers = var2;
      }

      public WebServiceOperationInfo parse() {
         this.opInfo = new WebServiceOperationInfo();
         this.opInfo.setProperty("Operation", this.operation.getName());
         if (this.parseSoapOperation()) {
            this.parseInput();
            this.parseOutput();
            this.parseDocumentation();
         } else {
            this.opInfo = null;
         }

         return this.opInfo;
      }

      private boolean parseSoapOperation() {
         for (Object var2 : this.operation.getExtensibilityElements()) {
            if (var2 instanceof SOAPOperation) {
               this.parseSoapOperation((SOAPOperation)var2);
               return true;
            }

            if (var2 instanceof SOAP12Operation) {
               this.parseSoap12Operation((SOAP12Operation)var2);
               return true;
            }

            if (var2 instanceof UnknownExtensibilityElement) {
               Element var3 = ((UnknownExtensibilityElement)var2).getElement();
               if (isSoap12Operation(var3)) {
                  this.parseSoap12Operation(var3);
                  return true;
               }
            }
         }

         return false;
      }

      private void parseSoapOperation(SOAPOperation var1) {
         this.opInfo.setProperty("SoapStyle", var1.getStyle());
         this.opInfo.setProperty("SoapAction", var1.getSoapActionURI());
      }

      private void parseSoap12Operation(SOAP12Operation var1) {
         this.opInfo.setProperty("SoapStyle", var1.getStyle());
         this.opInfo.setProperty("SoapAction", var1.getSoapActionURI());
      }

      private void parseSoap12Operation(Element var1) {
         String var2 = var1.getAttribute("style");
         if (var2 == null || var2.length() == 0) {
            var2 = "document";
         }

         String var3 = var1.getAttribute("soapAction");
         this.opInfo.setProperty("SoapStyle", var2);
         this.opInfo.setProperty("SoapAction", var3);
      }

      private void parseInput() {
         BindingInput var1 = this.operation.getBindingInput();
         String var2 = var1 != null ? var1.getName() : null;
         if (var2 == null) {
            if (this.operationIdentifiers.size() > 1) {
               System.err.println("Ambiguous operation/input/output.");
            }

            var2 = this.operationIdentifiers.get(0).getInputName();
         }

         this.opInfo.setProperty("InputName", var2);
         if (var1 != null) {
            this.extractSoapBodyUse(var1);
         }
      }

      private void parseOutput() {
         BindingOutput var1 = this.operation.getBindingOutput();
         String var2 = var1 != null ? var1.getName() : null;
         if (var2 == null) {
            if (this.operationIdentifiers.size() > 1) {
               System.err.println("Ambiguous operation/input/output.");
            }

            var2 = this.operationIdentifiers.get(0).getOutputName();
         }

         this.opInfo.setProperty("OutputName", var2);
         if (var1 != null && this.opInfo.getProperty("SoapBodyUse") == null) {
            this.extractSoapBodyUse(var1);
         }
      }

      private void parseDocumentation() {
         Element var1 = this.operation.getDocumentationElement();
         if (var1 != null) {
            this.opInfo.setProperty("Documentation", var1.getTextContent());
         }
      }

      private void extractSoapBodyUse(ElementExtensible var1) {
         for (Object var3 : var1.getExtensibilityElements()) {
            if (var3 instanceof SOAPBody) {
               this.extractSoapBodyUse((SOAPBody)var3);
            } else if (var3 instanceof SOAP12Body) {
               this.extractSoap12BodyUse((SOAP12Body)var3);
            } else if (var3 instanceof UnknownExtensibilityElement) {
               this.parseUnknownExtensibilityElement((UnknownExtensibilityElement)var3);
            }
         }
      }

      private void parseUnknownExtensibilityElement(UnknownExtensibilityElement var1) {
         Element var2 = var1.getElement();
         if (isSoap12Body(var2)) {
            this.parseSoap12Body(var2);
         }
      }

      private void extractSoapBodyUse(SOAPBody var1) {
         this.opInfo.setProperty("SoapBodyUse", var1.getUse());
      }

      private void extractSoap12BodyUse(SOAP12Body var1) {
         this.opInfo.setProperty("SoapBodyUse", var1.getUse());
      }

      private void parseSoap12Body(Element var1) {
         this.opInfo.setProperty("SoapBodyUse", var1.getAttribute("use"));
      }

      private static boolean isSoap12Operation(Element var0) {
         return "operation".equals(var0.getLocalName()) && "http://schemas.xmlsoap.org/wsdl/soap12/".equals(var0.getNamespaceURI());
      }

      private static boolean isSoap12Body(Element var0) {
         return "body".equals(var0.getLocalName()) && "http://schemas.xmlsoap.org/wsdl/soap12/".equals(var0.getNamespaceURI());
      }
   }

   private static class BindingParser {
      private final Binding binding;
      private final String bindingName;
      private final WebServiceInfoBuilder.OperationIdentifiers operationIdentifiers;
      private WebServiceInfoBuilder.SoapBindingInfo soapBindingInfo;

      public BindingParser(Binding var1, WebServiceInfoBuilder.OperationIdentifiers var2) {
         this.binding = var1;
         this.bindingName = var1.getQName().getLocalPart();
         this.operationIdentifiers = var2;
      }

      public List<WebServiceOperationInfo> parse() {
         this.soapBindingInfo = this.parseSoapBindingInfo();
         return this.soapBindingInfo != null ? this.parseOperations() : null;
      }

      private WebServiceInfoBuilder.SoapBindingInfo parseSoapBindingInfo() {
         for (Object var2 : this.binding.getExtensibilityElements()) {
            if (var2 instanceof SOAPBinding) {
               return WebServiceInfoBuilder.SoapBindingInfo.fromSoap11Binding((SOAPBinding)var2);
            }

            if (var2 instanceof SOAP12Binding) {
               return WebServiceInfoBuilder.SoapBindingInfo.fromSoap12Binding((SOAP12Binding)var2);
            }

            if (var2 instanceof UnknownExtensibilityElement) {
               Element var3 = ((UnknownExtensibilityElement)var2).getElement();
               if (isSoap12Binding(var3)) {
                  return WebServiceInfoBuilder.SoapBindingInfo.fromDomElement(var3);
               }
            }
         }

         return null;
      }

      private static boolean isSoap12Binding(Element var0) {
         return "binding".equals(var0.getLocalName()) && "http://schemas.xmlsoap.org/wsdl/soap12/".equals(var0.getNamespaceURI());
      }

      private List<WebServiceOperationInfo> parseOperations() {
         ArrayList var1 = new ArrayList();

         for (Object var3 : this.binding.getBindingOperations()) {
            WebServiceInfoBuilder.BindingOperationParser var4 = this.createOperationParser((BindingOperation)var3);
            if (var4 != null) {
               WebServiceOperationInfo var5 = var4.parse();
               if (var5 != null) {
                  this.setAdditionalProperties(var5);
                  var1.add(var5);
               }
            }
         }

         return var1;
      }

      private WebServiceInfoBuilder.BindingOperationParser createOperationParser(BindingOperation var1) {
         List var2 = this.operationIdentifiers.get(var1.getName());
         return var2 == null ? null : new WebServiceInfoBuilder.BindingOperationParser(var1, var2);
      }

      private void setAdditionalProperties(WebServiceOperationInfo var1) {
         var1.setProperty("Binding", this.bindingName);
         var1.setProperty("Transport", "SOAP");
         this.soapBindingInfo.applyTo(var1);
      }
   }

   private static class DefinitionInfo {
      private final Definition definition;
      private final List<WebServiceInfoBuilder.BindingInfo> bindingInfo;
      private final String targetNamespace;
      private final List<String> otherNamespaces;

      public DefinitionInfo(Definition var1, String var2, List<String> var3) {
         this.definition = var1;
         this.targetNamespace = var2;
         this.otherNamespaces = var3;
         this.bindingInfo = new ArrayList<>();
      }

      public void add(List<WebServiceInfoBuilder.BindingInfo> var1) {
         this.bindingInfo.addAll(var1);
      }

      public void applyTo(WebServiceInfo var1) {
         var1.setWsdlDefinition(this.definition);
         var1.setTargetNamespace(this.targetNamespace);
         var1.setOtherNamespaces(this.otherNamespaces);
         ArrayList var2 = new ArrayList();

         for (WebServiceInfoBuilder.BindingInfo var4 : this.bindingInfo) {
            var2.addAll(var4.operations);
         }

         var1.setOperations((WebServiceOperationInfo[]) var2.toArray(new WebServiceOperationInfo[var2.size()]));
      }
   }

   private static class DefinitionParser {
      private final Definition definition;
      private String targetNamespace;
      private List<String> otherNamespaces;
      private WebServiceInfoBuilder.PortTypeInfo portTypes;

      public DefinitionParser(Definition var1) {
         this.definition = var1;
      }

      public WebServiceInfoBuilder.DefinitionInfo parse() throws WsdlParserException {
         this.extractNamespaces();
         this.parsePortTypes();
         return this.parseServices();
      }

      private void extractNamespaces() {
         this.extractTargetNamespace();
         this.extractOtherNamespaces();
      }

      private void extractTargetNamespace() {
         this.targetNamespace = this.definition.getTargetNamespace();
      }

      private void extractOtherNamespaces() {
         this.otherNamespaces = new ArrayList<>();
         Map var1 = this.definition.getNamespaces();

         for (Object var3 : var1.entrySet()) {
            Entry var4 = (Entry)var3;
            String var5 = (String)var4.getKey();
            StringBuilder var6 = new StringBuilder("xmlns");
            if (var5.length() > 0) {
               var6.append(":").append(var5);
            }

            var6.append("=").append(var4.getValue());
            this.otherNamespaces.add(var6.toString());
         }
      }

      private void parsePortTypes() {
         this.portTypes = new WebServiceInfoBuilder.PortTypeInfo();

         for (Object var2 : this.definition.getPortTypes().values()) {
            PortType var3 = (PortType)var2;
            WebServiceInfoBuilder.OperationIdentifiers var4 = this.extractOperations(var3);
            this.portTypes.add(var3, var4);
         }
      }

      private WebServiceInfoBuilder.OperationIdentifiers extractOperations(PortType var1) {
         WebServiceInfoBuilder.OperationIdentifiers var2 = new WebServiceInfoBuilder.OperationIdentifiers();

         for (Object var4 : var1.getOperations()) {
            Operation var5 = (Operation)var4;
            WebServiceOperationIdentifier var6 = WebServiceOperationIdentifier.fromOperation(var5);
            var2.add(var6);
         }

         return var2;
      }

      private WebServiceInfoBuilder.DefinitionInfo parseServices() throws WsdlParserException {
         WebServiceInfoBuilder.DefinitionInfo var1 = new WebServiceInfoBuilder.DefinitionInfo(this.definition, this.targetNamespace, this.otherNamespaces);

         for (Object var3 : this.definition.getServices().values()) {
            List var4 = this.parseService((Service)var3);
            var1.add(var4);
         }

         return var1;
      }

      private List<WebServiceInfoBuilder.BindingInfo> parseService(Service var1) throws WsdlParserException {
         WebServiceInfoBuilder.ServiceParser var2 = new WebServiceInfoBuilder.ServiceParser(var1, this.portTypes);
         return var2.parse();
      }
   }

   private static class OperationIdentifiers {
      private final ListMultimap<String, WebServiceOperationIdentifier> map = ArrayListMultimap.create();

      public OperationIdentifiers() {
      }

      public void add(WebServiceOperationIdentifier var1) {
         this.map.put(var1.getOperationName(), var1);
      }

      public List<WebServiceOperationIdentifier> get(String var1) {
         return this.map.get(var1);
      }
   }

   private static class PortParser {
      private final Port port;
      private final WebServiceInfoBuilder.PortTypeInfo portTypes;
      private String soapAddressUri;

      public PortParser(Port var1, WebServiceInfoBuilder.PortTypeInfo var2) {
         this.port = var1;
         this.portTypes = var2;
      }

      public WebServiceInfoBuilder.BindingInfo parse() throws WsdlParserException {
         this.parseExtensibilityElements();
         return this.soapAddressUri != null ? this.parseBinding() : null;
      }

      private void parseExtensibilityElements() {
         for (Object var2 : this.port.getExtensibilityElements()) {
            if (var2 instanceof SOAPAddress) {
               this.soapAddressUri = ((SOAPAddress)var2).getLocationURI();
            } else if (var2 instanceof SOAP12Address) {
               this.soapAddressUri = ((SOAP12Address)var2).getLocationURI();
            } else if (var2 instanceof UnknownExtensibilityElement) {
               this.parseUnknownExtensibilityElement((UnknownExtensibilityElement)var2);
            }
         }
      }

      private void parseUnknownExtensibilityElement(UnknownExtensibilityElement var1) {
         Element var2 = var1.getElement();
         if (isSoap12Address(var2)) {
            this.parseSoap12Address(var2);
         }
      }

      private static boolean isSoap12Address(Element var0) {
         return "address".equals(var0.getLocalName()) && "http://schemas.xmlsoap.org/wsdl/soap12/".equals(var0.getNamespaceURI());
      }

      private void parseSoap12Address(Element var1) {
         this.soapAddressUri = var1.getAttribute("location");
      }

      private WebServiceInfoBuilder.BindingInfo parseBinding() throws WsdlParserException {
         WebServiceInfoBuilder.BindingParser var1 = this.createBindingParser();
         List var2 = var1.parse();
         if (var2 == null) {
            return null;
         }

         for (Object __etlItem496 : var2) {
            WebServiceOperationInfo var4 = (WebServiceOperationInfo) __etlItem496;
            this.setAdditionalProperties(var4);
         }

         return new WebServiceInfoBuilder.BindingInfo(var2);
      }

      private WebServiceInfoBuilder.BindingParser createBindingParser() throws WsdlParserException {
         Binding var1 = this.port.getBinding();
         PortType var2 = var1.getPortType();
         if (var2 == null) {
            throw new WsdlParserException(
               "Could not parse the binding " + var1.getQName() + " for the port " + this.port.getName() + ". Please make sure that the WSDL file is valid."
            );
         }

         WebServiceInfoBuilder.OperationIdentifiers var3 = this.portTypes.getOperations(var2);
         return new WebServiceInfoBuilder.BindingParser(var1, var3);
      }

      private void setAdditionalProperties(WebServiceOperationInfo var1) {
         var1.setProperty("Port", this.port.getName());
         var1.setProperty("WebService_Url", this.soapAddressUri);
      }
   }

   private static class PortTypeInfo {
      private final Map<QName, WebServiceInfoBuilder.OperationIdentifiers> portTypes = Maps.newHashMap();

      private PortTypeInfo() {
      }

      public void add(PortType var1, WebServiceInfoBuilder.OperationIdentifiers var2) {
         this.portTypes.put(var1.getQName(), var2);
      }

      public WebServiceInfoBuilder.OperationIdentifiers getOperations(PortType var1) {
         return this.portTypes.get(var1.getQName());
      }
   }

   private static class ServiceParser {
      private final Service service;
      private final String serviceName;
      private final WebServiceInfoBuilder.PortTypeInfo portTypes;

      public ServiceParser(Service var1, WebServiceInfoBuilder.PortTypeInfo var2) {
         this.service = var1;
         this.serviceName = var1.getQName().getLocalPart();
         this.portTypes = var2;
      }

      public List<WebServiceInfoBuilder.BindingInfo> parse() throws WsdlParserException {
         return this.parsePorts();
      }

      private List<WebServiceInfoBuilder.BindingInfo> parsePorts() throws WsdlParserException {
         ArrayList var1 = new ArrayList();

         for (Object var3 : this.service.getPorts().values()) {
            WebServiceInfoBuilder.BindingInfo var4 = this.parsePort((Port)var3);
            if (var4 != null) {
               var1.add(var4);
            }
         }

         return var1;
      }

      private WebServiceInfoBuilder.BindingInfo parsePort(Port var1) throws WsdlParserException {
         WebServiceInfoBuilder.PortParser var2 = new WebServiceInfoBuilder.PortParser(var1, this.portTypes);
         WebServiceInfoBuilder.BindingInfo var3 = var2.parse();
         if (var3 != null) {
            this.setAdditionalProperties(var3);
         }

         return var3;
      }

      private void setAdditionalProperties(WebServiceInfoBuilder.BindingInfo var1) {
         for (WebServiceOperationInfo var3 : var1.operations) {
            var3.setProperty("Service", this.serviceName);
         }
      }
   }

   private static class SoapBindingInfo {
      private final SoapVersion soapVersion;
      private final String soapStyle;
      private final boolean isHttpTransport;

      public static WebServiceInfoBuilder.SoapBindingInfo fromSoap11Binding(SOAPBinding var0) {
         SoapVersion var1 = SoapVersion.fromUri(var0.getElementType().getNamespaceURI());
         String var2 = var0.getStyle();
         boolean var3 = "http://schemas.xmlsoap.org/soap/http".equals(var0.getTransportURI());
         return new WebServiceInfoBuilder.SoapBindingInfo(var1, var2, var3);
      }

      public static WebServiceInfoBuilder.SoapBindingInfo fromSoap12Binding(SOAP12Binding var0) {
         SoapVersion var1 = SoapVersion.fromUri(var0.getElementType().getNamespaceURI());
         String var2 = var0.getStyle();
         boolean var3 = "http://schemas.xmlsoap.org/soap/http".equals(var0.getTransportURI());
         return new WebServiceInfoBuilder.SoapBindingInfo(var1, var2, var3);
      }

      public static WebServiceInfoBuilder.SoapBindingInfo fromDomElement(Element var0) {
         SoapVersion var1 = SoapVersion.fromUri(var0.getNamespaceURI());
         String var2 = var0.getAttribute("style");
         if (var2 == null || var2.length() == 0) {
            var2 = "document";
         }

         boolean var3 = "http://schemas.xmlsoap.org/soap/http".equals(var0.getAttribute("transport"));
         return new WebServiceInfoBuilder.SoapBindingInfo(var1, var2, var3);
      }

      public SoapBindingInfo(SoapVersion var1, String var2, boolean var3) {
         this.soapVersion = var1;
         this.soapStyle = var2;
         this.isHttpTransport = var3;
      }

      public void applyTo(WebServiceOperationInfo var1) {
         if (var1.getProperty("SoapStyle") == null) {
            var1.setProperty("SoapStyle", this.soapStyle);
         }

         if (this.isHttpTransport) {
            var1.setProperty("Soap_Transport", "HTTP");
         }

         var1.setProperty("SoapVersion", this.soapVersion.getVersionAsString());
      }
   }
}
