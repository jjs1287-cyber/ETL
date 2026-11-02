package org.etlforge.integration.server.implementation.webservice.sfdc.impl;

import com.google.common.base.Strings;
import com.etlforge.integration.client.sfdc.server.DescribeMultipleSObjectsCallback;
import com.etlforge.integration.client.sfdc.server.DescribeSObjectCallback;
import com.etlforge.integration.client.sfdc.server.OpenConnectionCallback;
import com.etlforge.integration.client.sfdc.server.QueryCallback;
import com.etlforge.integration.client.sfdc.server.SalesforceInfoProvider;
import com.etlforge.integration.client.sfdc.server.SessionData;
import java.rmi.RemoteException;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsCredentials;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsDescribeObjectsResult;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsLoginResult;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsOrgIdentifier;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsQueryResult;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSObjectSchema;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSalesforceError;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSerializedObjectList;
import org.etlforge.integration.server.implementation.webservice.sfdc.client.WsSessionData;
import org.etlforge.util.zip.ZipUtils;

public final class SalesforceInfoProviderWsImpl extends JavaWebServiceServerCall implements SalesforceInfoProvider {
   private static final boolean USE_NEW_API = false;

   public SalesforceInfoProviderWsImpl(JavaWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void openConnection(OrgIdentifier var1, OpenConnectionCallback var2) {
      SalesforceInfoProviderWsImpl.Worker var3 = new SalesforceInfoProviderWsImpl.Worker(this.getConfiguration());
      var3.openConnection(var1, var2);
   }

   @Override
   public void listAllObjects(SessionData var1, DescribeMultipleSObjectsCallback var2) {
      SalesforceInfoProviderWsImpl.Worker var3 = new SalesforceInfoProviderWsImpl.Worker(this.getConfiguration());
      var3.listAllObjects(var1, var2);
   }

   @Override
   public void describeObject(SessionData var1, String var2, DescribeSObjectCallback var3) {
      SalesforceInfoProviderWsImpl.Worker var4 = new SalesforceInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.describeObject(var1, var2, var3);
   }

   @Override
   public void describeObjects(SessionData var1, List<String> var2, DescribeMultipleSObjectsCallback var3) {
      SalesforceInfoProviderWsImpl.Worker var4 = new SalesforceInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.describeObjects(var1, var2, var3);
   }

   @Override
   public void query(SessionData var1, String var2, QueryCallback var3) {
      SalesforceInfoProviderWsImpl.Worker var4 = new SalesforceInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.query(var1, var2, var3);
   }

   @Override
   public void queryAll(SessionData var1, String var2, QueryCallback var3) {
      SalesforceInfoProviderWsImpl.Worker var4 = new SalesforceInfoProviderWsImpl.Worker(this.getConfiguration());
      var4.queryAll(var1, var2, var3);
   }

   private static class Worker {
      private static final String SERVICE_NAME = "konga_salesforce_infoprovider";
      private final WebServiceConfiguration wsConfig;
      private WsCredentials credentials;

      public Worker(WebServiceConfiguration var1) {
         this.wsConfig = var1;
      }

      public void openConnection(OrgIdentifier var1, OpenConnectionCallback var2) {
         try {
            org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider var3 = this.getInfoProvider(var2);
            if (var3 == null) {
               return;
            }

            OrgIdentifier var4 = var1.resolveDataElementDefaultValues();
            WsOrgIdentifier var5 = createWsOrgId(var4);
            WsLoginResult var6 = var3.openConnection(this.credentials, var5);
            if (Thread.currentThread().isInterrupted()) {
               var2.cancelled();
            }

            if (isSuccess(var6.getError())) {
               SessionData var7 = toClientSideSessionData(var6.getSessionData());
               var2.connectionWasOpened(var7);
            } else {
               var2.caught(toSalesforceException(var6.getError()));
            }
         } catch (RemoteException var8) {
            var2.caught(this.convert(var8));
         } catch (IntegrationServerException var9) {
            var2.caught(new IntegrationServerException("Server exception: " + var9.getMessage(), var9));
         }
      }

      private org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider getInfoProvider(ServerCallback var1) throws IntegrationServerException {
         if (Thread.currentThread().isInterrupted()) {
            var1.cancelled();
            return null;
         }

         try {
            SalesforceInfoProviderServiceLocator var2 = new SalesforceInfoProviderServiceLocator();
            StringBuilder var3 = new StringBuilder();
            if (!this.wsConfig.configureWebservice(var2, "konga_salesforce_infoprovider", var3, var1)) {
               return null;
            }

            String var4 = this.wsConfig.getUserName();
            String var5 = this.wsConfig.getPasswordHash();
            this.credentials = new WsCredentials(var4, var5);
            var2.setkonga_salesforce_infoproviderEndpointAddress(var3.toString());
            return var2.getkonga_salesforce_infoprovider();
         } catch (ServiceException var6) {
            throw new IntegrationServerException("Failed to call web service: " + var6.getMessage(), var6);
         }
      }

      private static WsOrgIdentifier createWsOrgId(OrgIdentifier var0) {
         WsOrgIdentifier var1 = new WsOrgIdentifier();
         var1.setUrl(var0.getLoginUrl());
         LoginCredentials var2 = var0.getCredentials();
         var1.setUsername(var2.username);
         var1.setPassword(var2.password);
         var1.setSecurityToken(var2.securityToken); var1.setAuthType(var2.authType); var1.setInstanceUrl(var2.instanceUrl); var1.setClientId(var2.clientId); var1.setClientSecret(var2.clientSecret);
         var1.setSalesforceVersion(var0.getVersion().getId());
         return var1;
      }

      private IntegrationServerException convert(RemoteException var1) {
         return this.wsConfig.convertToIntegrationServerException(var1);
      }

      private static boolean isSuccess(WsSalesforceError var0) {
         return Strings.isNullOrEmpty(var0.getMessage()) && Strings.isNullOrEmpty(var0.getErrorCode());
      }

      private static SessionData toClientSideSessionData(WsSessionData var0) {
         String var1 = var0.getUserId();
         if (Strings.isNullOrEmpty(var1)) {
            var1 = "unknown";
         }

         return new SessionData(var0.getSessionId(), var1, var0.getEndpoint(), var0.getTimeoutInSeconds());
      }

      private static SalesforceException toSalesforceException(WsSalesforceError var0) {
         return new SalesforceException(var0.getMessage(), var0.getErrorCode());
      }

      public void listAllObjects(SessionData var1, DescribeMultipleSObjectsCallback var2) {
         try {
            org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider var3 = this.getInfoProvider(var2);
            if (var3 == null) {
               return;
            }

            WsSessionData var4 = toServerSessionData(var1);
            WsDescribeObjectsResult var5 = var3.listAllObjects(this.credentials, var4);
            this.processDescribeMultipleObjectsResult(var5, var2);
         } catch (RemoteException var6) {
            var2.caught(this.convert(var6));
         } catch (IntegrationServerException var7) {
            var2.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
         }
      }

      public void listAllObjects2(SessionData var1, DescribeMultipleSObjectsCallback var2) {
         try {
            org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider var3 = this.getInfoProvider(var2);
            if (var3 == null) {
               return;
            }

            WsSessionData var4 = toServerSessionData(var1);
            WsSerializedObjectList var5 = var3.listAllObjects2(this.credentials, var4);
            this.processDescribeMultipleObjectsResult(var5, var2);
         } catch (RemoteException var6) {
            var2.caught(this.convert(var6));
         } catch (IntegrationServerException var7) {
            var2.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
         }
      }

      private static WsSessionData toServerSessionData(SessionData var0) {
         WsSessionData var1 = new WsSessionData();
         var1.setSessionId(var0.getSessionId());
         var1.setEndpoint(var0.getEndpoint());
         var1.setTimeoutInSeconds(var0.getSessionTimeoutInSeconds());
         return var1;
      }

      private void processDescribeMultipleObjectsResult(WsDescribeObjectsResult var1, DescribeMultipleSObjectsCallback var2) {
         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
         }

         if (isSuccess(var1.getError())) {
            WsSObjectSchema[] var3 = var1.getSchemas();
            List var4 = SchemaDeserializer.deserialize(var3);
            var2.gotDescriptions(var4);
         } else {
            var2.caught(toSalesforceException(var1.getError()));
         }
      }

      private void processDescribeMultipleObjectsResult(WsSerializedObjectList var1, DescribeMultipleSObjectsCallback var2) {
         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
         }

         if (isSuccess(var1.getError())) {
            try {
               String var3 = var1.getCompressedAndBase64EncodedBytes();
               byte[] var4 = Base64.decodeBase64(var3);
               byte[] var5 = ZipUtils.inflate(var4);
               List var6 = (List)SerializationUtils.deserialize(var5);
               var2.gotDescriptions(var6);
            } catch (Exception var7) {
               var2.caught(var7);
            }
         } else {
            var2.caught(toSalesforceException(var1.getError()));
         }
      }

