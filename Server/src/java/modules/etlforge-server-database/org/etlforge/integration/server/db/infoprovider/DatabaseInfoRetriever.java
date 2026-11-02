package org.etlforge.integration.server.db.infoprovider;

import java.sql.SQLException;
import org.etlforge.integration.database.info.DatabaseInfo;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;

public final class DatabaseInfoRetriever {
   private final ConnectionFactory connectionFactory;
   private TableSearchFilter filter;

   public DatabaseInfoRetriever(ConnectionFactory var1) {
      this.connectionFactory = var1;
      this.filter = TableSearchFilter.ALL_OBJECTS;
   }

   public void setFilter(TableSearchFilter var1) {
      this.filter = var1;
   }

   public DatabaseInfo getInfo() throws ServerDbException, ServerAccessException, SQLException {
      org.etlforge.integration.database.info.retriever.DatabaseInfoRetriever var1 = new org.etlforge.integration.database.info.retriever.DatabaseInfoRetriever();

      try {
         var1.setSearchFilter(this.filter);
         var1.setConnection(this.connectionFactory.newConnection());
         return var1.getInfo();
      } finally {
         var1.close();
      }
   }
}
