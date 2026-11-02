package org.etlforge.integration.server.implementation.webservice.interchange.db;

import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.List;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.dataelement.DataElementContext;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.conf.PreconfiguredDataElements;
import org.etlforge.integration.server.db.LocationPasswordLookup;
import org.etlforge.integration.server.db.infoprovider.DefaultConnectionFactory;
import org.etlforge.integration.server.ws.WsUtils;

abstract class Task<T> {
   private final String name;
   private final String etlforgeUser;
   private final String etlforgePassword;
   private final WsConnectionParams wsConnParams;
   private final String driverName;
   private final String sourceGuidAsString;
   private final String targetGuidAsString;

   protected Task(String var1, String var2, String var3, WsConnectionParams var4, String var5, String var6, String var7) {
      this.name = var1;
      this.etlforgeUser = var2;
      this.etlforgePassword = var3;
      this.wsConnParams = var4;
      this.driverName = var5;
      this.sourceGuidAsString = var6;
      this.targetGuidAsString = var7;
   }

   public final T execute() throws RemoteException {
      ExternalApiSupport var1 = null;

      try {
         ServerUiBackendLog.LOG.info("Entering JdbcInfoProvider." + this.name);
         var1 = ExternalApiSupport.forCall(this.etlforgeUser, this.etlforgePassword);
         DefaultConnectionFactory var2 = this.createConnectionFactory(var1);
         return this.runTask(var2);
      } catch (Exception var9) {
         WsUtils.convertError(var9, this.getClass(), this.name);
      } catch (Error var10) {
         WsUtils.convertError(var10, this.getClass(), this.name);
      } finally {
         ExternalApiSupport.safeDispose(var1);
      }

      return null;
   }

   protected abstract T runTask(DefaultConnectionFactory var1) throws Exception;

   private DefaultConnectionFactory createConnectionFactory(ExternalApiSupport var1) throws ServerAccessException {
      SourceId var2 = null;
      if (this.sourceGuidAsString != null) {
         try {
            var2 = new SourceId(this.sourceGuidAsString);
         } catch (Exception var7) {
            throw new RuntimeException("Invalid source GUID: " + this.sourceGuidAsString);
         }
      }

      TargetId var3 = null;
      if (this.targetGuidAsString != null) {
         try {
            var3 = new TargetId(this.targetGuidAsString);
         } catch (Exception var6) {
            throw new RuntimeException("Invalid target GUID: " + this.targetGuidAsString);
         }
      }

      DefaultConnectionFactory var4 = new DefaultConnectionFactory(convertWsConnParams(this.driverName, this.wsConnParams), var2, var3);
      LocationPasswordLookup var5 = new LocationPasswordLookup(var1.getTranDbConnection());
      var5.enableAccessControl(var1.getUser(), var1.getAccessController());
      var4.setLocationPasswordLookup(var5);
      return var4;
   }

   private static ConnectionParams convertWsConnParams(String var0, WsConnectionParams var1) {
      DataElementContext var2 = PreconfiguredDataElements.getContext();
      ConnectionParametersConverter var3 = new ConnectionParametersConverter(var2);
      ConnectionParams var4 = var3.convert(var0, var1);
      logConnectionParams(var4);
      return var4;
   }

   private static void logConnectionParams(ConnectionParams var0) {
      String var1 = String.format("server=%s database=%s user=%s", var0.getServer(), var0.getDatabase(), var0.getUser());
      ServerUiBackendLog.LOG.info("Connection params: " + var1);
   }

   protected static final WsJdbcDatabaseInfo convertDatabaseObjects(List<DatabaseObject> var0) {
      List var1 = Lists.transform(var0, JdbcFunctions.TO_WS_OBJECT);
      return new WsJdbcDatabaseInfo((WsJdbcDatabaseObject[]) var1.toArray(new WsJdbcDatabaseObject[var1.size()]), null);
   }
}
