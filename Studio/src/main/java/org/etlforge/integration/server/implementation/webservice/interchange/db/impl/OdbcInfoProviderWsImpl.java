package org.etlforge.integration.server.implementation.webservice.interchange.db.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.database.serverapi.GetDatabaseObjectCallback;
import org.etlforge.integration.client.database.serverapi.GetTableInfoCallback;
import org.etlforge.integration.client.database.serverapi.OdbcDriverListCallback;
import org.etlforge.integration.client.database.serverapi.OdbcInfoProvider;
import org.etlforge.integration.client.database.serverapi.WhereClauseValidationCallback;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.integration.database.driver.OdbcDriverDescriptor;
import org.etlforge.integration.database.driver.OdbcDriverDescriptorFactory;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectContainer;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.OdbcInfoProviderServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsDatabaseDriverInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsDatabaseInfo;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsDatabaseObject;
import org.etlforge.integration.server.implementation.webservice.interchange.db.client.WsOdbcDriverDescriptor;
import org.etlforge.lang.Arrays2;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class OdbcInfoProviderWsImpl extends CppWebServiceServerCall implements OdbcInfoProvider {
   private static final String SERVICE_NAME = "konga_odbc_infoprovider";
   private String userName = "";
   private String password = "";

   public OdbcInfoProviderWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   private org.etlforge.integration.server.implementation.webservice.interchange.db.client.OdbcInfoProvider getDatabaseInfoProvider(ServerCallback var1) throws IntegrationServerException {
      OdbcInfoProviderServiceLocator var2 = new OdbcInfoProviderServiceLocator();

      try {
         StringBuilder var3 = new StringBuilder();
         if (!this.getConfiguration().configureWebservice(var2, "konga_odbc_infoprovider", var3, var1)) {
            return null;
         }

         this.userName = this.getConfiguration().getUserName();
         this.password = this.getConfiguration().getPasswordHash();
         var2.setkonga_odbc_infoproviderEndpointAddress(var3.toString());
         return var2.getkonga_odbc_infoprovider();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
      }
   }

   @Override
   public void getAllDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, GetDatabaseObjectCallback var4) {
      this.getDatabaseObjects(var1, var2, var3, TableSearchFilter.ALL_OBJECTS, var4);
   }

   @Override
   public void getDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, TableSearchFilter var4, GetDatabaseObjectCallback var5) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.db.client.OdbcInfoProvider var6 = this.getDatabaseInfoProvider(var5);
         if (var6 == null) {
            return;
         }

         String[] var7 = Utils.getTypeNames(var4, DriverType.ODBC);
         WsDatabaseInfo var8 = var6.getDatabaseObjects(
            this.userName, this.password, var1.getDriverName(), var1.getConnectionString(), toString(var2), toString(var3), var7, var4.getSearchString()
         );
         DatabaseObjectContainer[] var9 = convertObjectInfo(var8);
         BeginEndQuote var10 = new BeginEndQuote(var8.getQuoteBegin(), var8.getQuoteEnd());
         var5.objectsRetrieved(var9, var10);
      } catch (RemoteException var11) {
         var5.caught(this.convert(var11));
      } catch (IntegrationServerException var12) {
         var5.caught(new IntegrationServerException("Server exception: " + var12.getMessage(), var12));
      }
   }

   private static String toString(Object var0) {
      return var0 != null ? var0.toString() : null;
   }

   private static DatabaseObjectContainer[] convertObjectInfo(WsDatabaseInfo var0) {
      EnumMap var1 = Maps.newEnumMap(DatabaseObjectType.class);
      WsDatabaseObject[] var2 = var0.getDbInfo();
      if (var2 != null) {
         DatabaseObject[] var3 = Arrays2.transform(var2, DatabaseObject.class, OdbcFunctions.FROM_WS_OBJECT);

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

   @Override
   public void getTableInfo(ConnectionParams var1, SourceId var2, TargetId var3, DatabaseObject[] var4, GetTableInfoCallback var5) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.db.client.OdbcInfoProvider var6 = this.getDatabaseInfoProvider(var5);
         if (var6 == null) {
            return;
         }

         WsDatabaseObject[] var7 = Arrays2.transform(var4, WsDatabaseObject.class, OdbcFunctions.TO_WS_TABLES);
         WsDatabaseInfo var8 = var6.getTableInfo(
            this.userName, this.password, var1.getDriverName(), var1.getConnectionString(), toString(var2), toString(var3), var7
         );
         DatabaseObjectContainer[] var9 = convertObjectInfo(var8);
         if (Thread.currentThread().isInterrupted()) {
            var5.cancelled();
            return;
         }

         var5.tableInfoRetrieved(var9);
      } catch (RemoteException var10) {
         var5.caught(this.convert(var10));
      } catch (IntegrationServerException var11) {
         var5.caught(new IntegrationServerException("Server exception: " + var11.getMessage(), var11));
      }
   }

   @Override
   public void validateWhereClause(DatabaseObject var1, String var2, WhereClauseValidationCallback var3) {
      var3.caught(new NotImplementedYetException());
   }

   @Override
   public void getDrivers(OdbcDriverListCallback var1) {
      try {
         org.etlforge.integration.server.implementation.webservice.interchange.db.client.OdbcInfoProvider var2 = this.getDatabaseInfoProvider(var1);
         if (var2 == null) {
            return;
         }

         ServerInfo var3 = this.getConfiguration().getServerInfo();
         WsDatabaseDriverInfo var4 = var2.getDrivers(this.userName, this.password);
         List var5 = this.translateRetrievedDriverInfo(var4);
         var1.driversRetrieved(var5, var3);
      } catch (RemoteException var6) {
         var1.caught(this.convert(var6));
      } catch (IntegrationServerException var7) {
         var1.caught(new IntegrationServerException("Server exception: " + var7.getMessage(), var7));
      }
   }

   private List<OdbcDriverDescriptor> translateRetrievedDriverInfo(WsDatabaseDriverInfo var1) {
      WsOdbcDriverDescriptor[] var2 = var1.getReturnedDrivers();
      ArrayList var3 = Lists.newArrayListWithExpectedSize(var2.length);

      for (WsOdbcDriverDescriptor var7 : var2) {
         var3.add(OdbcDriverDescriptorFactory.createDescriptor(var7.getName(), var7.getQuoteBegin(), var7.getQuoteEnd()));
      }

      return var3;
   }
}
