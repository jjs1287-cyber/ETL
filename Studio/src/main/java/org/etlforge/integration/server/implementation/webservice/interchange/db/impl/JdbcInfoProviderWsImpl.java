package org.etlforge.integration.server.implementation.webservice.interchange.db.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.database.serverapi.GetDatabaseObjectCallback;
import org.etlforge.integration.client.database.serverapi.GetTableInfoCallback;
import org.etlforge.integration.client.database.serverapi.JdbcDriverListCallback;
import org.etlforge.integration.client.database.serverapi.JdbcInfoProvider;
import org.etlforge.integration.client.database.serverapi.WhereClauseValidationCallback;
import org.etlforge.integration.client.datalocation.connection.serverapi.DatabaseConnectionCallback;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsConnectionParams;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcBeginEndQuote;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcDatabaseInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcDatabaseObject;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsJdbcDriverInfo;
import org.etlforge.lang.Arrays2;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class JdbcInfoProviderWsImpl extends JavaWebServiceServerCall implements JdbcInfoProvider {
   private static final String SERVICE_NAME = "konga_jdbc_infoprovider";
   private String user;
   private String password;

   public JdbcInfoProviderWsImpl(JavaWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void getDrivers(JdbcDriverListCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProvider var2 = this.getProvider(var1);
         if (var2 == null) {
            return;
         }

         ServerInfo var3 = this.getConfiguration().getServerInfo();
         if (Thread.currentThread().isInterrupted()) {
            var1.cancelled();
            return;
         }

         WsJdbcDriverInfo var4 = var2.getDrivers(this.user, this.password);
         List var5 = convertDriverInfo(var4);
         var1.driversRetrieved((JdbcDriverDescriptor[]) (JdbcDriverDescriptor[]) var5.toArray(new JdbcDriverDescriptor[var5.size()]), var3);
      } catch (RemoteException var6) {
         var1.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var1.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   @Override
   public void getDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, TableSearchFilter var4, GetDatabaseObjectCallback var5) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProvider var6 = this.getProvider(var5);
         if (var6 == null) {
            return;
         }

         if (Thread.currentThread().isInterrupted()) {
            var5.cancelled();
            return;
         }

         WsConnectionParams var7 = createConnectionParams(var1);
         String[] var8 = Utils.getTypeNames(var4, DriverType.JDBC);
         WsJdbcDatabaseInfo var9 = var6.getDatabaseObjects(
            this.user, this.password, var7, var1.getDriverName(), toString(var2), toString(var3), var8, var4.getSearchString()
         );
         DatabaseObjectContainer[] var10 = convertObjectInfo(var9);
         BeginEndQuote var11 = new BeginEndQuote(var9.getQuotes().getBeginQuote(), var9.getQuotes().getEndQuote());
         if (Thread.currentThread().isInterrupted()) {
            var5.cancelled();
            return;
         }

         var5.objectsRetrieved(var10, var11);
      } catch (RemoteException var12) {
         var5.caught(this.convert(var12));
      } catch (IntegrationServerException var13) {
         var5.caught(new IntegrationServerException("Server exception: " + var13.getMessage(), var13));
      }
   }

   @Override
   public void getAllDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, GetDatabaseObjectCallback var4) {
      this.getDatabaseObjects(var1, var2, var3, TableSearchFilter.ALL_OBJECTS, var4);
   }

   @Override
   public void getTableInfo(ConnectionParams var1, SourceId var2, TargetId var3, DatabaseObject[] var4, GetTableInfoCallback var5) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProvider var6 = this.getProvider(var5);
         if (var6 == null) {
            return;
         }

         if (Thread.currentThread().isInterrupted()) {
            var5.cancelled();
            return;
         }

         WsConnectionParams var7 = createConnectionParams(var1);
         WsJdbcDatabaseObject[] var8 = Arrays2.transform(var4, WsJdbcDatabaseObject.class, JdbcFunctions.TO_WS_TABLES);
         WsJdbcBeginEndQuote var9 = createWsQuotes(var1.getBeginEndQuotes());
         WsJdbcDatabaseInfo var10 = var6.getTableInfo(this.user, this.password, var7, var1.getDriverName(), var9, toString(var2), toString(var3), var8);
         DatabaseObjectContainer[] var11 = convertObjectInfo(var10);
         var5.tableInfoRetrieved(var11);
      } catch (RemoteException var12) {
         var5.caught(this.convert(var12));
      } catch (IntegrationServerException var13) {
         var5.caught(new IntegrationServerException("Server exception: " + var13.getMessage(), var13));
      }
   }

   @Override
   public void validateWhereClause(DatabaseObject var1, String var2, WhereClauseValidationCallback var3) {
      var3.caught(new NotImplementedYetException());
   }

   public void testConnection(DatabaseLocation var1, SourceId var2, TargetId var3, DatabaseConnectionCallback var4) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProvider var5 = this.getProvider(var4);
         if (var5 == null) {
            return;
         }

         if (Thread.currentThread().isInterrupted()) {
            var4.cancelled();
            return;
         }

         WsConnectionParams var6 = createConnectionParams(var1.getConnectionParams());
         WsJdbcBeginEndQuote var7 = var5.testConnection(this.user, this.password, var6, var1.getDriverName(), toString(var2), toString(var3));
         var4.connected(JdbcFunctions.FROM_WS_QUOTES.apply(var7));
      } catch (RemoteException var8) {
         var4.caught(this.convert(var8));
      } catch (IntegrationServerException var9) {
         var4.caught(new IntegrationServerException("Server exception: " + var9.getMessage(), var9));
      }
   }

   private org.etlforge.integration.server.implementation.webservice.interchange.db.client.JdbcInfoProvider getProvider(ServerCallback var1) throws IntegrationServerException {
      if (Thread.currentThread().isInterrupted()) {
         var1.cancelled();
         return null;
      }

      try {
         JdbcInfoProviderServiceLocator var2 = new JdbcInfoProviderServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_jdbc_infoprovider", var3, var1)) {
            return null;
         }

         this.user = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_jdbc_infoproviderEndpointAddress(var3.toString());
         return var2.getkonga_jdbc_infoprovider();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   private static List<JdbcDriverDescriptor> convertDriverInfo(WsJdbcDriverInfo var0) {
      return Lists.transform(Arrays.asList(var0.getReturnedDrivers()), JdbcFunctions.FROM_WS_DRIVER);
   }

   private static WsConnectionParams createConnectionParams(ConnectionParams var0) {
      WsConnectionParams var1 = new WsConnectionParams();
      var1.setServer(var0.getServer());
      var1.setPort(var0.getPort().getPort());
      var1.setDatabase(var0.getDatabase());
      var1.setUser(var0.getUser());
      var1.setPassword(var0.getPassword());
      var1.setManualConnection(var0.getManualConnectionString());
      var1.setAdditionalParams(var0.getAdditionalParams());
      return var1;
   }

   private static WsJdbcBeginEndQuote createWsQuotes(BeginEndQuote var0) {
      return new WsJdbcBeginEndQuote(var0.begin, var0.end);
   }

   private static DatabaseObjectContainer[] convertObjectInfo(WsJdbcDatabaseInfo var0) {
      EnumMap var1 = Maps.newEnumMap(DatabaseObjectType.class);
      WsJdbcDatabaseObject[] var2 = var0.getObjects();
      if (var2 != null) {
         DatabaseObject[] var3 = Arrays2.transform(var2, DatabaseObject.class, JdbcFunctions.FROM_WS_OBJECT);

         for (DatabaseObject var7 : var3) {
            if (var7 != null) {
               DatabaseObjectType var8 = var7.getType();
               DatabaseObjectContainer var9 = (DatabaseObjectContainer)var1.get(var8);
               if (var9 == null) {
                  var9 = new DatabaseObjectContainer(var8);
                  var1.put(var8, var9);
               }

               var9.addObject(var7);
            }
         }
      }

      return (DatabaseObjectContainer[]) var1.values().toArray(new DatabaseObjectContainer[var1.size()]);
   }

   private static String toString(Object var0) {
      return var0 != null ? var0.toString() : null;
   }
}
