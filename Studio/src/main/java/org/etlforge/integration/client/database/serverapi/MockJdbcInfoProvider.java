package org.etlforge.integration.client.database.serverapi;

import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;
import org.etlforge.lang.exception.NotImplementedYetException;

final class MockJdbcInfoProvider implements JdbcInfoProvider {
   @Override
   public void getDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, TableSearchFilter var4, GetDatabaseObjectCallback var5) {
      var5.caught(new NotImplementedYetException());
   }

   @Override
   public void getAllDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, GetDatabaseObjectCallback var4) {
      var4.caught(new NotImplementedYetException());
   }

   @Override
   public void getDrivers(JdbcDriverListCallback var1) {
      JdbcDriverDescriptor var2 = new JdbcDriverDescriptor("Postgresql", "jdbc:postgresql://%s/%s", "org.postgresql.Driver", new BeginEndQuote("\"", "\""));
      var1.driversRetrieved(new JdbcDriverDescriptor[]{var2}, null);
   }

   @Override
   public void getTableInfo(ConnectionParams var1, SourceId var2, TargetId var3, DatabaseObject[] var4, GetTableInfoCallback var5) {
      var5.caught(new NotImplementedYetException());
   }

   @Override
   public void validateWhereClause(DatabaseObject var1, String var2, WhereClauseValidationCallback var3) {
      var3.caught(new NotImplementedYetException());
   }
}
