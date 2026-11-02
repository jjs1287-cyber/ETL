package org.etlforge.integration.server.implementation.webservice.interchange.webservice.client;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.encoding.SerializerFactory;
import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListSerializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.soap.SOAPConstants;
import org.apache.axis.utils.JavaUtils;

public class Konga_webservice_infoproviderSoapBindingStub extends Stub implements WebServiceInfoProvider {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[6];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("getWsdlFiles");
      ParameterDesc var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFiles")
      );
      var0.setReturnClass(CWsdlFiles.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "getWsdlFilesReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("downloadWsdl");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "wsdlFile"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"),
         CWsdlFile.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFileDownloadResult")
      );
      var0.setReturnClass(CWsdlFileDownloadResult.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "downloadWsdlReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("uploadWsdl");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "wsdlFile"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"),
         CWsdlFile.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "parentWsdlFile"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"),
         CWsdlFile.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFileUploadResult")
      );
      var0.setReturnClass(CWsdlFileUploadResult.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "uploadWsdlReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("generateSoapSchema");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "wsOperation"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WebServiceOperationData"),
         WebServiceOperationData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "requestSchemaFileName"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "responseSchemaFileName"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "forceOverwrite"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "defineSoapHeader"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "SoapSchemaGenerationResult")
      );
      var0.setReturnClass(SoapSchemaGenerationResult.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "generateSoapSchemaReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("deleteWsdl");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "wsdlFile"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile"),
         CWsdlFile.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "force"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setReturnClass(String[].class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "deleteWsdlReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
      var0 = new OperationDesc();
      var0.setName("getInputAndOutputJTR");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "wsOperation"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WebServiceOperationData"),
         WebServiceOperationData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WebServiceJtr")
      );
      var0.setReturnClass(WebServiceJtr.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "getInputAndOutputJTRReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[5] = var0;
   }

   public Konga_webservice_infoproviderSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_webservice_infoproviderSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_webservice_infoproviderSoapBindingStub(Service var1) throws AxisFault {
      if (var1 == null) {
         super.service = new org.apache.axis.client.Service();
      } else {
         super.service = var1;
      }

      ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
      Class<?> var5 = BeanSerializerFactory.class;
      Class<?> var6 = BeanDeserializerFactory.class;
      Class<?> var7 = EnumSerializerFactory.class;
      Class<?> var8 = EnumDeserializerFactory.class;
      Class<?> var9 = ArraySerializerFactory.class;
      Class<?> var10 = ArrayDeserializerFactory.class;
      Class<?> var11 = SimpleSerializerFactory.class;
      Class<?> var12 = SimpleDeserializerFactory.class;
      Class<?> var13 = SimpleListSerializerFactory.class;
      Class<?> var14 = SimpleListDeserializerFactory.class;
      QName var3 = new QName(
         "urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "ArrayOf_xsd_string"
      );
      this.cachedSerQNames.add(var3);
      Class<?> var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      QName var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "ArrayOfCWsdlFile");
      this.cachedSerQNames.add(var3);
      var2 = CWsdlFile[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFile");
      this.cachedSerQNames.add(var3);
      var2 = CWsdlFile.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName(
         "urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFileDownloadResult"
      );
      this.cachedSerQNames.add(var3);
      var2 = CWsdlFileDownloadResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFiles");
      this.cachedSerQNames.add(var3);
      var2 = CWsdlFiles.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "CWsdlFileUploadResult");
      this.cachedSerQNames.add(var3);
      var2 = CWsdlFileUploadResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName(
         "urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "SoapSchemaGenerationResult"
      );
      this.cachedSerQNames.add(var3);
      var2 = SoapSchemaGenerationResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WebServiceJtr");
      this.cachedSerQNames.add(var3);
      var2 = WebServiceJtr.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName(
         "urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "WebServiceOperationData"
      );
      this.cachedSerQNames.add(var3);
      var2 = WebServiceOperationData.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
   }

   protected Call createCall() throws RemoteException {
      try {
         Call var1 = super._createCall();
         if (super.maintainSessionSet) {
            var1.setMaintainSession(super.maintainSession);
         }

         if (super.cachedUsername != null) {
            var1.setUsername(super.cachedUsername);
         }

         if (super.cachedPassword != null) {
            var1.setPassword(super.cachedPassword);
         }

         if (super.cachedEndpoint != null) {
            var1.setTargetEndpointAddress(super.cachedEndpoint);
         }

         if (super.cachedTimeout != null) {
            var1.setTimeout(super.cachedTimeout);
         }

         if (super.cachedPortName != null) {
            var1.setPortName(super.cachedPortName);
         }

         Enumeration var2 = super.cachedProperties.keys();

         while (var2.hasMoreElements()) {
            String var3 = (String)var2.nextElement();
            var1.setProperty(var3, super.cachedProperties.get(var3));
         }

         synchronized (this) {
            if (this.firstCall()) {
               var1.setEncodingStyle(null);

               for (int var4 = 0; var4 < this.cachedSerFactories.size(); var4++) {
                  Class var5 = (Class)this.cachedSerClasses.get(var4);
                  QName var6 = (QName)this.cachedSerQNames.get(var4);
                  Object var7 = this.cachedSerFactories.get(var4);
                  if (var7 instanceof Class) {
                     Class var8 = (Class)this.cachedSerFactories.get(var4);
                     Class var9 = (Class)this.cachedDeserFactories.get(var4);
                     var1.registerTypeMapping(var5, var6, var8, var9, false);
                  } else if (var7 instanceof SerializerFactory) {
                     org.apache.axis.encoding.SerializerFactory var13 = (org.apache.axis.encoding.SerializerFactory)this.cachedSerFactories.get(var4);
                     DeserializerFactory var14 = (DeserializerFactory)this.cachedDeserFactories.get(var4);
                     var1.registerTypeMapping(var5, var6, var13, var14, false);
                  }
               }
            }
         }

         return var1;
      } catch (Throwable var12) {
         throw new AxisFault("Failure trying to get the Call object", var12);
      }
   }

   @Override
   public CWsdlFiles getWsdlFiles(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[0]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("getWsdlFiles");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "getWsdlFiles")
      );
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (CWsdlFiles)var4;
         } catch (Exception var6) {
            return (CWsdlFiles)JavaUtils.convert(var4, CWsdlFiles.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public CWsdlFileDownloadResult downloadWsdl(String var1, String var2, CWsdlFile var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[1]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("downloadWsdl");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "downloadWsdl")
      );
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (CWsdlFileDownloadResult)var5;
         } catch (Exception var7) {
            return (CWsdlFileDownloadResult)JavaUtils.convert(var5, CWsdlFileDownloadResult.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public CWsdlFileUploadResult uploadWsdl(String var1, String var2, CWsdlFile var3, CWsdlFile var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[2]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("uploadWsdl");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "uploadWsdl")
      );
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (CWsdlFileUploadResult)var6;
         } catch (Exception var8) {
            return (CWsdlFileUploadResult)JavaUtils.convert(var6, CWsdlFileUploadResult.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public SoapSchemaGenerationResult generateSoapSchema(
      String var1, String var2, WebServiceOperationData var3, String var4, String var5, boolean var6, boolean var7
   ) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var8 = this.createCall();
      var8.setOperation(_operations[3]);
      var8.setUseSOAPAction(true);
      var8.setSOAPActionURI("generateSoapSchema");
      var8.setEncodingStyle(null);
      var8.setProperty("sendXsiTypes", Boolean.FALSE);
      var8.setProperty("sendMultiRefs", Boolean.FALSE);
      var8.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var8.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "generateSoapSchema")
      );
      this.setRequestHeaders(var8);
      this.setAttachments(var8);

      try {
         Object var9 = var8.invoke(new Object[]{var1, var2, var3, var4, var5, new Boolean(var6), new Boolean(var7)});
         if (var9 instanceof RemoteException) {
            throw (RemoteException)var9;
         }

         this.extractAttachments(var8);

         try {
            return (SoapSchemaGenerationResult)var9;
         } catch (Exception var11) {
            return (SoapSchemaGenerationResult)JavaUtils.convert(var9, SoapSchemaGenerationResult.class);
         }
      } catch (AxisFault var12) {
         throw var12;
      }
   }

   @Override
   public String[] deleteWsdl(String var1, String var2, CWsdlFile var3, boolean var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[4]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("deleteWsdl");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "deleteWsdl")
      );
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, new Boolean(var4)});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (String[])var6;
         } catch (Exception var8) {
            return (String[])JavaUtils.convert(var6, String[].class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public WebServiceJtr getInputAndOutputJTR(String var1, String var2, WebServiceOperationData var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[5]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("getInputAndOutputJTR");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.webservice.WebServiceInfoProvider", "getInputAndOutputJTR")
      );
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WebServiceJtr)var5;
         } catch (Exception var7) {
            return (WebServiceJtr)JavaUtils.convert(var5, WebServiceJtr.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   static {
      _initOperationDesc1();
   }
}
