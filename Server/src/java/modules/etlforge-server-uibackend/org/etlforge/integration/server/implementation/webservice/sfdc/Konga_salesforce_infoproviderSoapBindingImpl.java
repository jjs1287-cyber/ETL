package org.etlforge.integration.server.implementation.webservice.sfdc;

import com.etlforge.integration.server.sfdc.TraceSettingsSupplier;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.etlforge.integration.dataelement.DataElementResolver;
import org.etlforge.integration.salesforce.ErrorCode;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.SalesforceVersion;
import org.etlforge.integration.salesforce.ServerHost;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.ExceptionTranslator;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.TraceSettings;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.WscPartnerConnectionFactory;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.WscPartnerConnectionQuery;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.WscPartnerConnectionSchemaProvider;
import org.etlforge.integration.salesforce.wsdl.partner.wsc.WscUtils;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.conf.PreconfiguredDataElements;
import org.etlforge.integration.server.db.trandb.ProxySettingsSupplier;
import org.etlforge.integration.server.implementation.webservice.WsCredentials;
import org.etlforge.integration.server.ws.WsUtils;
import org.etlforge.util.zip.ZipUtils;

public final class Konga_salesforce_infoproviderSoapBindingImpl implements SalesforceInfoProvider {
   private static final TraceSettingsSupplier traceSettingsSupplier = TraceSettingsSupplier.defaultSupplier();

   @Override
   public WsLoginResult openConnection(WsCredentials var1, WsOrgIdentifier var2) throws RemoteException {
      ExternalApiSupport var3 = null;

      try {
         var3 = ExternalApiSupport.forCall(var1.getUser(), var1.getMd5Pwd());
         Connection var4 = var3.getTranDbConnection();
         WscPartnerConnectionFactory var5 = new WscPartnerConnectionFactory();
         var5.setProxySettingsSupplier(new ProxySettingsSupplier(var4));
         var5.setTraceSettingsSupplier(traceSettingsSupplier);
         OrgIdentifier var6 = getOrgId(var2);
         SalesforceConnection var7 = var5.newConnection(var6);

         try {
            var7.open();
            String var8 = var7.getSessionId();
            String var22 = var7.getUserId();
            String var23 = var7.getServiceEndpoint();
            int var11 = var7.getSessionTimeoutInSeconds();
            WsSessionData var12 = new WsSessionData(var8, var22, var23, var11);
            WsSalesforceError var13 = new WsSalesforceError();
            return new WsLoginResult(var12, var13);
         } catch (SalesforceException var19) {
            var19.printStackTrace();
            WsSessionData var9 = new WsSessionData();
            WsSalesforceError var10 = new WsSalesforceError(var19.getMessage(), var19.getErrorCodeString());
            return new WsLoginResult(var9, var10);
         }
      } catch (Exception var20) {
         WsUtils.convertError(var20, this.getClass(), "openConnection");
      } finally {
         ExternalApiSupport.safeDispose(var3);
      }

      return null;
   }

   private static OrgIdentifier getOrgId(WsOrgIdentifier var0) {
      DataElementResolver var1 = new DataElementResolver();
      var1.setContext(PreconfiguredDataElements.getContext());
      ServerHost var2 = ServerHost.fromUrl(var1.resolve(var0.getUrl()));
      SalesforceVersion var3 = SalesforceVersion.fromId(var0.getSalesforceVersion());
      LoginCredentials var4 = new LoginCredentials(var1.resolve(var0.getUsername()), var1.resolve(var0.getPassword()), var1.resolve(var0.getSecurityToken()), false, var0.getAuthType(), var1.resolve(var0.getInstanceUrl()), var1.resolve(var0.getClientId()), var1.resolve(var0.getClientSecret()));
      return new OrgIdentifier(var2, var3, var4);
   }

   @Override
   public WsDescribeObjectsResult listAllObjects(WsCredentials var1, WsSessionData var2) throws RemoteException {
      Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall var3 = new Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall("listAllObjects", var1, var2) {
         @Override
         protected List<SObjectSchema> makeCall(WscPartnerConnectionSchemaProvider var1) throws ConnectionException {
            return var1.listAllObjects();
         }
      };
      return var3.run();
   }

