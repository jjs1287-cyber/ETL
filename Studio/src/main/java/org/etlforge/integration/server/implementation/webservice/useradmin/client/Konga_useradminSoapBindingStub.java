package org.etlforge.integration.server.implementation.webservice.useradmin.client;

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
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public class Konga_useradminSoapBindingStub extends Stub implements UserAdmin {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[5];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("getUsersAndGroups");
      ParameterDesc var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UsersAndGroups"));
      var0.setReturnClass(UsersAndGroups.class);
      var0.setReturnQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "getUsersAndGroupsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("getUsersAndGroupsWithOptions");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "options"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"),
         WsKeyValuePairs.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UsersAndGroups"));
      var0.setReturnClass(UsersAndGroups.class);
      var0.setReturnQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "getUsersAndGroupsWithOptionsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("setUsersAndGroups");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "usersAndGroups"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UsersAndGroups"),
         UsersAndGroups.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("setUserInfo");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "userInfo"),
         (byte)1,
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "User"),
         User.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("getUserInfo");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "User"));
      var0.setReturnClass(User.class);
      var0.setReturnQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "getUserInfoReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
   }

   public Konga_useradminSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_useradminSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_useradminSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValue");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = WsKeyValue.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValuePairs.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "ArrayOf_tns2_WsKeyValue");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValue[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValue");
      QName var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "ArrayOfGroup");
      this.cachedSerQNames.add(var3);
      var2 = Group[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Group");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "ArrayOfUser");
      this.cachedSerQNames.add(var3);
      var2 = User[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "User");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "ArrayOfUserGroup");
      this.cachedSerQNames.add(var3);
      var2 = UserGroup[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UserGroup");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "Group");
      this.cachedSerQNames.add(var3);
      var2 = Group.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "User");
      this.cachedSerQNames.add(var3);
      var2 = User.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UserGroup");
      this.cachedSerQNames.add(var3);
      var2 = UserGroup.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "UsersAndGroups");
      this.cachedSerQNames.add(var3);
      var2 = UsersAndGroups.class;
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
   public UsersAndGroups getUsersAndGroups(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[0]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("konga_useradmin#getUsersAndGroups");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "getUsersAndGroups"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (UsersAndGroups)var4;
         } catch (Exception var6) {
            return (UsersAndGroups)JavaUtils.convert(var4, UsersAndGroups.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public UsersAndGroups getUsersAndGroupsWithOptions(String var1, String var2, WsKeyValuePairs var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[1]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("konga_useradmin#getUsersAndGroupsWithOptions");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "getUsersAndGroupsWithOptions"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (UsersAndGroups)var5;
         } catch (Exception var7) {
            return (UsersAndGroups)JavaUtils.convert(var5, UsersAndGroups.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public void setUsersAndGroups(String var1, String var2, UsersAndGroups var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[2]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("konga_useradmin#setUsersAndGroups");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "setUsersAndGroups"));
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
   public void setUserInfo(String var1, String var2, User var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[3]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("konga_useradmin#setUserInfo");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "setUserInfo"));
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
   public User getUserInfo(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[4]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("getUserInfo");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.useradmin.UserAdmin", "getUserInfo"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (User)var4;
         } catch (Exception var6) {
            return (User)JavaUtils.convert(var4, User.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   static {
      _initOperationDesc1();
   }
}
