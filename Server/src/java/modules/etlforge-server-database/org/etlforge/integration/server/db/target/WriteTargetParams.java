package org.etlforge.integration.server.db.target;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.db.DataFileLogFileHack;

public final class WriteTargetParams {
   public final TargetId targetId;
   public final ConnectionParams connectionParameters;
   public final String jtrFile;
   public final String dataFile;
   public final String logFile;
   public boolean isTransaction;
   public boolean includeNulls;
   public String preTargetSql;
   public DbInsertMode insertMode = DbInsertMode.INSERT_ONLY;

   public WriteTargetParams(TargetId var1, ConnectionParams var2, String var3, String var4) {
      Preconditions.checkNotNull(var1, "targetId");
      Preconditions.checkNotNull(var2, "connParams");
      Preconditions.checkNotNull(var3, "jtrFile");
      Preconditions.checkNotNull(var4, "dataFile");
      this.targetId = var1;
      this.connectionParameters = var2;
      this.jtrFile = var3;
      DataFileLogFileHack var5 = new DataFileLogFileHack(var4);
      this.dataFile = var5.dataFile;
      this.logFile = var5.logFile;
   }
}