   @Override
   public WsSerializedObjectList listAllObjects2(WsCredentials var1, WsSessionData var2) throws RemoteException {
      Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall var3 = new Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall("listAllObjects", var1, var2) {
         @Override
         protected List<SObjectSchema> makeCall(WscPartnerConnectionSchemaProvider var1) throws ConnectionException {
            return var1.listAllObjects();
         }
      };
      return var3.run2();
   }

   @Override
   public WsDescribeObjectsResult describeObjects(WsCredentials var1, WsSessionData var2, final String[] var3) throws RemoteException {
      Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall var4 = new Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall("describeObjects", var1, var2) {
         @Override
         protected List<SObjectSchema> makeCall(WscPartnerConnectionSchemaProvider var1) throws ConnectionException {
            return var1.describeObjects(Arrays.asList(var3));
         }
      };
      return var4.run();
   }

   @Override
   public WsDescribeObjectsResult describeObject(WsCredentials var1, WsSessionData var2, final String var3) throws RemoteException {
      Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall var4 = new Konga_salesforce_infoproviderSoapBindingImpl.SchemaCall("describeObject", var1, var2) {
         @Override
         protected List<SObjectSchema> makeCall(WscPartnerConnectionSchemaProvider var1) throws ConnectionException {
            SObjectSchema var2x = var1.describeObject(var3);
            return Arrays.asList(var2x);
         }
      };
      return var4.run();
   }

