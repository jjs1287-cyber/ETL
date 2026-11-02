package org.etlforge.integration.server.backend.webservice.netsuite;

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
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.connector.WsNameValuePair;
import org.etlforge.integration.server.backend.webservice.connector.WsStatus;

public class Konga_netsuitedesign_engineSoapBindingStub extends Stub implements NetSuiteDesignEngine {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[3];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("GetSavedSearch");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "SelectedObject"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteSavedSearch"));
      var0.setReturnClass(WsNetSuiteSavedSearch.class);
      var0.setReturnQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "GetSavedSearchReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("GetNetSuiteCustomFieldsNew");
      var1 = new ParameterDesc(
         new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "IsCustomObject"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "CustomFieldKey"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteObjectCustomFieldsNew"));
      var0.setReturnClass(WsNetSuiteObjectCustomFieldsNew.class);
      var0.setReturnQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "GetNetSuiteCustomFieldsNewReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("GetNetSuiteDefinitionContents");
      var1 = new ParameterDesc(
         new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "EndpointParameters"),
         (byte)1,
         new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsEndpointParameters"),
         WsEndpointParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteDefinitionContents"));
      var0.setReturnClass(WsNetSuiteDefinitionContents.class);
      var0.setReturnQName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "GetNetSuiteDefinitionContentsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
   }

   public Konga_netsuitedesign_engineSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_netsuitedesign_engineSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_netsuitedesign_engineSoapBindingStub(Service var1) throws AxisFault {
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
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "ArrayOf_tns2_WsNameValuePair");
      this.cachedSerQNames.add(var3);
      var2 = WsNameValuePair[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsNameValuePair");
      QName var4 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      var4 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsNetSuiteCustomFieldNew");
      this.cachedSerQNames.add(var3);
      var2 = WsNetSuiteCustomFieldNew[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldNew");
      var4 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsNetSuiteCustomFieldsNew");
      this.cachedSerQNames.add(var3);
      var2 = WsNetSuiteCustomFieldsNew[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldsNew");
      var4 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldNew");
      this.cachedSerQNames.add(var3);
      var2 = WsNetSuiteCustomFieldNew.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteCustomFieldsNew");
      this.cachedSerQNames.add(var3);
      var2 = WsNetSuiteCustomFieldsNew.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteDefinitionContents");
      this.cachedSerQNames.add(var3);
      var2 = WsNetSuiteDefinitionContents.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteObjectCustomFieldsNew");
      this.cachedSerQNames.add(var3);
      var2 = WsNetSuiteObjectCustomFieldsNew.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "WsNetSuiteSavedSearch");
      this.cachedSerQNames.add(var3);
      var2 = WsNetSuiteSavedSearch.class;
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
   public WsNetSuiteSavedSearch getSavedSearch(WsEndpointParameters var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[0]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("GetSavedSearch");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "GetSavedSearch"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsNetSuiteSavedSearch)var4;
         } catch (Exception var6) {
            return (WsNetSuiteSavedSearch)JavaUtils.convert(var4, WsNetSuiteSavedSearch.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsNetSuiteObjectCustomFieldsNew getNetSuiteCustomFieldsNew(WsEndpointParameters var1, boolean var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[1]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("GetNetSuiteCustomFieldsNew");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "GetNetSuiteCustomFieldsNew"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, new Boolean(var2), var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsNetSuiteObjectCustomFieldsNew)var5;
         } catch (Exception var7) {
            return (WsNetSuiteObjectCustomFieldsNew)JavaUtils.convert(var5, WsNetSuiteObjectCustomFieldsNew.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsNetSuiteDefinitionContents getNetSuiteDefinitionContents(WsEndpointParameters var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[2]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("GetNetSuiteDefinitionContents");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://netsuite.webservice.backend.server.integration.etlforge.invalid", "GetNetSuiteDefinitionContents"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsNetSuiteDefinitionContents)var3;
         } catch (Exception var5) {
            return (WsNetSuiteDefinitionContents)JavaUtils.convert(var3, WsNetSuiteDefinitionContents.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   static {
      _initOperationDesc1();
   }
}
