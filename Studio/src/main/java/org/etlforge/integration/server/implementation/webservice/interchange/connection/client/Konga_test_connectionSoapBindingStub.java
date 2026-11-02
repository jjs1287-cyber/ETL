package org.etlforge.integration.server.implementation.webservice.interchange.connection.client;

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

public class Konga_test_connectionSoapBindingStub extends Stub implements TestConnection {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[3];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("testFileTypeConnection");
      ParameterDesc var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in2"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "ConnectionInfo"),
         ConnectionInfo.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in4"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "FileList"));
      var0.setReturnClass(FileList.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "testFileTypeConnectionReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("testDatabaseConnection");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in2"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "ConnectionInfo"),
         ConnectionInfo.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in4"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setReturnClass(String.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "testDatabaseConnectionReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("testLdapConnection");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in2"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "ConnectionInfo"),
         ConnectionInfo.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "in4"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setReturnClass(String.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "testLdapConnectionReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
   }

   public Konga_test_connectionSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_test_connectionSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_test_connectionSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      QName var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "ConnectionInfo");
      this.cachedSerQNames.add(var3);
      var2 = ConnectionInfo.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "FileList");
      this.cachedSerQNames.add(var3);
      var2 = FileList.class;
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
   public FileList testFileTypeConnection(String var1, String var2, ConnectionInfo var3, String var4, boolean var5) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var6 = this.createCall();
      var6.setOperation(_operations[0]);
      var6.setUseSOAPAction(true);
      var6.setSOAPActionURI("konga_test_connection#testFileTypeConnection");
      var6.setEncodingStyle(null);
      var6.setProperty("sendXsiTypes", Boolean.FALSE);
      var6.setProperty("sendMultiRefs", Boolean.FALSE);
      var6.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var6.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "testFileTypeConnection")
      );
      this.setRequestHeaders(var6);
      this.setAttachments(var6);

      try {
         Object var7 = var6.invoke(new Object[]{var1, var2, var3, var4, new Boolean(var5)});
         if (var7 instanceof RemoteException) {
            throw (RemoteException)var7;
         }

         this.extractAttachments(var6);

         try {
            return (FileList)var7;
         } catch (Exception var9) {
            return (FileList)JavaUtils.convert(var7, FileList.class);
         }
      } catch (AxisFault var10) {
         throw var10;
      }
   }

   @Override
   public String testDatabaseConnection(String var1, String var2, ConnectionInfo var3, String var4, boolean var5) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var6 = this.createCall();
      var6.setOperation(_operations[1]);
      var6.setUseSOAPAction(true);
      var6.setSOAPActionURI("konga_test_connection#testDatabaseConnection");
      var6.setEncodingStyle(null);
      var6.setProperty("sendXsiTypes", Boolean.FALSE);
      var6.setProperty("sendMultiRefs", Boolean.FALSE);
      var6.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var6.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "testDatabaseConnection")
      );
      this.setRequestHeaders(var6);
      this.setAttachments(var6);

      try {
         Object var7 = var6.invoke(new Object[]{var1, var2, var3, var4, new Boolean(var5)});
         if (var7 instanceof RemoteException) {
            throw (RemoteException)var7;
         }

         this.extractAttachments(var6);

         try {
            return (String)var7;
         } catch (Exception var9) {
            return (String)JavaUtils.convert(var7, String.class);
         }
      } catch (AxisFault var10) {
         throw var10;
      }
   }

   @Override
   public String testLdapConnection(String var1, String var2, ConnectionInfo var3, String var4, boolean var5) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var6 = this.createCall();
      var6.setOperation(_operations[2]);
      var6.setUseSOAPAction(true);
      var6.setSOAPActionURI("konga_test_connection#testLdapConnection");
      var6.setEncodingStyle(null);
      var6.setProperty("sendXsiTypes", Boolean.FALSE);
      var6.setProperty("sendMultiRefs", Boolean.FALSE);
      var6.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var6.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.connection.TestConnection", "testLdapConnection")
      );
      this.setRequestHeaders(var6);
      this.setAttachments(var6);

      try {
         Object var7 = var6.invoke(new Object[]{var1, var2, var3, var4, new Boolean(var5)});
         if (var7 instanceof RemoteException) {
            throw (RemoteException)var7;
         }

         this.extractAttachments(var6);

         try {
            return (String)var7;
         } catch (Exception var9) {
            return (String)JavaUtils.convert(var7, String.class);
         }
      } catch (AxisFault var10) {
         throw var10;
      }
   }

   static {
      _initOperationDesc1();
   }
}
