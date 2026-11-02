package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

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

public class Konga_jdbc_infoproviderSoapBindingStub extends Stub implements JdbcInfoProvider {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[5];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("getAllDatabaseObjects");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsConnectionParams"),
         WsConnectionParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in4"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in5"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseInfo"));
      var0.setReturnClass(WsJdbcDatabaseInfo.class);
      var0.setReturnQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getAllDatabaseObjectsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("getDatabaseObjects");
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsConnectionParams"),
         WsConnectionParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in4"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in5"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in6"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in7"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseInfo"));
      var0.setReturnClass(WsJdbcDatabaseInfo.class);
      var0.setReturnQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getDatabaseObjectsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("getTableInfo");
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsConnectionParams"),
         WsConnectionParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in4"),
         (byte)1,
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcBeginEndQuote"),
         WsJdbcBeginEndQuote.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in5"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in6"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in7"),
         (byte)1,
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseObject"),
         WsJdbcDatabaseObject[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseInfo"));
      var0.setReturnClass(WsJdbcDatabaseInfo.class);
      var0.setReturnQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getTableInfoReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("getDrivers");
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDriverInfo"));
      var0.setReturnClass(WsJdbcDriverInfo.class);
      var0.setReturnQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getDriversReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("testConnection");
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsConnectionParams"),
         WsConnectionParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in4"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "in5"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcBeginEndQuote"));
      var0.setReturnClass(WsJdbcBeginEndQuote.class);
      var0.setReturnQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "testConnectionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
   }

   public Konga_jdbc_infoproviderSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_jdbc_infoproviderSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_jdbc_infoproviderSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsJdbcDatabaseColumn");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsJdbcDatabaseColumn[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseColumn");
      QName var4 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsJdbcDatabaseObject");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcDatabaseObject[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseObject");
      var4 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsJdbcDriverDescriptor");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcDriverDescriptor[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDriverDescriptor");
      var4 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsConnectionParams");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectionParams.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcBeginEndQuote");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcBeginEndQuote.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseColumn");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcDatabaseColumn.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseInfo");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcDatabaseInfo.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseObject");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcDatabaseObject.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDriverDescriptor");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcDriverDescriptor.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDriverInfo");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcDriverInfo.class;
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
   public WsJdbcDatabaseInfo getAllDatabaseObjects(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var7 = this.createCall();
      var7.setOperation(_operations[0]);
      var7.setUseSOAPAction(true);
      var7.setSOAPActionURI("getAllDatabaseObjects");
      var7.setEncodingStyle(null);
      var7.setProperty("sendXsiTypes", Boolean.FALSE);
      var7.setProperty("sendMultiRefs", Boolean.FALSE);
      var7.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var7.setOperationName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getAllDatabaseObjects"));
      this.setRequestHeaders(var7);
      this.setAttachments(var7);

      try {
         Object var8 = var7.invoke(new Object[]{var1, var2, var3, var4, var5, var6});
         if (var8 instanceof RemoteException) {
            throw (RemoteException)var8;
         }

         this.extractAttachments(var7);

         try {
            return (WsJdbcDatabaseInfo)var8;
         } catch (Exception var10) {
            return (WsJdbcDatabaseInfo)JavaUtils.convert(var8, WsJdbcDatabaseInfo.class);
         }
      } catch (AxisFault var11) {
         throw var11;
      }
   }

   @Override
   public WsJdbcDatabaseInfo getDatabaseObjects(
      String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6, String[] var7, String var8
   ) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var9 = this.createCall();
      var9.setOperation(_operations[1]);
      var9.setUseSOAPAction(true);
      var9.setSOAPActionURI("getDatabaseObjects");
      var9.setEncodingStyle(null);
      var9.setProperty("sendXsiTypes", Boolean.FALSE);
      var9.setProperty("sendMultiRefs", Boolean.FALSE);
      var9.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var9.setOperationName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getDatabaseObjects"));
      this.setRequestHeaders(var9);
      this.setAttachments(var9);

      try {
         Object var10 = var9.invoke(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8});
         if (var10 instanceof RemoteException) {
            throw (RemoteException)var10;
         }

         this.extractAttachments(var9);

         try {
            return (WsJdbcDatabaseInfo)var10;
         } catch (Exception var12) {
            return (WsJdbcDatabaseInfo)JavaUtils.convert(var10, WsJdbcDatabaseInfo.class);
         }
      } catch (AxisFault var13) {
         throw var13;
      }
   }

   @Override
   public WsJdbcDatabaseInfo getTableInfo(
      String var1, String var2, WsConnectionParams var3, String var4, WsJdbcBeginEndQuote var5, String var6, String var7, WsJdbcDatabaseObject[] var8
   ) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var9 = this.createCall();
      var9.setOperation(_operations[2]);
      var9.setUseSOAPAction(true);
      var9.setSOAPActionURI("getTableInfo");
      var9.setEncodingStyle(null);
      var9.setProperty("sendXsiTypes", Boolean.FALSE);
      var9.setProperty("sendMultiRefs", Boolean.FALSE);
      var9.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var9.setOperationName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getTableInfo"));
      this.setRequestHeaders(var9);
      this.setAttachments(var9);

      try {
         Object var10 = var9.invoke(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8});
         if (var10 instanceof RemoteException) {
            throw (RemoteException)var10;
         }

         this.extractAttachments(var9);

         try {
            return (WsJdbcDatabaseInfo)var10;
         } catch (Exception var12) {
            return (WsJdbcDatabaseInfo)JavaUtils.convert(var10, WsJdbcDatabaseInfo.class);
         }
      } catch (AxisFault var13) {
         throw var13;
      }
   }

   @Override
   public WsJdbcDriverInfo getDrivers(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[3]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("getDrivers");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "getDrivers"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsJdbcDriverInfo)var4;
         } catch (Exception var6) {
            return (WsJdbcDriverInfo)JavaUtils.convert(var4, WsJdbcDriverInfo.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsJdbcBeginEndQuote testConnection(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var7 = this.createCall();
      var7.setOperation(_operations[4]);
      var7.setUseSOAPAction(true);
      var7.setSOAPActionURI("testConnection");
      var7.setEncodingStyle(null);
      var7.setProperty("sendXsiTypes", Boolean.FALSE);
      var7.setProperty("sendMultiRefs", Boolean.FALSE);
      var7.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var7.setOperationName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "testConnection"));
      this.setRequestHeaders(var7);
      this.setAttachments(var7);

      try {
         Object var8 = var7.invoke(new Object[]{var1, var2, var3, var4, var5, var6});
         if (var8 instanceof RemoteException) {
            throw (RemoteException)var8;
         }

         this.extractAttachments(var7);

         try {
            return (WsJdbcBeginEndQuote)var8;
         } catch (Exception var10) {
            return (WsJdbcBeginEndQuote)JavaUtils.convert(var8, WsJdbcBeginEndQuote.class);
         }
      } catch (AxisFault var11) {
         throw var11;
      }
   }

   static {
      _initOperationDesc1();
   }
}