      public void describeObject(SessionData var1, String var2, DescribeSObjectCallback var3) {
         try {
            org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider var4 = this.getInfoProvider(var3);
            if (var4 == null) {
               return;
            }

            WsSessionData var5 = toServerSessionData(var1);
            WsDescribeObjectsResult var6 = var4.describeObject(this.credentials, var5, var2);
            if (Thread.currentThread().isInterrupted()) {
               var3.cancelled();
            }

            if (isSuccess(var6.getError())) {
               WsSObjectSchema[] var7 = var6.getSchemas();
               if (var7.length == 0) {
                  var3.cancelled();
               } else {
                  var3.gotDescription(SchemaDeserializer.deserialize(var7[0]));
               }
            } else {
               var3.caught(toSalesforceException(var6.getError()));
            }
         } catch (RemoteException var8) {
            var3.caught(this.convert(var8));
         } catch (IntegrationServerException var9) {
            var3.caught(new IntegrationServerException("Server exception: " + var9.getMessage(), var9));
         }
      }

      public void describeObjects(SessionData var1, List<String> var2, DescribeMultipleSObjectsCallback var3) {
         try {
            org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider var4 = this.getInfoProvider(var3);
            if (var4 == null) {
               return;
            }

            WsSessionData var5 = toServerSessionData(var1);
            WsDescribeObjectsResult var6 = var4.describeObjects(this.credentials, var5, var2.toArray(new String[var2.size()]));
            this.processDescribeMultipleObjectsResult(var6, var3);
         } catch (RemoteException var7) {
            var3.caught(this.convert(var7));
         } catch (IntegrationServerException var8) {
            var3.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
         }
      }

