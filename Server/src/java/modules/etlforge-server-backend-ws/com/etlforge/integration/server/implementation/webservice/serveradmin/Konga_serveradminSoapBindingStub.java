package com.etlforge.integration.server.implementation.webservice.serveradmin;

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

public class Konga_serveradminSoapBindingStub extends Stub implements ServerAdministration {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[7];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("downloadFile");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "path"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setReturnClass(String.class);
      var0.setReturnQName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "downloadFileReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("downloadServerConfigurationFile");
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setReturnClass(String.class);
      var0.setReturnQName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "downloadServerConfigurationFileReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("uploadServerConfigurationFile");
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "zippedAndBase64EncodedContents"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("listServerLogFiles");
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocators"));
      var0.setReturnClass(WsServerFileLocators.class);
      var0.setReturnQName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listServerLogFilesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("listPluginLogFiles");
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocators"));
      var0.setReturnClass(WsServerFileLocators.class);
      var0.setReturnQName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listPluginLogFilesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
      var0 = new OperationDesc();
      var0.setName("listDebugLogFiles");
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "startDate"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "endDate"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocators"));
      var0.setReturnClass(WsServerFileLocators.class);
      var0.setReturnQName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listDebugLogFilesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[5] = var0;
      var0 = new OperationDesc();
      var0.setName("listOperationLogFiles");
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "startDate"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "endDate"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocators"));
      var0.setReturnClass(WsServerFileLocators.class);
      var0.setReturnQName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listOperationLogFilesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[6] = var0;
   }

   public Konga_serveradminSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_serveradminSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_serveradminSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsServerFileLocator");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsServerFileLocator[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocator");
      QName var4 = new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocator");
      this.cachedSerQNames.add(var3);
      var2 = WsServerFileLocator.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "WsServerFileLocators");
      this.cachedSerQNames.add(var3);
      var2 = WsServerFileLocators.class;
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
   public String downloadFile(String var1, String var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[0]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("downloadFile");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "downloadFile"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (String)var5;
         } catch (Exception var7) {
            return (String)JavaUtils.convert(var5, String.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public String downloadServerConfigurationFile(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[1]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("downloadServerConfigurationFile");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "downloadServerConfigurationFile"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (String)var4;
         } catch (Exception var6) {
            return (String)JavaUtils.convert(var4, String.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public void uploadServerConfigurationFile(String var1, String var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[2]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("uploadServerConfigurationFile");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "uploadServerConfigurationFile"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsServerFileLocators listServerLogFiles(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[3]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("listServerLogFiles");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listServerLogFiles"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsServerFileLocators)var4;
         } catch (Exception var6) {
            return (WsServerFileLocators)JavaUtils.convert(var4, WsServerFileLocators.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsServerFileLocators listPluginLogFiles(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[4]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("listPluginLogFiles");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listPluginLogFiles"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsServerFileLocators)var4;
         } catch (Exception var6) {
            return (WsServerFileLocators)JavaUtils.convert(var4, WsServerFileLocators.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsServerFileLocators listDebugLogFiles(String var1, String var2, String var3, String var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[5]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("listDebugLogFiles");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listDebugLogFiles"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsServerFileLocators)var6;
         } catch (Exception var8) {
            return (WsServerFileLocators)JavaUtils.convert(var6, WsServerFileLocators.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public WsServerFileLocators listOperationLogFiles(String var1, String var2, String var3, String var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[6]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("listOperationLogFiles");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://serveradmin.webservice.implementation.server.integration.etlforge.invalid", "listOperationLogFiles"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsServerFileLocators)var6;
         } catch (Exception var8) {
            return (WsServerFileLocators)JavaUtils.convert(var6, WsServerFileLocators.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   static {
      _initOperationDesc1();
   }
}
