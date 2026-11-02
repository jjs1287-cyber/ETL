package org.etlforge.integration.server.implementation.webservice.interchange.ldap;

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
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public class Konga_ldap_infoproviderSoapBindingStub extends Stub implements LdapInfoProvider {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[2];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("getObjectClasses");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in4"),
         (byte)1,
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsLdapConnectionParams"),
         WsLdapConnectionParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in5"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in6"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in7"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in8"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"),
         WsKeyValuePairs.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsObjectClassInfoReturn"));
      var0.setReturnClass(WsObjectClassInfoReturn.class);
      var0.setReturnQName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "getObjectClassesReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("getObjectStructures");
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in4"),
         (byte)1,
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsLdapConnectionParams"),
         WsLdapConnectionParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in5"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in6"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "boolean"),
         boolean.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "in7"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"),
         WsKeyValuePairs.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsClassStructureReturn"));
      var0.setReturnClass(WsClassStructureReturn.class);
      var0.setReturnQName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "getObjectStructuresReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
   }

   public Konga_ldap_infoproviderSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_ldap_infoproviderSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_ldap_infoproviderSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "ArrayOf_tns2_WsKeyValue");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsKeyValue[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValue");
      QName var4 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      var4 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsObjectClassInfo");
      this.cachedSerQNames.add(var3);
      var2 = WsObjectClassInfo[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsObjectClassInfo");
      var4 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsClassStructureReturn");
      this.cachedSerQNames.add(var3);
      var2 = WsClassStructureReturn.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsLdapConnectionParams");
      this.cachedSerQNames.add(var3);
      var2 = WsLdapConnectionParams.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsObjectClassInfo");
      this.cachedSerQNames.add(var3);
      var2 = WsObjectClassInfo.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsObjectClassInfoReturn");
      this.cachedSerQNames.add(var3);
      var2 = WsObjectClassInfoReturn.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValue");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValue.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValuePairs.class;
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
      } catch (Exception var12) {
         throw new AxisFault("Failure trying to get the Call object", var12);
      }
   }

   @Override
   public WsObjectClassInfoReturn getObjectClasses(
      String var1, String var2, String var3, String var4, WsLdapConnectionParams var5, String var6, boolean var7, boolean var8, WsKeyValuePairs var9
   ) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var10 = this.createCall();
      var10.setOperation(_operations[0]);
      var10.setUseSOAPAction(true);
      var10.setSOAPActionURI("getObjectClasses");
      var10.setEncodingStyle(null);
      var10.setProperty("sendXsiTypes", Boolean.FALSE);
      var10.setProperty("sendMultiRefs", Boolean.FALSE);
      var10.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var10.setOperationName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "getObjectClasses"));
      this.setRequestHeaders(var10);
      this.setAttachments(var10);

      try {
         Object var11 = var10.invoke(new Object[]{var1, var2, var3, var4, var5, var6, new Boolean(var7), new Boolean(var8), var9});
         if (var11 instanceof RemoteException) {
            throw (RemoteException)var11;
         }

         this.extractAttachments(var10);

         try {
            return (WsObjectClassInfoReturn)var11;
         } catch (Exception var13) {
            return (WsObjectClassInfoReturn)JavaUtils.convert(var11, WsObjectClassInfoReturn.class);
         }
      } catch (AxisFault var14) {
         throw var14;
      }
   }

   @Override
   public WsClassStructureReturn getObjectStructures(
      String var1, String var2, String var3, String var4, WsLdapConnectionParams var5, String[] var6, boolean var7, WsKeyValuePairs var8
   ) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var9 = this.createCall();
      var9.setOperation(_operations[1]);
      var9.setUseSOAPAction(true);
      var9.setSOAPActionURI("getObjectStructures");
      var9.setEncodingStyle(null);
      var9.setProperty("sendXsiTypes", Boolean.FALSE);
      var9.setProperty("sendMultiRefs", Boolean.FALSE);
      var9.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var9.setOperationName(new QName("http://ldap.interchange.webservice.implementation.server.integration.etlforge.invalid", "getObjectStructures"));
      this.setRequestHeaders(var9);
      this.setAttachments(var9);

      try {
         Object var10 = var9.invoke(new Object[]{var1, var2, var3, var4, var5, var6, new Boolean(var7), var8});
         if (var10 instanceof RemoteException) {
            throw (RemoteException)var10;
         }

         this.extractAttachments(var9);

         try {
            return (WsClassStructureReturn)var10;
         } catch (Exception var12) {
            return (WsClassStructureReturn)JavaUtils.convert(var10, WsClassStructureReturn.class);
         }
      } catch (AxisFault var13) {
         throw var13;
      }
   }

   static {
      _initOperationDesc1();
   }
}