      public void query(SessionData var1, String var2, QueryCallback var3) {
         try {
            org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider var4 = this.getInfoProvider(var3);
            if (var4 == null) {
               return;
            }

            WsSessionData var5 = toServerSessionData(var1);
            WsQueryResult var6 = var4.query(this.credentials, var5, var2);
            this.processQueryResult(var6, var3);
         } catch (RemoteException var7) {
            var3.caught(this.convert(var7));
         } catch (IntegrationServerException var8) {
            var3.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
         }
      }

      private void processQueryResult(WsQueryResult var1, QueryCallback var2) {
         if (Thread.currentThread().isInterrupted()) {
            var2.cancelled();
         }

         if (isSuccess(var1.getError())) {
            QueryResult var3 = QueryResultDeserializer.deserialize(var1);
            var2.done(var3);
         } else {
            var2.caught(toSalesforceException(var1.getError()));
         }
      }

      public void queryAll(SessionData var1, String var2, QueryCallback var3) {
         try {
            org.etlforge.integration.server.implementation.webservice.sfdc.client.SalesforceInfoProvider var4 = this.getInfoProvider(var3);
            if (var4 == null) {
               return;
            }

            WsSessionData var5 = toServerSessionData(var1);
            WsQueryResult var6 = var4.queryAll(this.credentials, var5, var2);
            this.processQueryResult(var6, var3);
         } catch (RemoteException var7) {
            var3.caught(this.convert(var7));
         } catch (IntegrationServerException var8) {
            var3.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
         }
      }
   }
}
