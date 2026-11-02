package org.etlforge.integration.server.implementation.webservice.sfdc;

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
import org.etlforge.integration.server.implementation.webservice.WsCredentials;

public class Konga_salesforce_infoproviderSoapBindingStub extends Stub implements SalesforceInfoProvider {
   private final Vector cachedSerClasses = new Vector();
   private final Vector cachedSerQNames = new Vector();
   private final Vector cachedSerFactories = new Vector();
   private final Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[8];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("openConnection");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "etlforgeCredentials"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "orgId"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsOrgIdentifier"),
         WsOrgIdentifier.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsLoginResult"));
      var0.setReturnClass(WsLoginResult.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "openConnectionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("query");
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "etlforgeCredentials"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionData"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"),
         WsSessionData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "soql"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryResult"));
      var0.setReturnClass(WsQueryResult.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "queryReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("queryAll");
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "etlforgeCredentials"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionData"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"),
         WsSessionData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "soql"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryResult"));
      var0.setReturnClass(WsQueryResult.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "queryAllReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("describeObjects");
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "etlforgeCredentials"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionData"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"),
         WsSessionData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "objectNames"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsDescribeObjectsResult"));
      var0.setReturnClass(WsDescribeObjectsResult.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "describeObjectsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("queryAllToFile");
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"),
         WsSessionData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "in3"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSalesforceError"));
      var0.setReturnClass(WsSalesforceError.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "queryAllToFileReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
      var0 = new OperationDesc();
      var0.setName("listAllObjects");
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "etlforgeCredentials"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionData"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"),
         WsSessionData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsDescribeObjectsResult"));
      var0.setReturnClass(WsDescribeObjectsResult.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "listAllObjectsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[5] = var0;
      var0 = new OperationDesc();
      var0.setName("describeObject");
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "etlforgeCredentials"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionData"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"),
         WsSessionData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "objectName"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsDescribeObjectsResult"));
      var0.setReturnClass(WsDescribeObjectsResult.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "describeObjectReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[6] = var0;
      var0 = new OperationDesc();
      var0.setName("listAllObjects2");
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "etlforgeCredentials"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials"),
         WsCredentials.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "sessionData"),
         (byte)1,
         new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData"),
         WsSessionData.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSerializedObjectList"));
      var0.setReturnClass(WsSerializedObjectList.class);
      var0.setReturnQName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "listAllObjects2Return"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[7] = var0;
   }

   public Konga_salesforce_infoproviderSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_salesforce_infoproviderSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_salesforce_infoproviderSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      QName var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsPicklistEntry");
      this.cachedSerQNames.add(var3);
      var2 = WsPicklistEntry[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsPicklistEntry");
      var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsQueryField");
      this.cachedSerQNames.add(var3);
      var2 = WsQueryField[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryField");
      var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsQueryRecord");
      this.cachedSerQNames.add(var3);
      var2 = WsQueryRecord[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryRecord");
      var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsSObjectChild");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectChild[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectChild");
      var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsSObjectField");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectField[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectField");
      var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsSObjectParent");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectParent[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectParent");
      var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "ArrayOfWsSObjectSchema");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectSchema[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectSchema");
      var4 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsDescribeObjectsResult");
      this.cachedSerQNames.add(var3);
      var2 = WsDescribeObjectsResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsLoginResult");
      this.cachedSerQNames.add(var3);
      var2 = WsLoginResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsOrgIdentifier");
      this.cachedSerQNames.add(var3);
      var2 = WsOrgIdentifier.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsPicklistEntry");
      this.cachedSerQNames.add(var3);
      var2 = WsPicklistEntry.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryField");
      this.cachedSerQNames.add(var3);
      var2 = WsQueryField.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryRecord");
      this.cachedSerQNames.add(var3);
      var2 = WsQueryRecord.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsQueryResult");
      this.cachedSerQNames.add(var3);
      var2 = WsQueryResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSalesforceError");
      this.cachedSerQNames.add(var3);
      var2 = WsSalesforceError.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSerializedObjectList");
      this.cachedSerQNames.add(var3);
      var2 = WsSerializedObjectList.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSessionData");
      this.cachedSerQNames.add(var3);
      var2 = WsSessionData.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectChild");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectChild.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectField");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectField.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectParent");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectParent.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "WsSObjectSchema");
      this.cachedSerQNames.add(var3);
      var2 = WsSObjectSchema.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsCredentials");
      this.cachedSerQNames.add(var3);
      var2 = WsCredentials.class;
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
   public WsLoginResult openConnection(WsCredentials var1, WsOrgIdentifier var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[0]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("openConnection");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "openConnection"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsLoginResult)var4;
         } catch (Exception var6) {
            return (WsLoginResult)JavaUtils.convert(var4, WsLoginResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsQueryResult query(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[1]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("query");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "query"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsQueryResult)var5;
         } catch (Exception var7) {
            return (WsQueryResult)JavaUtils.convert(var5, WsQueryResult.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsQueryResult queryAll(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[2]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("queryAll");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "queryAll"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsQueryResult)var5;
         } catch (Exception var7) {
            return (WsQueryResult)JavaUtils.convert(var5, WsQueryResult.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsDescribeObjectsResult describeObjects(WsCredentials var1, WsSessionData var2, String[] var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[3]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("describeObjects");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "describeObjects"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsDescribeObjectsResult)var5;
         } catch (Exception var7) {
            return (WsDescribeObjectsResult)JavaUtils.convert(var5, WsDescribeObjectsResult.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsSalesforceError queryAllToFile(WsCredentials var1, WsSessionData var2, String var3, String var4) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var5 = this.createCall();
      var5.setOperation(_operations[4]);
      var5.setUseSOAPAction(true);
      var5.setSOAPActionURI("queryAllToFile");
      var5.setEncodingStyle(null);
      var5.setProperty("sendXsiTypes", Boolean.FALSE);
      var5.setProperty("sendMultiRefs", Boolean.FALSE);
      var5.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var5.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "queryAllToFile"));
      this.setRequestHeaders(var5);
      this.setAttachments(var5);

      try {
         Object var6 = var5.invoke(new Object[]{var1, var2, var3, var4});
         if (var6 instanceof RemoteException) {
            throw (RemoteException)var6;
         }

         this.extractAttachments(var5);

         try {
            return (WsSalesforceError)var6;
         } catch (Exception var8) {
            return (WsSalesforceError)JavaUtils.convert(var6, WsSalesforceError.class);
         }
      } catch (AxisFault var9) {
         throw var9;
      }
   }

   @Override
   public WsDescribeObjectsResult listAllObjects(WsCredentials var1, WsSessionData var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[5]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("listAllObjects");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "listAllObjects"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsDescribeObjectsResult)var4;
         } catch (Exception var6) {
            return (WsDescribeObjectsResult)JavaUtils.convert(var4, WsDescribeObjectsResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsDescribeObjectsResult describeObject(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[6]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("describeObject");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "describeObject"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, var3});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsDescribeObjectsResult)var5;
         } catch (Exception var7) {
            return (WsDescribeObjectsResult)JavaUtils.convert(var5, WsDescribeObjectsResult.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   @Override
   public WsSerializedObjectList listAllObjects2(WsCredentials var1, WsSessionData var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[7]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("listAllObjects2");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://sfdc.webservice.implementation.server.integration.etlforge.invalid", "listAllObjects2"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsSerializedObjectList)var4;
         } catch (Exception var6) {
            return (WsSerializedObjectList)JavaUtils.convert(var4, WsSerializedObjectList.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   static {
      _initOperationDesc1();
   }
}
