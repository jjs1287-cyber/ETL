package org.etlforge.integration.server.db.infoprovider;

import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.retriever.BeginEndQuoteRetriever;

public final class ConnectionTester {
   private final ConnectionFactory connectionFactory;

   public ConnectionTester(ConnectionFactory var1) {
      this.connectionFactory = var1;
   }

   public TestConnectionResult testConnection() {
      Connection var1 = null;
      boolean var2 = false;
      BeginEndQuote var3 = null;
      Exception var4 = null;

      try {
         var1 = this.connectionFactory.newConnection();
         var2 = true;

         try {
            var3 = BeginEndQuoteRetriever.getBeginEndQuote(var1);
         } catch (SQLException var10) {
         }
      } catch (Exception var11) {
         var4 = var11;
      } finally {
         KongaDbUtils.close(var1);
      }

      return new TestConnectionResult(var2, var3, var4);
   }
}
