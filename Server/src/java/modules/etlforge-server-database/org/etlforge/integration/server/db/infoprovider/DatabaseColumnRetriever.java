package org.etlforge.integration.server.db.infoprovider;

import com.google.common.base.Preconditions;
import java.sql.SQLException;
import java.util.logging.Logger;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.DatabaseObjectType;
import org.etlforge.integration.database.info.InformixUtils;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;

public final class DatabaseColumnRetriever {
   private final ConnectionFactory connectionFactory;
   private BeginEndQuote beginEndQuote;
   private boolean informix;
   private Logger logger;

   public DatabaseColumnRetriever(ConnectionFactory var1) {
      this.connectionFactory = var1;
   }

   public void setBeginEndQuote(BeginEndQuote var1) {
      this.beginEndQuote = var1;
   }

   public void setInformix(boolean var1) {
      this.informix = var1;
   }

   public void setLogger(Logger var1) {
      Preconditions.checkNotNull(var1, "logger");
      this.logger = var1;
   }

   public void populateColumns(DatabaseObject[] var1) throws ServerDbException, ServerAccessException, SQLException {
      org.etlforge.integration.database.info.retriever.DatabaseColumnRetriever var2 = new org.etlforge.integration.database.info.retriever.DatabaseColumnRetriever();
      var2.setLogger(this.logger);

      try {
         var2.setBeginEndQuote(this.beginEndQuote);
         var2.setConnection(this.connectionFactory.newConnection());

         for (DatabaseObject var6 : var1) {
            var2.populateColumns(var6);
            if (this.informix && var6.getType() != DatabaseObjectType.MANUAL_SQL) {
               InformixUtils.addRowIdColumn(var6);
            }
         }
      } finally {
         var2.close();
      }
   }
}
