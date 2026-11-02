package org.etlforge.integration.server.implementation.webservice.interchange.db;

import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseInfo;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.integration.server.db.infoprovider.DatabaseInfoRetriever;
import org.etlforge.integration.server.db.infoprovider.DefaultConnectionFactory;

final class GetDatabaseObjectsTask extends Task<WsJdbcDatabaseInfo> {
   private final TableSearchFilter filter;

   public GetDatabaseObjectsTask(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6, TableSearchFilter var7) {
      super("getDatabaseObjects", var1, var2, var3, var4, var5, var6);
      this.filter = var7;
   }

   protected WsJdbcDatabaseInfo runTask(DefaultConnectionFactory var1) throws Exception {
      DatabaseInfoRetriever var2 = new DatabaseInfoRetriever(var1);
      var2.setFilter(this.filter);
      DatabaseInfo var3 = var2.getInfo();
      logDbInfo(var3);
      WsJdbcDatabaseInfo var4 = convertDatabaseObjects(var3.getObjects());
      var4.setQuotes(convertBeginEndQuotes(var3.getBeginEndQuote()));
      return var4;
   }

   private static void logDbInfo(DatabaseInfo var0) {
      String var1 = String.format("Database Info:\nNumber of tables: %d\nBegin-End Quote: %s", var0.getObjects().size(), var0.getBeginEndQuote());
      ServerUiBackendLog.LOG.info("Connection params: " + var1);
   }

   private static WsJdbcBeginEndQuote convertBeginEndQuotes(BeginEndQuote var0) {
      return new WsJdbcBeginEndQuote(var0.begin, var0.end);
   }
}
