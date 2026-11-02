package org.etlforge.integration.server.implementation.webservice.interchange.db;

import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.EnumSet;
import java.util.List;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.integration.server.conf.JdbcDriversConfig;
import org.etlforge.integration.server.ws.WsUtils;

public final class Konga_jdbc_infoproviderSoapBindingImpl implements JdbcInfoProvider {
   @Override
   public WsJdbcDatabaseInfo getAllDatabaseObjects(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6) throws RemoteException {
      return this.getDatabaseObjectsImpl(var1, var2, var3, var4, var5, var6, TableSearchFilter.ALL_OBJECTS);
   }

   private WsJdbcDatabaseInfo getDatabaseObjectsImpl(
      String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6, TableSearchFilter var7
   ) throws RemoteException {
      GetDatabaseObjectsTask var8 = new GetDatabaseObjectsTask(var1, var2, var3, var4, var5, var6, var7);
      return var8.execute();
   }

   @Override
   public WsJdbcDatabaseInfo getDatabaseObjects(
      String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6, String[] var7, String var8
   ) throws RemoteException {
      EnumSet var9 = EnumSet.noneOf(DatabaseObjectType.class);

      for (String var13 : var7) {
         var9.add(DatabaseObjectType.valueOf(var13));
      }

      TableSearchFilter var14 = new TableSearchFilter(var8, var9);
      return this.getDatabaseObjectsImpl(var1, var2, var3, var4, var5, var6, var14);
   }

   @Override
   public WsJdbcDatabaseInfo getTableInfo(
      String var1, String var2, WsConnectionParams var3, String var4, WsJdbcBeginEndQuote var5, String var6, String var7, WsJdbcDatabaseObject[] var8
   ) throws RemoteException {
      GetTableInfoTask var9 = new GetTableInfoTask(var1, var2, var3, var4, var6, var7, var8, var5);
      return var9.execute();
   }

   @Override
   public WsJdbcBeginEndQuote testConnection(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6) throws RemoteException {
      TestConnectionTask var7 = new TestConnectionTask(var1, var2, var3, var4, var5, var6);
      return var7.execute();
   }

   @Override
   public WsJdbcDriverInfo getDrivers(String var1, String var2) throws RemoteException {
      try {
         ServerUiBackendLog.LOG.info("Entering JdbcInfoProvider.getDrivers()");
         WsUtils.authenticate(var1, var2);
         JdbcDriversConfig var3 = JdbcDriversConfig.getConfig();
         List var4 = var3.getRegisteredDrivers();
         return convertDriverData(var4);
      } catch (Exception var6) {
         WsUtils.convertError(var6, this.getClass(), "getDrivers");
         return null;
      }
   }

   private static WsJdbcDriverInfo convertDriverData(List<JdbcDriverDescriptor> var0) {
      ServerUiBackendLog.LOG.info("There are " + var0.size() + " JDBC driver(s) registered on the server");
      List var1 = Lists.transform(var0, JdbcFunctions.TO_WS_DRIVER);
      return new WsJdbcDriverInfo((WsJdbcDriverDescriptor[]) var1.toArray(new WsJdbcDriverDescriptor[var1.size()]));
   }
}
