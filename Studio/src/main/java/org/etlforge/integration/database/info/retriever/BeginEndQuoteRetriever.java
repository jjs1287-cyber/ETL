package org.etlforge.integration.database.info.retriever;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.etlforge.integration.database.BeginEndQuote;

public final class BeginEndQuoteRetriever {
   public static BeginEndQuote getBeginEndQuote(Connection var0) throws SQLException {
      DatabaseMetaData var1 = var0.getMetaData();
      String var2 = var1.getIdentifierQuoteString();
      return new BeginEndQuote(var2);
   }

   private BeginEndQuoteRetriever() {
   }
}
