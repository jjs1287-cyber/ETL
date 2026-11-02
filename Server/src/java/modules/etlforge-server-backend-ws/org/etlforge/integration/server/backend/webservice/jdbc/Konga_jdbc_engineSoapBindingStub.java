package org.etlforge.integration.server.backend.webservice.jdbc;

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

public class Konga_jdbc_engineSoapBindingStub extends Stub implements JdbcEngine {
   private Vector cachedSerClasses = new Vector();
   private Vector cachedSerQNames = new Vector();
   private Vector cachedSerFactories = new Vector();
   private Vector cachedDeserFactories = new Vector();
   static OperationDesc[] _operations = new OperationDesc[10];

   private static void _initOperationDesc1() {
      OperationDesc var0 = new OperationDesc();
      var0.setName("closeSession");
      ParameterDesc var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcError"));
      var0.setReturnClass(WsJdbcError.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "closeSessionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[0] = var0;
      var0 = new OperationDesc();
      var0.setName("rollbackTransactions");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcError"));
      var0.setReturnClass(WsJdbcError.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "rollbackTransactionsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[1] = var0;
      var0 = new OperationDesc();
      var0.setName("callStoredProcedure");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPParams"),
         WsSPParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPResult"));
      var0.setReturnClass(WsSPResult.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "callStoredProcedureReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[2] = var0;
      var0 = new OperationDesc();
      var0.setName("startNewSession");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "string"),
         String.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcStartNewSessionResult"));
      var0.setReturnClass(WsJdbcStartNewSessionResult.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "startNewSessionReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[3] = var0;
      var0 = new OperationDesc();
      var0.setName("commitTransactions");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcError"));
      var0.setReturnClass(WsJdbcError.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "commitTransactionsReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[4] = var0;
      var0 = new OperationDesc();
      var0.setName("cachedDbLookup");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbLookupParams"),
         WsDbLookupParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbLookupResult"));
      var0.setReturnClass(WsDbLookupResult.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "cachedDbLookupReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[5] = var0;
      var0 = new OperationDesc();
      var0.setName("readSource");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsReadSourceParams"),
         WsReadSourceParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcError"));
      var0.setReturnClass(WsJdbcError.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "readSourceReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[6] = var0;
      var0 = new OperationDesc();
      var0.setName("dbLookup");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbLookupParams"),
         WsDbLookupParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbLookupResult"));
      var0.setReturnClass(WsDbLookupResult.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "dbLookupReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[7] = var0;
      var0 = new OperationDesc();
      var0.setName("writeTarget");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsWriteTargetParams"),
         WsWriteTargetParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcError"));
      var0.setReturnClass(WsJdbcError.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "writeTargetReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[8] = var0;
      var0 = new OperationDesc();
      var0.setName("dbExecute");
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in0"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId"),
         WsJdbcSessionId.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in1"),
         (byte)1,
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbLookupParams"),
         WsDbLookupParams.class,
         false,
         false
      );
      var0.addParameter(var1);
      var1 = new ParameterDesc(
         new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "in2"),
         (byte)1,
         new QName("http://www.w3.org/2001/XMLSchema", "int"),
         int.class,
         false,
         false
      );
      var0.addParameter(var1);
      var0.setReturnType(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbExecuteResult"));
      var0.setReturnClass(WsDbExecuteResult.class);
      var0.setReturnQName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "dbExecuteReturn"));
      var0.setStyle(Style.WRAPPED);
      var0.setUse(Use.LITERAL);
      _operations[9] = var0;
   }

   public Konga_jdbc_engineSoapBindingStub() throws AxisFault {
      this(null);
   }

   public Konga_jdbc_engineSoapBindingStub(URL var1, Service var2) throws AxisFault {
      this(var2);
      super.cachedEndpoint = var1;
   }

   public Konga_jdbc_engineSoapBindingStub(Service var1) throws AxisFault {
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
      QName var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "ArrayOf_xsd_string");
      this.cachedSerQNames.add(var3);
      Class<?> var2 = String[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://www.w3.org/2001/XMLSchema", "string");
      QName var4 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsKeyValue");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValue[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsKeyValue");
      var4 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsResultSetRow");
      this.cachedSerQNames.add(var3);
      var2 = WsResultSetRow[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsResultSetRow");
      var4 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "ArrayOfWsSPParameter");
      this.cachedSerQNames.add(var3);
      var2 = WsSPParameter[].class;
      this.cachedSerClasses.add(var2);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPParameter");
      var4 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "item");
      this.cachedSerFactories.add(new ArraySerializerFactory(var3, var4));
      this.cachedDeserFactories.add(new ArrayDeserializerFactory());
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsConnectionParams");
      this.cachedSerQNames.add(var3);
      var2 = WsConnectionParams.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbExecuteResult");
      this.cachedSerQNames.add(var3);
      var2 = WsDbExecuteResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbLookupParams");
      this.cachedSerQNames.add(var3);
      var2 = WsDbLookupParams.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsDbLookupResult");
      this.cachedSerQNames.add(var3);
      var2 = WsDbLookupResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcError");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcError.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcSessionId");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcSessionId.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsJdbcStartNewSessionResult");
      this.cachedSerQNames.add(var3);
      var2 = WsJdbcStartNewSessionResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsKeyValue");
      this.cachedSerQNames.add(var3);
      var2 = WsKeyValue.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsReadSourceParams");
      this.cachedSerQNames.add(var3);
      var2 = WsReadSourceParams.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsResultSet");
      this.cachedSerQNames.add(var3);
      var2 = WsResultSet.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsResultSetRow");
      this.cachedSerQNames.add(var3);
      var2 = WsResultSetRow.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPParameter");
      this.cachedSerQNames.add(var3);
      var2 = WsSPParameter.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPParams");
      this.cachedSerQNames.add(var3);
      var2 = WsSPParams.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsSPResult");
      this.cachedSerQNames.add(var3);
      var2 = WsSPResult.class;
      this.cachedSerClasses.add(var2);
      this.cachedSerFactories.add(var5);
      this.cachedDeserFactories.add(var6);
      var3 = new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "WsWriteTargetParams");
      this.cachedSerQNames.add(var3);
      var2 = WsWriteTargetParams.class;
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
   public WsJdbcError closeSession(WsJdbcSessionId var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[0]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("closeSession");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "closeSession"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsJdbcError)var3;
         } catch (Exception var5) {
            return (WsJdbcError)JavaUtils.convert(var3, WsJdbcError.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsJdbcError rollbackTransactions(WsJdbcSessionId var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[1]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("rollbackTransactions");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "rollbackTransactions"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsJdbcError)var3;
         } catch (Exception var5) {
            return (WsJdbcError)JavaUtils.convert(var3, WsJdbcError.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsSPResult callStoredProcedure(WsJdbcSessionId var1, WsSPParams var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[2]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("callStoredProcedure");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "callStoredProcedure"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsSPResult)var4;
         } catch (Exception var6) {
            return (WsSPResult)JavaUtils.convert(var4, WsSPResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsJdbcStartNewSessionResult startNewSession(String var1, String var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[3]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("startNewSession");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "startNewSession"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsJdbcStartNewSessionResult)var4;
         } catch (Exception var6) {
            return (WsJdbcStartNewSessionResult)JavaUtils.convert(var4, WsJdbcStartNewSessionResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsJdbcError commitTransactions(WsJdbcSessionId var1) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var2 = this.createCall();
      var2.setOperation(_operations[4]);
      var2.setUseSOAPAction(true);
      var2.setSOAPActionURI("commitTransactions");
      var2.setEncodingStyle(null);
      var2.setProperty("sendXsiTypes", Boolean.FALSE);
      var2.setProperty("sendMultiRefs", Boolean.FALSE);
      var2.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var2.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "commitTransactions"));
      this.setRequestHeaders(var2);
      this.setAttachments(var2);

      try {
         Object var3 = var2.invoke(new Object[]{var1});
         if (var3 instanceof RemoteException) {
            throw (RemoteException)var3;
         }

         this.extractAttachments(var2);

         try {
            return (WsJdbcError)var3;
         } catch (Exception var5) {
            return (WsJdbcError)JavaUtils.convert(var3, WsJdbcError.class);
         }
      } catch (AxisFault var6) {
         throw var6;
      }
   }

   @Override
   public WsDbLookupResult cachedDbLookup(WsJdbcSessionId var1, WsDbLookupParams var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[5]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("cachedDbLookup");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "cachedDbLookup"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsDbLookupResult)var4;
         } catch (Exception var6) {
            return (WsDbLookupResult)JavaUtils.convert(var4, WsDbLookupResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsJdbcError readSource(WsJdbcSessionId var1, WsReadSourceParams var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[6]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("readSource");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "readSource"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsJdbcError)var4;
         } catch (Exception var6) {
            return (WsJdbcError)JavaUtils.convert(var4, WsJdbcError.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsDbLookupResult dbLookup(WsJdbcSessionId var1, WsDbLookupParams var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[7]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("dbLookup");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "dbLookup"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsDbLookupResult)var4;
         } catch (Exception var6) {
            return (WsDbLookupResult)JavaUtils.convert(var4, WsDbLookupResult.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsJdbcError writeTarget(WsJdbcSessionId var1, WsWriteTargetParams var2) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var3 = this.createCall();
      var3.setOperation(_operations[8]);
      var3.setUseSOAPAction(true);
      var3.setSOAPActionURI("writeTarget");
      var3.setEncodingStyle(null);
      var3.setProperty("sendXsiTypes", Boolean.FALSE);
      var3.setProperty("sendMultiRefs", Boolean.FALSE);
      var3.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var3.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "writeTarget"));
      this.setRequestHeaders(var3);
      this.setAttachments(var3);

      try {
         Object var4 = var3.invoke(new Object[]{var1, var2});
         if (var4 instanceof RemoteException) {
            throw (RemoteException)var4;
         }

         this.extractAttachments(var3);

         try {
            return (WsJdbcError)var4;
         } catch (Exception var6) {
            return (WsJdbcError)JavaUtils.convert(var4, WsJdbcError.class);
         }
      } catch (AxisFault var7) {
         throw var7;
      }
   }

   @Override
   public WsDbExecuteResult dbExecute(WsJdbcSessionId var1, WsDbLookupParams var2, int var3) throws RemoteException {
      if (super.cachedEndpoint == null) {
         throw new NoEndPointException();
      }

      Call var4 = this.createCall();
      var4.setOperation(_operations[9]);
      var4.setUseSOAPAction(true);
      var4.setSOAPActionURI("dbExecute");
      var4.setEncodingStyle(null);
      var4.setProperty("sendXsiTypes", Boolean.FALSE);
      var4.setProperty("sendMultiRefs", Boolean.FALSE);
      var4.setSOAPVersion(SOAPConstants.SOAP11_CONSTANTS);
      var4.setOperationName(new QName("http://jdbc.webservice.backend.server.integration.etlforge.invalid", "dbExecute"));
      this.setRequestHeaders(var4);
      this.setAttachments(var4);

      try {
         Object var5 = var4.invoke(new Object[]{var1, var2, new Integer(var3)});
         if (var5 instanceof RemoteException) {
            throw (RemoteException)var5;
         }

         this.extractAttachments(var4);

         try {
            return (WsDbExecuteResult)var5;
         } catch (Exception var7) {
            return (WsDbExecuteResult)JavaUtils.convert(var5, WsDbExecuteResult.class);
         }
      } catch (AxisFault var8) {
         throw var8;
      }
   }

   static {
      _initOperationDesc1();
   }
}
