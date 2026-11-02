package org.etlforge.integration.server.backend.webservice.connector;

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

public class Konga_connector_engineSoapBindingStub extends Stub implements ETLForgeConnectorEngine {
   private final Vector cachedSerClasses = new Vector();
   private final Vector cachedSerQNames = new Vector();
   private final Vector cachedSerFactories = new Vector();
   private final Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[9];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("GetConnectorSchemas");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ConnectorInput"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorInput"),
         WsConnectorInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemas"));
      var0.setReturnClass(WsConnectorSchemas.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetConnectorSchemasReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("TestConnection");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setReturnClass(WsStatus.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "TestConnectionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("GetAutoDeskItemVersionId");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WorkspaceId"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "DmsId"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorAutoDeskItemInfo"));
      var0.setReturnClass(WsConnectorAutoDeskItemInfo.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetAutoDeskItemVersionIdReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("Login");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSessionResult"));
      var0.setReturnClass(WsConnectorSessionResult.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "LoginReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("Logout");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setReturnClass(WsStatus.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "LogoutReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
      var0 = new OperationDesc();
      var0.setName("GetConnectorSchemaFiles");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "InvalidateCache"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ConnectorInput"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorInput"),
         WsConnectorInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemaFiles"));
      var0.setReturnClass(WsConnectorSchemaFiles.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetConnectorSchemaFilesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[5] = var0;
      var0 = new OperationDesc();
      var0.setName("ExecuteConnector");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ConnectorInput"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorInput"),
         WsConnectorInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "RequestXml"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorExecutionResponse"));
      var0.setReturnClass(WsConnectorExecutionResponse.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ExecuteConnectorReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[6] = var0;
      var0 = new OperationDesc();
      var0.setName("ExecuteConnectorFile");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ConnectorInput"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorInput"),
         WsConnectorInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "RequestXmlFile"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ResponseXmlFile"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setReturnClass(WsStatus.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ExecuteConnectorFileReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[7] = var0;
      var0 = new OperationDesc();
      var0.setName("GetConnectorObjectList");
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ConnectorInput"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorInput"),
         WsConnectorInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ListFilter"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorObjectList"));
      var0.setReturnClass(WsConnectorObjectList.class);
      var0.setReturnQName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetConnectorObjectListReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[8] = var0;
   }

   public Konga_connector_engineSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_connector_engineSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_connector_engineSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      QName var4 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsConnectorObject");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorObject[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorObject");
      var4 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsConnectorSchemaFile");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorSchemaFile[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemaFile");
      var4 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsNameValuePair");
      this.cachedSerQNames.add(var3);
      var2 = WsNameValuePair[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsNameValuePair");
      var4 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorAutoDeskItemInfo");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorAutoDeskItemInfo.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorExecutionResponse");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorExecutionResponse.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorInput");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorInput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorObject");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorObject.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorObjectList");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorObjectList.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemaFile");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorSchemaFile.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemaFiles");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorSchemaFiles.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSchemas");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorSchemas.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsConnectorSessionResult");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectorSessionResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters");
      this.cachedSerQNames.add(var3);
      var2 = WsEndpointParameters.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsNameValuePair");
      this.cachedSerQNames.add(var3);
      var2 = WsNameValuePair.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus");
      this.cachedSerQNames.add(var3);
      var2 = WsStatus.class;
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
   public WsConnectorSchemas getConnectorSchemas(WsConnectorInput var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[0]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("GetConnectorSchemas");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetConnectorSchemas"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsConnectorSchemas)var3;
         } catch (Exception var5) {
            return (WsConnectorSchemas)JavaUtils.convert(var3, WsConnectorSchemas.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsStatus testConnection(WsEndpointParameters var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[1]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("TestConnection");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "TestConnection"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsStatus)var3;
         } catch (Exception var5) {
            return (WsStatus)JavaUtils.convert(var3, WsStatus.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsConnectorAutoDeskItemInfo getAutoDeskItemVersionId(WsEndpointParameters var1, String var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[2]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("GetAutoDeskItemVersionId");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetAutoDeskItemVersionId"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsConnectorAutoDeskItemInfo)var5;
         } catch (Exception var7) {
            return (WsConnectorAutoDeskItemInfo)JavaUtils.convert(var5, WsConnectorAutoDeskItemInfo.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsConnectorSessionResult login(WsEndpointParameters var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[3]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("Login");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Login"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsConnectorSessionResult)var3;
         } catch (Exception var5) {
            return (WsConnectorSessionResult)JavaUtils.convert(var3, WsConnectorSessionResult.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsStatus logout(WsEndpointParameters var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[4]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("Logout");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Logout"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsStatus)var3;
         } catch (Exception var5) {
            return (WsStatus)JavaUtils.convert(var3, WsStatus.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsConnectorSchemaFiles getConnectorSchemaFiles(boolean var1, WsConnectorInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[5]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("GetConnectorSchemaFiles");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetConnectorSchemaFiles"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{new Boolean(var1), var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsConnectorSchemaFiles)var4;
         } catch (Exception var6) {
            return (WsConnectorSchemaFiles)JavaUtils.convert(var4, WsConnectorSchemaFiles.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsConnectorExecutionResponse executeConnector(WsConnectorInput var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[6]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("ExecuteConnector");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ExecuteConnector"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsConnectorExecutionResponse)var4;
         } catch (Exception var6) {
            return (WsConnectorExecutionResponse)JavaUtils.convert(var4, WsConnectorExecutionResponse.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsStatus executeConnectorFile(WsConnectorInput var1, String var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[7]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("ExecuteConnectorFile");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "ExecuteConnectorFile"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsStatus)var5;
         } catch (Exception var7) {
            return (WsStatus)JavaUtils.convert(var5, WsStatus.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsConnectorObjectList getConnectorObjectList(WsConnectorInput var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[8]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("GetConnectorObjectList");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "GetConnectorObjectList"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsConnectorObjectList)var4;
         } catch (Exception var6) {
            return (WsConnectorObjectList)JavaUtils.convert(var4, WsConnectorObjectList.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   static {
      _initOperationDesc1();
   }
}
