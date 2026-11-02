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

public class Konga_odbc_infoproviderSoapBindingStub extends Stub implements OdbcInfoProvider {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[3];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("getDatabaseObjects");
      ParameterDesc var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "driverName"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "connectionString"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "sourceGuid"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "targetGuid"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "objectTypes"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "searchString"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseInfo"));
      var0.setReturnClass(WsDatabaseInfo.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "getDatabaseObjectsReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("getTableInfo");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "driverName"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "connectionString"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "sourceGuid"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "targetGuid"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "tables"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseObject"),
         WsDatabaseObject[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseInfo"));
      var0.setReturnClass(WsDatabaseInfo.class);
      var0.setReturnQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "getTableInfoReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("getDrivers");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseDriverInfo"));
      var0.setReturnClass(WsDatabaseDriverInfo.class);
      var0.setReturnQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "getDriversReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
   }

   public Konga_odbc_infoproviderSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_odbc_infoproviderSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_odbc_infoproviderSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "ArrayOfWsDatabaseColumn");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsDatabaseColumn[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseColumn");
      QName var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "ArrayOfWsDatabaseObject");
      this.cachedSerQNames.add(var3);
      var2 = WsDatabaseObject[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseObject");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "ArrayOfWsOdbcDriverDescriptor");
      this.cachedSerQNames.add(var3);
      var2 = WsOdbcDriverDescriptor[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsOdbcDriverDescriptor");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseColumn");
      this.cachedSerQNames.add(var3);
      var2 = WsDatabaseColumn.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseDriverInfo");
      this.cachedSerQNames.add(var3);
      var2 = WsDatabaseDriverInfo.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseInfo");
      this.cachedSerQNames.add(var3);
      var2 = WsDatabaseInfo.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsDatabaseObject");
      this.cachedSerQNames.add(var3);
      var2 = WsDatabaseObject.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "WsOdbcDriverDescriptor");
      this.cachedSerQNames.add(var3);
      var2 = WsOdbcDriverDescriptor.class;
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
   public WsDatabaseInfo getDatabaseObjects(String var1, String var2, String var3, String var4, String var5, String var6, String[] var7, String var8) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var9 = this.createCall();
      var9.setOperation(_operations[0]);
      var9.setUseSOAPAction(true);
      var9.setSOAPActionURI("getDatabaseObjects");
      var9.setEncodingStyle(null);
      var9.setProperty("sendXsiTypes", Boolean.FALSE);
      var9.setProperty("sendMultiRefs", Boolean.FALSE);
      var9.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var9.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "getDatabaseObjects"));
      this.setRequestHeaders(var9);
      this.setAttachments(var9);

      try {
         Object var10 = var9.invoke(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8});
         if (var10 instanceof RemoteException) {
            throw (RemoteException)var10;
         }

         this.extractAttachments(var9);

         try {
            return (WsDatabaseInfo)var10;
         } catch (Exception var12) {
            return (WsDatabaseInfo)JavaUtils.convert(var10, WsDatabaseInfo.class);
         }
      } catch (AxisFault var13) {
         throw var13;
      }
   }

   @Override
   public WsDatabaseInfo getTableInfo(String var1, String var2, String var3, String var4, String var5, String var6, WsDatabaseObject[] var7) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var8 = this.createCall();
      var8.setOperation(_operations[1]);
      var8.setUseSOAPAction(true);
      var8.setSOAPActionURI("getTableInfo");
      var8.setEncodingStyle(null);
      var8.setProperty("sendXsiTypes", Boolean.FALSE);
      var8.setProperty("sendMultiRefs", Boolean.FALSE);
      var8.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var8.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "getTableInfo"));
      this.setRequestHeaders(var8);
      this.setAttachments(var8);

      try {
         Object var9 = var8.invoke(new Object[]{var1, var2, var3, var4, var5, var6, var7});
         if (var9 instanceof RemoteException) {
            throw (RemoteException)var9;
         }

         this.extractAttachments(var8);

         try {
            return (WsDatabaseInfo)var9;
         } catch (Exception var11) {
            return (WsDatabaseInfo)JavaUtils.convert(var9, WsDatabaseInfo.class);
         }
      } catch (AxisFault var12) {
         throw var12;
      }
   }

   @Override
   public WsDatabaseDriverInfo getDrivers(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[2]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("getDrivers");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.db.OdbcInfoProvider", "getDrivers"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsDatabaseDriverInfo)var4;
         } catch (Exception var6) {
            return (WsDatabaseDriverInfo)JavaUtils.convert(var4, WsDatabaseDriverInfo.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   static {
      _initOperationDesc1();
   }
}
