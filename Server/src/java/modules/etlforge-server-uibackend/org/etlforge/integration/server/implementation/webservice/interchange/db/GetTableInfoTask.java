package org.etlforge.integration.server.implementation.webservice.interchange.db;

import java.util.Arrays;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.integration.server.conf.PreconfiguredDataElements;
import org.etlforge.integration.server.db.infoprovider.DatabaseColumnRetriever;
import org.etlforge.integration.server.db.infoprovider.DefaultConnectionFactory;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.lang.Arrays2;

final class GetTableInfoTask extends Task<WsJdbcDatabaseInfo> {
   private final WsJdbcDatabaseObject[] wsTables;
   private final WsJdbcBeginEndQuote wsQuotes;

   public GetTableInfoTask(
      String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6, WsJdbcDatabaseObject[] var7, WsJdbcBeginEndQuote var8
   ) {
      super("getTableInfo", var1, var2, var3, var4, var5, var6);
      this.wsTables = var7;
      this.wsQuotes = var8;
   }

   protected WsJdbcDatabaseInfo runTask(DefaultConnectionFactory var1) throws Exception {
      BeginEndQuote var2 = this.wsQuotes != null ? new BeginEndQuote(this.wsQuotes.getBeginQuote(), this.wsQuotes.getEndQuote()) : null;
      DatabaseColumnRetriever var3 = this.createColumnRetriever(var1, var2);
      DatabaseObject[] var4 = this.convertIncomingTableInfo();
      var3.populateColumns(var4);
      logTableInfo(var4, var2);
      return convertDatabaseObjects(Arrays.asList(var4));
   }

   private DatabaseColumnRetriever createColumnRetriever(DefaultConnectionFactory var1, BeginEndQuote var2) throws ServerConfigurationException {
      DatabaseColumnRetriever var3 = new DatabaseColumnRetriever(var1);
      var3.setLogger(ServerUiBackendLog.LOG);
      var3.setBeginEndQuote(var2);
      var3.setInformix(var1.isInformixConnection());
      return var3;
   }

   private DatabaseObject[] convertIncomingTableInfo() {
      DatabaseObjectConverter var1 = new DatabaseObjectConverter(PreconfiguredDataElements.getContext());
      return Arrays2.transform(this.wsTables, DatabaseObject.class, var1);
   }

   private static void logTableInfo(DatabaseObject[] var0, BeginEndQuote var1) {
      TableInfoLogger var2 = new TableInfoLogger(ServerUiBackendLog.LOG);
      var2.log(var0, var1);
   }
}
