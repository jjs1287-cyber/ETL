package com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client;

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

public class Konga_bulkload_infoproviderSoapBindingStub extends Stub implements BulkLoadInfoProvider {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[2];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("listInstances");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "md5Pwd"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "filter"),
         (byte)1,
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsInstanceFilter"),
         WsInstanceFilter.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstance"));
      var0.setReturnClass(WsBulkLoadInstance[].class);
      var0.setReturnQName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "listInstancesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("listBatches");
      var1 = new ParameterDesc(
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "md5Pwd"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "instanceId"),
         (byte)1,
         new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstanceId"),
         WsBulkLoadInstanceId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsRecordOfSentBatch"));
      var0.setReturnClass(WsRecordOfSentBatch[].class);
      var0.setReturnQName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "listBatchesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
   }

   public Konga_bulkload_infoproviderSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_bulkload_infoproviderSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_bulkload_infoproviderSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBatch");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsBatch.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstance");
      this.cachedSerQNames.add(var3);
      var2 = WsBulkLoadInstance.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstanceId");
      this.cachedSerQNames.add(var3);
      var2 = WsBulkLoadInstanceId.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsInstanceFilter");
      this.cachedSerQNames.add(var3);
      var2 = WsInstanceFilter.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsRecordOfSentBatch");
      this.cachedSerQNames.add(var3);
      var2 = WsRecordOfSentBatch.class;
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
   public WsBulkLoadInstance[] listInstances(String var1, String var2, WsInstanceFilter var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[0]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("listInstances");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "listInstances"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsBulkLoadInstance[])var5;
         } catch (Exception var7) {
            return (WsBulkLoadInstance[])JavaUtils.convert(var5, WsBulkLoadInstance[].class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsRecordOfSentBatch[] listBatches(String var1, String var2, WsBulkLoadInstanceId var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[1]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("listBatches");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "listBatches"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsRecordOfSentBatch[])var5;
         } catch (Exception var7) {
            return (WsRecordOfSentBatch[])JavaUtils.convert(var5, WsRecordOfSentBatch[].class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   static {
      _initOperationDesc1();
   }
}
