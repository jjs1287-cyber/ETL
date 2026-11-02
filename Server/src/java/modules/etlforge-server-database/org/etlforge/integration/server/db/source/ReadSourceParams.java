package org.etlforge.integration.server.db.source;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.db.DataFileLogFileHack;

public final class ReadSourceParams {
   public final SourceId sourceId;
   public final ConnectionParams connectionParameters;
   public final String jtrFile;
   public final String dataFile;
   public final String logFile;
   public String adHocFilter;
   public int fetchSize;
   public String structureFile;

   public ReadSourceParams(SourceId var1, ConnectionParams var2, String var3, String var4) {
      Preconditions.checkNotNull(var1, "sourceId");
      Preconditions.checkNotNull(var2, "connParams");
      Preconditions.checkNotNull(var3, "jtrFile");
      Preconditions.checkNotNull(var4, "dataFile");
      DataFileLogFileHack var5 = new DataFileLogFileHack(var4);
      this.dataFile = var5.dataFile;
      this.logFile = var5.logFile;
      this.sourceId = var1;
      this.connectionParameters = var2;
      this.jtrFile = var3;
   }
}
