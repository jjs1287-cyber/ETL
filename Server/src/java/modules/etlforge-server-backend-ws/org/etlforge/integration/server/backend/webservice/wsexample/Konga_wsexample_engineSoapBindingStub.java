package org.etlforge.integration.server.backend.webservice.wsexample;

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
import org.apache.axis.encoding.XMLType;
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

public class Konga_wsexample_engineSoapBindingStub extends Stub implements WsExampleEngine {
   private final Vector cachedSerClasses = new Vector();
   private final Vector cachedSerQNames = new Vector();
   private final Vector cachedSerFactories = new Vector();
   private final Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[4];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("TestExample");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "Parameters"),
         (byte)1,
         new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "WsParameters"),
         WsParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setReturnClass(WsStatus.class);
      var0.setReturnQName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "TestExampleReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("TestNoOutput");
      var1 = new ParameterDesc(
         new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "Parameters"),
         (byte)1,
         new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "WsParameters"),
         WsParameters.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("TestNoInput");
      var0.setReturnType(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      var0.setReturnClass(WsStatus.class);
      var0.setReturnQName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "TestNoInputReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("TestNoInputOutput");
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
   }

   public Konga_wsexample_engineSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_wsexample_engineSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_wsexample_engineSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      QName var4 = new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "WsParameters");
      this.cachedSerQNames.add(var3);
      var2 = WsParameters.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "WsStatus");
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
   public WsStatus testExample(WsParameters var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[0]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("TestExample");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "TestExample"));
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
   public void testNoOutput(WsParameters var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[1]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("TestNoOutput");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "TestNoOutput"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);
      } catch (AxisFault var4) {
         throw var4;
      }
   }

   @Override
   public WsStatus testNoInput() throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var1 = this.createCall();
      var1.setOperation(_operations[2]);
      var1.setUseSOAPAction(true);
      var1.setSOAPActionURI("TestNoInput");
      var1.setEncodingStyle(null);
      var1.setProperty("sendXsiTypes", Boolean.FALSE);
      var1.setProperty("sendMultiRefs", Boolean.FALSE);
      var1.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var1.setOperationName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "TestNoInput"));
      this.setRequestHeaders(var1);
      this.setAttachments(var1);

      try {
         Object var2 = var1.invoke(new Object[0]);
         if (var2 instanceof RemoteException) {
            throw (RemoteException)var2;
         }

         this.extractAttachments(var1);

         try {
            return (WsStatus)var2;
         } catch (Exception var4) {
            return (WsStatus)JavaUtils.convert(var2, WsStatus.class);
         }
      } catch (AxisFault var5) {
         throw var5;
      }
   }

   @Override
   public void testNoInputOutput() throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var1 = this.createCall();
      var1.setOperation(_operations[3]);
      var1.setUseSOAPAction(true);
      var1.setSOAPActionURI("TestNoInputOutput");
      var1.setEncodingStyle(null);
      var1.setProperty("sendXsiTypes", Boolean.FALSE);
      var1.setProperty("sendMultiRefs", Boolean.FALSE);
      var1.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var1.setOperationName(new QName("http://wsexample.webservice.backend.server.integration.etlforge.invalid", "TestNoInputOutput"));
      this.setRequestHeaders(var1);
      this.setAttachments(var1);

      try {
         Object var2 = var1.invoke(new Object[0]);
         if (var2 instanceof RemoteException) {
            throw (RemoteException)var2;
         }

         this.extractAttachments(var1);
      } catch (AxisFault var3) {
         throw var3;
      }
   }

   static {
      _initOperationDesc1();
   }
}
