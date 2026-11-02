package org.etlforge.integration.server.db;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.server.ServerGuidLookup;
import org.etlforge.integration.server.db.trandb.KeyValuesTab;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class ServerGuidDatabaseLookup implements ServerGuidLookup {
   private final Connection tranDbConnection;
   private ServerGuid guid;

   public ServerGuidDatabaseLookup(Connection var1) {
      this.tranDbConnection = Preconditions.checkNotNull(var1);
   }

   @Override
   public ServerGuid lookupGuid() throws ServerConfigurationException {
      if (this.guid == null) {
         this.guid = this.lookupGuidFromDatabase();
      }

      return this.guid;
   }

   private ServerGuid lookupGuidFromDatabase() throws ServerConfigurationException {
      try {
         KeyValuesTab var1 = new KeyValuesTab(this.tranDbConnection);
         String var2 = var1.getValueForKey("ServerGUID");
         if (var2 == null) {
            throw new ServerConfigurationException("The ServerGUID has not been set in the backend database.");
         } else {
            return new ServerGuid(var2);
         }
      } catch (SQLException var3) {
         throw new ServerConfigurationException(var3);
      }
   }
}
