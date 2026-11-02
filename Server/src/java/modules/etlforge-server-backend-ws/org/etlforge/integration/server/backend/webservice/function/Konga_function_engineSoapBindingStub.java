package org.etlforge.integration.server.backend.webservice.function;

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

public class Konga_function_engineSoapBindingStub extends Stub implements FunctionEngine {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[9];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("sendEmail");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsSendEmailInput"),
         WsSendEmailInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsSendEmailOutput"));
      var0.setReturnClass(WsSendEmailOutput.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "sendEmailReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("testFunction");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsTestFunctionInput"),
         WsTestFunctionInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsTestFunctionOutput"));
      var0.setReturnClass(WsTestFunctionOutput.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "testFunctionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("fromDataLoaderTime");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromDataLoaderDateInput"),
         FromDataLoaderDateInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFromDataLoaderDateOutput"));
      var0.setReturnClass(WsFromDataLoaderDateOutput.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "fromDataLoaderTimeReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("fromDataLoaderDate");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromDataLoaderDateInput"),
         FromDataLoaderDateInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFromDataLoaderDateOutput"));
      var0.setReturnClass(WsFromDataLoaderDateOutput.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "fromDataLoaderDateReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("fromDataLoaderDateTime");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromDataLoaderDateInput"),
         FromDataLoaderDateInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFromDataLoaderDateOutput"));
      var0.setReturnClass(WsFromDataLoaderDateOutput.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "fromDataLoaderDateTimeReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
      var0 = new OperationDesc();
      var0.setName("startNewFunctionSession");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsStartNewFunctionSessionResult"));
      var0.setReturnClass(WsStartNewFunctionSessionResult.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "startNewFunctionSessionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[5] = var0;
      var0 = new OperationDesc();
      var0.setName("getSalesforceServerTimestamp");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsGetSalesforceServerTimeStampInput"),
         WsGetSalesforceServerTimeStampInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsGetSalesforceServerTimeStampResult"));
      var0.setReturnClass(WsGetSalesforceServerTimeStampResult.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "getSalesforceServerTimestampReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[6] = var0;
      var0 = new OperationDesc();
      var0.setName("closeFunctionSession");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionError"));
      var0.setReturnClass(WsFunctionError.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "closeFunctionSessionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[7] = var0;
      var0 = new OperationDesc();
      var0.setName("convertTimeZone");
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId"),
         WsFunctionSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsConvertTimeZoneInput"),
         WsConvertTimeZoneInput.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsConvertTimeZoneOutput"));
      var0.setReturnClass(WsConvertTimeZoneOutput.class);
      var0.setReturnQName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "convertTimeZoneReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[8] = var0;
   }

   public Konga_function_engineSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_function_engineSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_function_engineSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      QName var4 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "FromDataLoaderDateInput");
      this.cachedSerQNames.add(var3);
      var2 = FromDataLoaderDateInput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsConvertTimeZoneInput");
      this.cachedSerQNames.add(var3);
      var2 = WsConvertTimeZoneInput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsConvertTimeZoneOutput");
      this.cachedSerQNames.add(var3);
      var2 = WsConvertTimeZoneOutput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFromDataLoaderDateOutput");
      this.cachedSerQNames.add(var3);
      var2 = WsFromDataLoaderDateOutput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionError");
      this.cachedSerQNames.add(var3);
      var2 = WsFunctionError.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsFunctionSessionId");
      this.cachedSerQNames.add(var3);
      var2 = WsFunctionSessionId.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsGetSalesforceServerTimeStampInput");
      this.cachedSerQNames.add(var3);
      var2 = WsGetSalesforceServerTimeStampInput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsGetSalesforceServerTimeStampResult");
      this.cachedSerQNames.add(var3);
      var2 = WsGetSalesforceServerTimeStampResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsSendEmailInput");
      this.cachedSerQNames.add(var3);
      var2 = WsSendEmailInput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsSendEmailOutput");
      this.cachedSerQNames.add(var3);
      var2 = WsSendEmailOutput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsStartNewFunctionSessionResult");
      this.cachedSerQNames.add(var3);
      var2 = WsStartNewFunctionSessionResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsTestFunctionInput");
      this.cachedSerQNames.add(var3);
      var2 = WsTestFunctionInput.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "WsTestFunctionOutput");
      this.cachedSerQNames.add(var3);
      var2 = WsTestFunctionOutput.class;
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
   public WsSendEmailOutput sendEmail(WsFunctionSessionId var1, WsSendEmailInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[0]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("sendEmail");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "sendEmail"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsSendEmailOutput)var4;
         } catch (Exception var6) {
            return (WsSendEmailOutput)JavaUtils.convert(var4, WsSendEmailOutput.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsTestFunctionOutput testFunction(WsFunctionSessionId var1, WsTestFunctionInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[1]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("testFunction");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "testFunction"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsTestFunctionOutput)var4;
         } catch (Exception var6) {
            return (WsTestFunctionOutput)JavaUtils.convert(var4, WsTestFunctionOutput.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsFromDataLoaderDateOutput fromDataLoaderTime(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[2]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("fromDataLoaderTime");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "fromDataLoaderTime"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsFromDataLoaderDateOutput)var4;
         } catch (Exception var6) {
            return (WsFromDataLoaderDateOutput)JavaUtils.convert(var4, WsFromDataLoaderDateOutput.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsFromDataLoaderDateOutput fromDataLoaderDate(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[3]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("fromDataLoaderDate");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "fromDataLoaderDate"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsFromDataLoaderDateOutput)var4;
         } catch (Exception var6) {
            return (WsFromDataLoaderDateOutput)JavaUtils.convert(var4, WsFromDataLoaderDateOutput.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsFromDataLoaderDateOutput fromDataLoaderDateTime(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[4]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("fromDataLoaderDateTime");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "fromDataLoaderDateTime"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsFromDataLoaderDateOutput)var4;
         } catch (Exception var6) {
            return (WsFromDataLoaderDateOutput)JavaUtils.convert(var4, WsFromDataLoaderDateOutput.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsStartNewFunctionSessionResult startNewFunctionSession(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[5]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("startNewFunctionSession");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "startNewFunctionSession"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsStartNewFunctionSessionResult)var4;
         } catch (Exception var6) {
            return (WsStartNewFunctionSessionResult)JavaUtils.convert(var4, WsStartNewFunctionSessionResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsGetSalesforceServerTimeStampResult getSalesforceServerTimestamp(WsFunctionSessionId var1, WsGetSalesforceServerTimeStampInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[6]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("getSalesforceServerTimestamp");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "getSalesforceServerTimestamp"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsGetSalesforceServerTimeStampResult)var4;
         } catch (Exception var6) {
            return (WsGetSalesforceServerTimeStampResult)JavaUtils.convert(var4, WsGetSalesforceServerTimeStampResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsFunctionError closeFunctionSession(WsFunctionSessionId var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[7]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("closeFunctionSession");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "closeFunctionSession"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsFunctionError)var3;
         } catch (Exception var5) {
            return (WsFunctionError)JavaUtils.convert(var3, WsFunctionError.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsConvertTimeZoneOutput convertTimeZone(WsFunctionSessionId var1, WsConvertTimeZoneInput var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[8]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("convertTimeZone");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://function.webservice.backend.server.integration.etlforge.invalid", "convertTimeZone"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsConvertTimeZoneOutput)var4;
         } catch (Exception var6) {
            return (WsConvertTimeZoneOutput)JavaUtils.convert(var4, WsConvertTimeZoneOutput.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   static {
      _initOperationDesc1();
   }
}