   @Override
   public WsQueryResult query(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException {
      Konga_salesforce_infoproviderSoapBindingImpl.QueryCall var4 = new Konga_salesforce_infoproviderSoapBindingImpl.QueryCall("query", var1, var2, var3) {
         @Override
         protected QueryResult makeCall(WscPartnerConnectionQuery var1, String var2x) throws ConnectionException {
            return var1.query(var2x);
         }
      };
      return var4.run();
   }

   @Override
   public WsQueryResult queryAll(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException {
      Konga_salesforce_infoproviderSoapBindingImpl.QueryCall var4 = new Konga_salesforce_infoproviderSoapBindingImpl.QueryCall("queryAll", var1, var2, var3) {
         @Override
         protected QueryResult makeCall(WscPartnerConnectionQuery var1, String var2x) throws ConnectionException {
            return var1.queryAll(var2x);
         }
      };
      return var4.run();
   }

   @Override
   public WsSalesforceError queryAllToFile(WsCredentials var1, WsSessionData var2, String var3, String var4) throws RemoteException {
      Konga_salesforce_infoproviderSoapBindingImpl.QueryAllToFileCall var5 = new Konga_salesforce_infoproviderSoapBindingImpl.QueryAllToFileCall(
         "queryAllToFile", var1, var2, var3, var4
      ) {
         @Override
         protected int makeCall(WscPartnerConnectionQuery var1, String var2x, String var3x) throws ConnectionException, Exception {
            return var1.queryAllToFile(var2x, var3x);
         }
      };
      return var5.run();
   }

   private static PartnerConnection createPartnerConnection(WsSessionData var0, ProxySettingsSupplier var1) throws SalesforceException {
      try {
         TraceSettings var2 = traceSettingsSupplier.get();
         return WscUtils.createConnectionObjectForEstablishedConnection(var0.getEndpoint(), var0.getSessionId(), var1, var2);
      } catch (ConnectionException var3) {
         throw ExceptionTranslator.translate(var3);
      }
   }

   private static WsSalesforceError createError(ConnectionException var0) {
      SalesforceException var1 = ExceptionTranslator.translate(var0);
      return new WsSalesforceError(var1.getMessage(), var1.getErrorCodeString());
   }

   private abstract static class QueryAllToFileCall {
      private final String methodName;
      private final WsCredentials etlforgeCredentials;
      private final WsSessionData sessionData;
      private final String soql;
      private final String fileName;

      protected QueryAllToFileCall(String var1, WsCredentials var2, WsSessionData var3, String var4, String var5) {
         this.methodName = var1;
         this.etlforgeCredentials = var2;
         this.sessionData = var3;
         this.soql = var4;
         this.fileName = var5;
      }

      protected abstract int makeCall(WscPartnerConnectionQuery var1, String var2, String var3) throws ConnectionException, Exception;

      public final WsSalesforceError run() throws RemoteException {
         ExternalApiSupport var1 = null;

         try {
            var1 = ExternalApiSupport.forCall(this.etlforgeCredentials.getUser(), this.etlforgeCredentials.getMd5Pwd());
            Connection var2 = var1.getTranDbConnection();
            ProxySettingsSupplier var3 = new ProxySettingsSupplier(var2);

            try {
               WscPartnerConnectionQuery var4 = createQueryRunner(this.sessionData, var3);
               int var5 = this.makeCall(var4, this.soql, this.fileName);
               return new WsSalesforceError(Integer.toString(var5), "");
            } catch (ConnectionException var11) {
               return new WsSalesforceError(var11.getMessage(), ErrorCode.QUERYALL_TO_FILE_ERROR.toString());
            }
         } catch (Exception var12) {
            WsUtils.convertError(var12, this.getClass(), this.methodName);
         } finally {
            ExternalApiSupport.safeDispose(var1);
         }

         return null;
      }

      private static WscPartnerConnectionQuery createQueryRunner(WsSessionData var0, ProxySettingsSupplier var1) throws SalesforceException {
         PartnerConnection var2 = Konga_salesforce_infoproviderSoapBindingImpl.createPartnerConnection(var0, var1);
         return new WscPartnerConnectionQuery(var2);
      }
   }

   private abstract static class QueryCall {
      private final String methodName;
      private final WsCredentials etlforgeCredentials;
      private final WsSessionData sessionData;
      private final String soql;

      protected QueryCall(String var1, WsCredentials var2, WsSessionData var3, String var4) {
         this.methodName = var1;
         this.etlforgeCredentials = var2;
         this.sessionData = var3;
         this.soql = var4;
      }

      protected abstract QueryResult makeCall(WscPartnerConnectionQuery var1, String var2) throws ConnectionException;

      public final WsQueryResult run() throws RemoteException {
         ExternalApiSupport var1 = null;

         try {
            var1 = ExternalApiSupport.forCall(this.etlforgeCredentials.getUser(), this.etlforgeCredentials.getMd5Pwd());
            Connection var2 = var1.getTranDbConnection();
            ProxySettingsSupplier var3 = new ProxySettingsSupplier(var2);

            try {
               WscPartnerConnectionQuery var4 = createQueryRunner(this.sessionData, var3);
               QueryResult var5 = this.makeCall(var4, this.soql);
               return QueryResultSerializer.serialize(var5);
            } catch (ConnectionException var12) {
               return translateQueryError(var12);
            }
         } catch (Exception var13) {
            WsUtils.convertError(var13, this.getClass(), this.methodName);
         } finally {
            ExternalApiSupport.safeDispose(var1);
         }

         return null;
      }

      private static WscPartnerConnectionQuery createQueryRunner(WsSessionData var0, ProxySettingsSupplier var1) throws SalesforceException {
         PartnerConnection var2 = Konga_salesforce_infoproviderSoapBindingImpl.createPartnerConnection(var0, var1);
         return new WscPartnerConnectionQuery(var2);
      }

      private static WsQueryResult translateQueryError(ConnectionException var0) {
         WsSalesforceError var1 = Konga_salesforce_infoproviderSoapBindingImpl.createError(var0);
         WsQueryRecord[] var2 = new WsQueryRecord[0];
         return new WsQueryResult(true, var2, var1);
      }
   }

   private abstract static class SchemaCall {
      private final String methodName;
      private final WsCredentials etlforgeCredentials;
      private final WsSessionData sessionData;

      protected SchemaCall(String var1, WsCredentials var2, WsSessionData var3) {
         this.methodName = var1;
         this.etlforgeCredentials = var2;
         this.sessionData = var3;
      }

      protected abstract List<SObjectSchema> makeCall(WscPartnerConnectionSchemaProvider var1) throws ConnectionException;

      public final WsDescribeObjectsResult run() throws RemoteException {
         ExternalApiSupport var1 = null;

         try {
            var1 = ExternalApiSupport.forCall(this.etlforgeCredentials.getUser(), this.etlforgeCredentials.getMd5Pwd());
            Connection var2 = var1.getTranDbConnection();
            ProxySettingsSupplier var3 = new ProxySettingsSupplier(var2);

            try {
               WscPartnerConnectionSchemaProvider var4 = createSchemaProvider(this.sessionData, var3);
               List var5 = this.makeCall(var4);
               return serializeSchemas(var5);
            } catch (ConnectionException var11) {
               return translateSchemaProviderError(var11);
            }
         } catch (Exception var12) {
            WsUtils.convertError(var12, this.getClass(), this.methodName);
         } finally {
            ExternalApiSupport.safeDispose(var1);
         }

         return null;
      }

      public final WsSerializedObjectList run2() throws RemoteException {
         ExternalApiSupport var1 = null;

         try {
            var1 = ExternalApiSupport.forCall(this.etlforgeCredentials.getUser(), this.etlforgeCredentials.getMd5Pwd());
            Connection var2 = var1.getTranDbConnection();
            ProxySettingsSupplier var3 = new ProxySettingsSupplier(var2);

            try {
               WscPartnerConnectionSchemaProvider var4 = createSchemaProvider(this.sessionData, var3);
               List var5 = this.makeCall(var4);
               return serializeSchemas2(var5);
            } catch (ConnectionException var11) {
               return translateSchemaProviderError2(var11);
            }
         } catch (Exception var12) {
            WsUtils.convertError(var12, this.getClass(), this.methodName);
         } finally {
            ExternalApiSupport.safeDispose(var1);
         }

         return null;
      }

      private static WscPartnerConnectionSchemaProvider createSchemaProvider(WsSessionData var0, ProxySettingsSupplier var1) throws SalesforceException {
         PartnerConnection var2 = Konga_salesforce_infoproviderSoapBindingImpl.createPartnerConnection(var0, var1);
         return new WscPartnerConnectionSchemaProvider(var2);
      }

      private static WsDescribeObjectsResult serializeSchemas(List<SObjectSchema> var0) {
         WsSObjectSchema[] var1 = SObjectSchemaSerializer.serialize(var0);
         WsSalesforceError var2 = new WsSalesforceError();
         return new WsDescribeObjectsResult(var1, var2);
      }

      private static WsSerializedObjectList serializeSchemas2(List<SObjectSchema> var0) {
         try {
            ArrayList var1 = new ArrayList(var0);
            byte[] var2 = SerializationUtils.serialize(var1);
            byte[] var3 = ZipUtils.deflateInputStream(new ByteArrayInputStream(var2));
            String var4 = Base64.encodeBase64String(var3);
            WsSalesforceError var5 = new WsSalesforceError();
            return new WsSerializedObjectList(var4, var5);
         } catch (IOException var6) {
            throw new RuntimeException("Will not happen since we're reading directly from memory.");
         }
      }

      private static WsDescribeObjectsResult translateSchemaProviderError(ConnectionException var0) {
         WsSObjectSchema[] var1 = new WsSObjectSchema[0];
         WsSalesforceError var2 = Konga_salesforce_infoproviderSoapBindingImpl.createError(var0);
         return new WsDescribeObjectsResult(var1, var2);
      }

      private static WsSerializedObjectList translateSchemaProviderError2(ConnectionException var0) {
         String var1 = "";
         WsSalesforceError var2 = Konga_salesforce_infoproviderSoapBindingImpl.createError(var0);
         return new WsSerializedObjectList(var1, var2);
      }
   }
}
