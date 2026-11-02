package org.etlforge.integration.server.engine.jdbc;

import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class DbLookupParams {
   public final IntegrationEntityId entityId;
   public final String sql;
   public final boolean autoCommit;

   public DbLookupParams(IntegrationEntityId var1, String var2, boolean var3) {
      Preconditions.checkNotNull(var1, "entityId");
      Preconditions.checkNotNull(var2, "sql");
      this.entityId = var1;
      this.sql = var2;
      this.autoCommit = var3;
   }
}
