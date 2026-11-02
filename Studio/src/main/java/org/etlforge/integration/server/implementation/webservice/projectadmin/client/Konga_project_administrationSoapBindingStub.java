package org.etlforge.integration.server.implementation.webservice.projectadmin.client;

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

public class Konga_project_administrationSoapBindingStub extends Stub implements ProjectAdministration {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[7];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("DisableSchedules");
      ParameterDesc var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "schedules"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("EnableSchedules");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "schedules"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("DisableScheduleForOperation");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "operations"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("EnableScheduleForOperation");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "operations"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(XMLType.AXIS_VOID);
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("FindScheduledOperations");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "schedules"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "operations"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String[].class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ISO8601StartTime"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ISO8601EndTime"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "FindScheduledOperationsResult")
      );
      var0.setReturnClass(FindScheduledOperationsResult.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "FindScheduledOperationsReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
      var0 = new OperationDesc();
      var0.setName("TestScheduleWithKeyValues");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "scheduleProperties"),
         (byte)1,
         new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValuePairs"),
         WsKeyValuePairs.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "iso8601StartTime"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "iso8601EndTime"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "maxInstances"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "int"),
         int.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "FindScheduledOperationsResult")
      );
      var0.setReturnClass(FindScheduledOperationsResult.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "TestScheduleWithKeyValuesReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[5] = var0;
      var0 = new OperationDesc();
      var0.setName("TestScheduleWithGuid");
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "user"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "password"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "schedule"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "iso8601StartTime"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "iso8601EndTime"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "maxInstances"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "int"),
         int.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "FindScheduledOperationsResult")
      );
      var0.setReturnClass(FindScheduledOperationsResult.class);
      var0.setReturnQName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "TestScheduleWithGuidReturn")
      );
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[6] = var0;
   }

   public Konga_project_administrationSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_project_administrationSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_project_administrationSoapBindingStub(Service var1) throws AxisFault {
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
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ArrayOf_tns2_WsKeyValue");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValue[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://webservice.implementation.server.integration.etlforge.invalid", "WsKeyValue");
      QName var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ArrayOfScheduledOperation");
      this.cachedSerQNames.add(var3);
      var2 = ScheduledOperation[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ScheduledOperation");
      var4 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "FindScheduledOperationsResult");
      this.cachedSerQNames.add(var3);
      var2 = FindScheduledOperationsResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "ScheduledOperation");
      this.cachedSerQNames.add(var3);
      var2 = ScheduledOperation.class;
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
   public void disableSchedules(String var1, String var2, String[] var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[0]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("DisableSchedules");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "DisableSchedules"));
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
   public void enableSchedules(String var1, String var2, String[] var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[1]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("EnableSchedules");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "EnableSchedules"));
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
   public void disableScheduleForOperation(String var1, String var2, String[] var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[2]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("DisableScheduleForOperation");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "DisableScheduleForOperation")
      );
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
   public void enableScheduleForOperation(String var1, String var2, String[] var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[3]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("EnableScheduleForOperation");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "EnableScheduleForOperation")
      );
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
   public FindScheduledOperationsResult findScheduledOperations(String var1, String var2, String[] var3, String[] var4, String var5, String var6) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var7 = this.createCall();
      var7.setOperation(_operations[4]);
      var7.setUseSOAPAction(true);
      var7.setSOAPActionURI("FindScheduledOperations");
      var7.setEncodingStyle(null);
      var7.setProperty("sendXsiTypes", Boolean.FALSE);
      var7.setProperty("sendMultiRefs", Boolean.FALSE);
      var7.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var7.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "FindScheduledOperations")
      );
      this.setRequestHeaders(var7);
      this.setAttachments(var7);

      try {
         Object var8 = var7.invoke(new Object[]{var1, var2, var3, var4, var5, var6});
         if (var8 instanceof RemoteException) {
            throw (RemoteException)var8;
         }

         this.extractAttachments(var7);

         try {
            return (FindScheduledOperationsResult)var8;
         } catch (Exception var10) {
            return (FindScheduledOperationsResult)JavaUtils.convert(var8, FindScheduledOperationsResult.class);
         }
      } catch (AxisFault var11) {
         throw var11;
      }
   }

   @Override
   public FindScheduledOperationsResult testScheduleWithKeyValues(String var1, String var2, WsKeyValuePairs var3, String var4, String var5, int var6) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var7 = this.createCall();
      var7.setOperation(_operations[5]);
      var7.setUseSOAPAction(true);
      var7.setSOAPActionURI("TestScheduleWithKeyValues");
      var7.setEncodingStyle(null);
      var7.setProperty("sendXsiTypes", Boolean.FALSE);
      var7.setProperty("sendMultiRefs", Boolean.FALSE);
      var7.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var7.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "TestScheduleWithKeyValues")
      );
      this.setRequestHeaders(var7);
      this.setAttachments(var7);

      try {
         Object var8 = var7.invoke(new Object[]{var1, var2, var3, var4, var5, new Integer(var6)});
         if (var8 instanceof RemoteException) {
            throw (RemoteException)var8;
         }

         this.extractAttachments(var7);

         try {
            return (FindScheduledOperationsResult)var8;
         } catch (Exception var10) {
            return (FindScheduledOperationsResult)JavaUtils.convert(var8, FindScheduledOperationsResult.class);
         }
      } catch (AxisFault var11) {
         throw var11;
      }
   }

   @Override
   public FindScheduledOperationsResult testScheduleWithGuid(String var1, String var2, String var3, String var4, String var5, int var6) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var7 = this.createCall();
      var7.setOperation(_operations[6]);
      var7.setUseSOAPAction(true);
      var7.setSOAPActionURI("TestScheduleWithGuid");
      var7.setEncodingStyle(null);
      var7.setProperty("sendXsiTypes", Boolean.FALSE);
      var7.setProperty("sendMultiRefs", Boolean.FALSE);
      var7.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var7.setOperationName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.projectadmin.ProjectAdministration", "TestScheduleWithGuid")
      );
      this.setRequestHeaders(var7);
      this.setAttachments(var7);

      try {
         Object var8 = var7.invoke(new Object[]{var1, var2, var3, var4, var5, new Integer(var6)});
         if (var8 instanceof RemoteException) {
            throw (RemoteException)var8;
         }

         this.extractAttachments(var7);

         try {
            return (FindScheduledOperationsResult)var8;
         } catch (Exception var10) {
            return (FindScheduledOperationsResult)JavaUtils.convert(var8, FindScheduledOperationsResult.class);
         }
      } catch (AxisFault var11) {
         throw var11;
      }
   }

   static {
      _initOperationDesc1();
   }
}
