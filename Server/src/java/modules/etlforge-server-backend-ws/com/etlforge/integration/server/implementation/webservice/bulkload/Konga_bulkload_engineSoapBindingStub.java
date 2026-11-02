package com.etlforge.integration.server.implementation.webservice.bulkload;

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

public class Konga_bulkload_engineSoapBindingStub extends Stub implements BulkLoadEngine {
   private final Vector cachedSerClasses = new Vector();
   private final Vector cachedSerQNames = new Vector();
   private final Vector cachedSerFactories = new Vector();
   private final Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[3];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("startNewBulkLoadSession");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "etlforgeUser"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "md5Pwd"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsStartNewBulkLoadSessionResult"));
      var0.setReturnClass(WsStartNewBulkLoadSessionResult.class);
      var0.setReturnQName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "startNewBulkLoadSessionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("runBulkLoad");
      var1 = new ParameterDesc(
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "sessionId"),
         (byte)1,
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadSessionId"),
         WsBulkLoadSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "bulkloadParams"),
         (byte)1,
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadParams"),
         WsBulkLoadParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "salesforceEndpoint"),
         (byte)1,
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsSalesforceEndpoint"),
         WsSalesforceEndpoint.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsKeyValue"),
         WsKeyValue[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadResult"));
      var0.setReturnClass(WsBulkLoadResult.class);
      var0.setReturnQName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "runBulkLoadReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("closeBulkLoadSession");
      var1 = new ParameterDesc(
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "sessionId"),
         (byte)1,
         new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadSessionId"),
         WsBulkLoadSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadError"));
      var0.setReturnClass(WsBulkLoadError.class);
      var0.setReturnQName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "closeBulkLoadSessionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
   }

   public Konga_bulkload_engineSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_bulkload_engineSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_bulkload_engineSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadError");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsBulkLoadError.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadParams");
      this.cachedSerQNames.add(var3);
      var2 = WsBulkLoadParams.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadResult");
      this.cachedSerQNames.add(var3);
      var2 = WsBulkLoadResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadSessionId");
      this.cachedSerQNames.add(var3);
      var2 = WsBulkLoadSessionId.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsKeyValue");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValue.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsSalesforceEndpoint");
      this.cachedSerQNames.add(var3);
      var2 = WsSalesforceEndpoint.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsStartNewBulkLoadSessionResult");
      this.cachedSerQNames.add(var3);
      var2 = WsStartNewBulkLoadSessionResult.class;
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
   public WsStartNewBulkLoadSessionResult startNewBulkLoadSession(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[0]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("startNewBulkLoadSession");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "startNewBulkLoadSession"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsStartNewBulkLoadSessionResult)var4;
         } catch (Exception var6) {
            return (WsStartNewBulkLoadSessionResult)JavaUtils.convert(var4, WsStartNewBulkLoadSessionResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsBulkLoadResult runBulkLoad(WsBulkLoadSessionId var1, WsBulkLoadParams var2, WsSalesforceEndpoint var3, WsKeyValue[] var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[1]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("runBulkLoad");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "runBulkLoad"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsBulkLoadResult)var6;
         } catch (Exception var8) {
            return (WsBulkLoadResult)JavaUtils.convert(var6, WsBulkLoadResult.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public WsBulkLoadError closeBulkLoadSession(WsBulkLoadSessionId var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[2]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("closeBulkLoadSession");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "closeBulkLoadSession"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsBulkLoadError)var3;
         } catch (Exception var5) {
            return (WsBulkLoadError)JavaUtils.convert(var3, WsBulkLoadError.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   static {
      _initOperationDesc1();
   }
}
