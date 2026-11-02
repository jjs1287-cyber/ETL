package org.etlforge.integration.server.db.infoprovider;

import org.etlforge.integration.database.BeginEndQuote;

public final class TestConnectionResult {
   public final boolean connected;
   public final BeginEndQuote beginEndQuote;
   public final Exception connectionError;

   public TestConnectionResult(boolean var1, BeginEndQuote var2, Exception var3) {
      this.connected = var1;
      this.beginEndQuote = var2;
      this.connectionError = var3;
   }
}
