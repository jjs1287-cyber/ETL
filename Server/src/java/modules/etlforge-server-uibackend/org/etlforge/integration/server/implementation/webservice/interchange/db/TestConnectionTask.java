package org.etlforge.integration.server.implementation.webservice.interchange.db;

import java.rmi.RemoteException;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.server.db.infoprovider.ConnectionTester;
import org.etlforge.integration.server.db.infoprovider.DefaultConnectionFactory;
import org.etlforge.integration.server.db.infoprovider.TestConnectionResult;

final class TestConnectionTask extends Task<WsJdbcBeginEndQuote> {
   public TestConnectionTask(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6) {
      super("testConnection", var1, var2, var3, var4, var5, var6);
   }

   protected WsJdbcBeginEndQuote runTask(DefaultConnectionFactory var1) throws Exception {
      ConnectionTester var2 = new ConnectionTester(var1);
      TestConnectionResult var3 = var2.testConnection();
      if (var3 == null) {
         throw new RemoteException("Failed to test the connection. Reason: unknown.");
      } else if (var3.connectionError != null) {
         throw var3.connectionError;
      } else {
         return convertBeginEndQuotes(var3.beginEndQuote);
      }
   }

   private static WsJdbcBeginEndQuote convertBeginEndQuotes(BeginEndQuote var0) {
      return new WsJdbcBeginEndQuote(var0.begin, var0.end);
   }
}
