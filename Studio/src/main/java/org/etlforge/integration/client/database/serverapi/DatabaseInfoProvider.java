package org.etlforge.integration.client.database.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.integration.database.info.retriever.TableSearchFilter;

public interface DatabaseInfoProvider extends ServerCall {
   void getDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, TableSearchFilter var4, GetDatabaseObjectCallback var5);

   void getAllDatabaseObjects(ConnectionParams var1, SourceId var2, TargetId var3, GetDatabaseObjectCallback var4);

   void getTableInfo(ConnectionParams var1, SourceId var2, TargetId var3, DatabaseObject[] var4, GetTableInfoCallback var5);

   void validateWhereClause(DatabaseObject var1, String var2, WhereClauseValidationCallback var3);
}
