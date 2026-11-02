package org.etlforge.integration.server.backend.webservice.mscrm.client;

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

public class Konga_mscrmdesign_engineSoapBindingStub extends Stub implements MSCrmDesignEngine {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[5];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("RunQuery");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgeUser"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgePassword"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "endpointParams"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "fetchXml"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsQueryResult"));
      var0.setReturnClass(WsQueryResult.class);
      var0.setReturnQName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "RunQueryReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("GetQueryResponseSchema");
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgeUser"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgePassword"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "endpointParams"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "fetchXml"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsQueryResponseSchema"));
      var0.setReturnClass(WsQueryResponseSchema.class);
      var0.setReturnQName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetQueryResponseSchemaReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("GetObjectDescriptions");
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgeUser"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgePassword"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "endpointParams"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsMetaDataResponse"));
      var0.setReturnClass(WsMetaDataResponse.class);
      var0.setReturnQName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetObjectDescriptionsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("GetSavedQueries");
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgeUser"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgePassword"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "endpointParams"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "objectName"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsSavedQueries"));
      var0.setReturnClass(WsSavedQueries.class);
      var0.setReturnQName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetSavedQueriesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("GetDetailedMetaData");
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgeUser"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "etlforgePassword"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "endpointParams"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "objectName"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsMetaDataResponse"));
      var0.setReturnClass(WsMetaDataResponse.class);
      var0.setReturnQName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetDetailedMetaDataReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
   }

   public Konga_mscrmdesign_engineSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_mscrmdesign_engineSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_mscrmdesign_engineSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsEndpointParameters.class;
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
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "ArrayOf_tns2_WsNameValuePair");
      this.cachedSerQNames.add(var3);
      var2 = WsNameValuePair[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsNameValuePair");
      QName var4 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsFetchXml");
      this.cachedSerQNames.add(var3);
      var2 = WsFetchXml[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsFetchXml");
      var4 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsFetchXml");
      this.cachedSerQNames.add(var3);
      var2 = WsFetchXml.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsMetaDataResponse");
      this.cachedSerQNames.add(var3);
      var2 = WsMetaDataResponse.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsQueryResponseSchema");
      this.cachedSerQNames.add(var3);
      var2 = WsQueryResponseSchema.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsQueryResult");
      this.cachedSerQNames.add(var3);
      var2 = WsQueryResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsSavedQueries");
      this.cachedSerQNames.add(var3);
      var2 = WsSavedQueries.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "WsSchema");
      this.cachedSerQNames.add(var3);
      var2 = WsSchema.class;
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
   public WsQueryResult runQuery(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[0]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("RunQuery");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "RunQuery"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsQueryResult)var6;
         } catch (Exception var8) {
            return (WsQueryResult)JavaUtils.convert(var6, WsQueryResult.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public WsQueryResponseSchema getQueryResponseSchema(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[1]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("GetQueryResponseSchema");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetQueryResponseSchema"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsQueryResponseSchema)var6;
         } catch (Exception var8) {
            return (WsQueryResponseSchema)JavaUtils.convert(var6, WsQueryResponseSchema.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public WsMetaDataResponse getObjectDescriptions(String var1, String var2, WsEndpointParameters var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[2]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("GetObjectDescriptions");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetObjectDescriptions"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsMetaDataResponse)var5;
         } catch (Exception var7) {
            return (WsMetaDataResponse)JavaUtils.convert(var5, WsMetaDataResponse.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsSavedQueries getSavedQueries(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[3]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("GetSavedQueries");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetSavedQueries"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsSavedQueries)var6;
         } catch (Exception var8) {
            return (WsSavedQueries)JavaUtils.convert(var6, WsSavedQueries.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public WsMetaDataResponse getDetailedMetaData(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[4]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("GetDetailedMetaData");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://mscrm.webservice.backend.server.integration.etlforge.invalid", "GetDetailedMetaData"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsMetaDataResponse)var6;
         } catch (Exception var8) {
            return (WsMetaDataResponse)JavaUtils.convert(var6, WsMetaDataResponse.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   static {
      _initOperationDesc1();
   }
}
